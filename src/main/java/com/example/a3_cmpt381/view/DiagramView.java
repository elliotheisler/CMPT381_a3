package com.example.a3_cmpt381.view;

import com.example.a3_cmpt381.AppController;
import com.example.a3_cmpt381.model.*;
import com.example.a3_cmpt381.model.sm_item.CustomRectangle;
import com.example.a3_cmpt381.model.sm_item.SMItem;
import com.example.a3_cmpt381.model.sm_item.SMStateNode;
import com.example.a3_cmpt381.model.sm_item.SMTransitionLink;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.*;

public class DiagramView extends StackPane implements ModelListener {
    public static final double WIDTH = 300;
    public static final double HEIGHT = WIDTH * 2 / 3;
    
    private Pane viewport = new Pane();
    
    private Map<SMItem, Rectangle> itemProjections = new HashMap();
    private Map<SMTransitionLink, Line> sourceArrows = new HashMap();
    private Map<SMTransitionLink, Line> drainArrows = new HashMap();
    private Line linkingLine = new Line();
    
    private SMModel smModel;
    public void setSMModel(SMModel smModel) {
        this.smModel = smModel;
    }


    private InteractionModel iModel;
    public void setIModel(InteractionModel iModel) {
        this.iModel = iModel;
    }


//    private ViewportModel vModel;
//    public void setViewportModel(ViewportModel vModel) {
//        this.vModel = vModel;
//    }


    public void setController(AppController controller) {
        viewport.setOnMousePressed(e -> controller.mousePressCanvas(e));
        viewport.setOnMouseClicked(e -> controller.mouseClickCanvas(e));
        viewport.setOnMouseReleased(e -> controller.mouseReleaseCanvas(e));
        viewport.setOnMouseDragged(e -> controller.mouseDraggedCanvas(e));
    }


    public DiagramView() {
        getStyleClass().add("DiagramView");
        viewport.getStyleClass().add("viewport");
        viewport.setPrefSize(WIDTH, HEIGHT);
        viewport.getChildren().add(linkingLine);
        getChildren().add(viewport);
    }
    
    public void modelChanged(Class<?> c) {
        SMItem changedItem = iModel.getChangedItem();
        SMStateNode changedNode;
        SMTransitionLink changedLink;
        Rectangle projection;
        Point2D start, middle, end;
        switch (iModel.getLastChange()) {
            case SELECT:
                projection = itemProjections.get(changedItem);
//                projection.setStroke
            case DRAGGING_NODE:
                changedNode = (SMStateNode) changedItem;
                updateItemRect(changedNode);
                updateDrainArrows(smModel.getIncomingLinks(changedNode));
                updateSourceArrows(smModel.getOutgoingLinks(changedNode));
                break;
            case DRAGGING_LINK:
                changedLink = (SMTransitionLink) changedItem;
                updateItemRect(changedLink);
                updateDrainArrow(changedLink);
                updateSourceArrow(changedLink);
                break;
            case UPDATE_LINKING:
                linkingLine.setVisible(true);
                start = iModel.getSelectedItem().getMiddle();
                end = iModel.getCursorPos();
                linkingLine.setStartX(start.getX());
                linkingLine.setStartY(start.getY());
                linkingLine.setEndX(end.getX());
                linkingLine.setEndY(end.getY());
                break;
            case ADD_NODE:
                projection = rectFromItem(changedItem);
                itemProjections.put(changedItem, projection);
                viewport.getChildren().add(projection);
                break;
            case ADD_LINK:
                changedLink = (SMTransitionLink) changedItem;
                projection = rectFromItem(changedItem);
                itemProjections.put(changedItem, projection);
                viewport.getChildren().add(projection);
                start = changedLink.getSource().getMiddle();
                middle = changedLink.getMiddle();
                end = changedLink.getDrain().getMiddle();
                Line sourceArrow = new Line(
                        start.getX(),
                        start.getY(),
                        middle.getX(),
                        middle.getY()
                );
                Line drainArrow = new Line(
                        middle.getX(),
                        middle.getY(),
                        end.getX(),
                        end.getY()
                );
                sourceArrows.put(changedLink, sourceArrow);
                drainArrows.put(changedLink, drainArrow);
                viewport.getChildren().addAll(sourceArrow, drainArrow);
                linkingLine.setVisible(false);
                break;
            case DELETE:
                projection = itemProjections.get(changedItem);
                itemProjections.remove(changedItem);
                viewport.getChildren().remove(projection);
                break;
        }
        iModel.setLastChange(ModelTransition.NONE);
    }

    private void updateItemRect(SMItem item) {
        Rectangle projection = itemProjections.get(item);
        projection.setTranslateX(item.getMinX());
        projection.setTranslateY(item.getMinY());
    }

    private void updateDrainArrows(Collection<SMTransitionLink> links) {
        for (SMTransitionLink link : links) {
            updateDrainArrow(link);
        }
    }
    private void updateDrainArrow(SMTransitionLink link) {
        Line arrow = drainArrows.get(link);
        Point2D start = link.getMiddle();
        Point2D end = link.getDrain().getMiddle();
        arrow.setStartX(start.getX());
        arrow.setStartY(start.getY());
        arrow.setEndX(end.getX());
        arrow.setEndY(end.getY());
    }

    private void updateSourceArrows(Collection<SMTransitionLink> links) {
        for (SMTransitionLink link : links) {
            updateSourceArrow(link);
        }
    }
    private void updateSourceArrow(SMTransitionLink link) {
        Line arrow = sourceArrows.get(link);
        Point2D start = link.getSource().getMiddle();
        Point2D end = link.getMiddle();
        arrow.setStartX(start.getX());
        arrow.setStartY(start.getY());
        arrow.setEndX(end.getX());
        arrow.setEndY(end.getY());
    }
    
    private static Rectangle rectFromItem(SMItem item) {
        Rectangle r = new Rectangle(0, 0);
        r.setTranslateX(item.getMinX());
        r.setTranslateY(item.getMinY());
        switch (item.type) {
            case NODE:
                r.setWidth(item.getWidth());
                r.setHeight(item.getHeight());
                r.setFill(Color.BLACK);
                r.setStroke(Color.SADDLEBROWN);
                break;
            case LINK:
                r.setWidth(item.getWidth());
                r.setHeight(item.getHeight());
                r.setFill(Color.BEIGE);
                r.setStroke(Color.SADDLEBROWN);
        }
        return r;
    }


    /* for rendering of transition arrows. given a start point, end point, and a rectangle,
     * calculate where that directed line first intercepts the rectangle.
     */
    private static Intercept getFirstIntercept(Point2D start, Point2D end, Rectangle2D rect) {
        double slope = (end.getY() - start.getY()) / (end.getX() - start.getX());
        // a*startX + b = startY
        // b = startY - a*startX
        double yIntercept = start.getY() - slope*start.getX();
        Collection<Intercept> intercepts = getIntercepts(slope, yIntercept, rect);
        switch (intercepts.size()) {
            case 0:
                return null;
            case 1:
                return intercepts.iterator().next();
            case 2:
                if (start.getX() < end.getX())
                    return intercepts.stream()
                            .min((i0, i1) -> (int) (i1.getX() - i0.getX()))
                            .orElse(null);
                else
                    return intercepts.stream()
                            .max((i0, i1) -> (int) (i1.getX() - i0.getX()))
                            .orElse(null);
            default:
                System.out.println("more than 2 intercepts!?");
                System.exit(1);
                return null;
        }
    }
    
    private static Collection<Intercept> getIntercepts(double slope, double yIntercept, Rectangle2D rect) {
        Collection<Intercept> verticalIntercepts = getVerticalIntercepts(slope, yIntercept, rect);
        Collection<Intercept> horizontalIntercepts = getHorizontalIntercepts(slope, yIntercept, rect);
        verticalIntercepts.addAll(horizontalIntercepts);
        return verticalIntercepts;

    }

    private static Collection<Intercept> getHorizontalIntercepts(double slope, double yIntercept, Rectangle2D rect) {
        Collection<Intercept> intercepts = new ArrayList(2);
        intercepts.add(getHorizontalIntercept(slope, yIntercept, rect.getMinY()));
        intercepts.add(getHorizontalIntercept(slope, yIntercept, rect.getMaxY()));
        return intercepts.stream()
                .filter(i -> rect.getMinX() <= i.getX() && i.getX() <= rect.getMaxX())
                .toList();
    }

    private static Intercept getHorizontalIntercept(double slope, double yIntercept, double horiz) {
        // slope * x + yIntercept = horiz
        return new Intercept(
                (horiz - yIntercept) / slope,
                horiz
                );
    }
    // postcondition: intercepts are sorted by min to max X
    private static Collection<Intercept> getVerticalIntercepts(double slope, double yIntercept, Rectangle2D rect) {
        Collection<Intercept> intercepts = new ArrayList(2);
        intercepts.add(getVerticalIntercept(slope, yIntercept, rect.getMinX()));
        intercepts.add(getVerticalIntercept(slope, yIntercept, rect.getMaxX()));
        return intercepts.stream()
                .filter(i -> rect.getMinY() <= i.getY() && i.getY() <= rect.getMaxY())
                .toList();
    }

    private static Intercept getVerticalIntercept(double slope, double yIntercept, double vert) {
        return new Intercept(
                vert,
                slope * vert + yIntercept
        );
    }

    // when intersecting a line with a 2d-axis, there are two intercepts. one is closer. 
    private static class Intercept extends Point2D {
        public Intercept(double v, double v1) {
            super(v, v1);
        }
    };
}
