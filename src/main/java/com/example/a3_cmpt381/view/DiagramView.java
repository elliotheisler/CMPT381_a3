package com.example.a3_cmpt381.view;

import com.example.a3_cmpt381.AppController;
import com.example.a3_cmpt381.model.*;
import com.example.a3_cmpt381.model.sm_item.CustomRectangle;
import com.example.a3_cmpt381.model.sm_item.SMItem;
import com.example.a3_cmpt381.model.sm_item.SMStateNode;
import com.example.a3_cmpt381.model.sm_item.SMTransitionLink;
import javafx.geometry.Point2D;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.*;
import java.util.stream.Stream;

public class DiagramView extends StackPane implements ModelListener {
    public static final double WIDTH = 800;
    public static final double HEIGHT = WIDTH;
    
    private Pane viewport = new Pane();
    
    private Map<SMItem, Rectangle> itemProjections = new HashMap();
    private Map<SMTransitionLink, Arrow> sourceArrows = new HashMap();
    private Map<SMTransitionLink, Arrow> drainArrows = new HashMap();
    private Arrow linkingArrow = new Arrow();
    
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
        HBox.setHgrow(this, Priority.ALWAYS);
        viewport.getChildren().addAll(linkingArrow.getShapes());
        linkingArrow.setVisible(false);
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
                break;
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
                linkingArrow.setVisible(true);
                start = iModel.getSelectedItem().getMiddle();
                end = iModel.getCursorPos();
                linkingArrow.setStartPos(start);
                linkingArrow.setEndPos(end);
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

                Arrow sourceArrow = new Arrow();
                Arrow drainArrow = new Arrow();
                sourceArrows.put(changedLink, sourceArrow);
                drainArrows.put(changedLink, drainArrow);
                updateSourceArrow(changedLink);
                updateDrainArrow(changedLink);
                viewport.getChildren().addAll(sourceArrow.getShapes());
                viewport.getChildren().addAll(drainArrow.getShapes());
                linkingArrow.setVisible(false);
                break;
            case DELETE_NODE:
                changedNode = (SMStateNode) changedItem;
                // delete view of this node
                projection = itemProjections.get(changedItem);
                itemProjections.remove(changedItem);
                viewport.getChildren().remove(projection);
                // delete views of links to this node
                deleteLinks(smModel.getIncomingLinks(changedNode));
                deleteLinks(smModel.getOutgoingLinks(changedNode));
                break;
        }
        iModel.setLastChange(ModelTransition.NONE);
    }
    
    private void updateLinkArrows(SMTransitionLink changedLink) {
        Point2D start, middle, end;
        start = changedLink.getSource().getMiddle();
        middle = changedLink.getMiddle();
        end = changedLink.getDrain().getMiddle();

        Point2D start1, end1, start2, end2;
        start1 = InterceptCalc.getFirstIntercept(start, middle, changedLink.getSource());
        end1   = InterceptCalc.getFirstIntercept(start, middle, changedLink);
        start2 = InterceptCalc.getFirstIntercept(middle, end, changedLink);
        end2   = InterceptCalc.getFirstIntercept(middle, end, changedLink.getDrain());
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
        Point2D middle, end;
        middle = link.getMiddle();
        end = link.getDrain().getMiddle();

        Point2D lineStart, lineEnd;
        lineStart = InterceptCalc.getFirstIntercept(middle, end, link);
        lineEnd   = InterceptCalc.getFirstIntercept(middle, end, link.getDrain());
        Arrow arrow = drainArrows.get(link);
        arrow.setStartPos(lineStart);
        arrow.setEndPos(lineEnd);
    }

    private void deleteLinks(Collection<SMTransitionLink> links) {
        for (SMTransitionLink link : links) {
            deleteLink(link);
        }
    }
    private void deleteLink(SMTransitionLink link) {
        viewport.getChildren().removeAll(sourceArrows.get(link).getShapes());
        viewport.getChildren().removeAll(drainArrows.get(link).getShapes());
        viewport.getChildren().removeAll(itemProjections.get(link));
        sourceArrows.remove(link);
        drainArrows.remove(link);
        itemProjections.remove(link);
    }

    private void updateSourceArrows(Collection<SMTransitionLink> links) {
        for (SMTransitionLink link : links) {
            updateSourceArrow(link);
        }
    }
    private void updateSourceArrow(SMTransitionLink link) {
        Point2D start, middle;
        start = link.getSource().getMiddle();
        middle = link.getMiddle();

        Point2D lineStart, lineEnd;
        lineStart = InterceptCalc.getFirstIntercept(start, middle, link.getSource());
        lineEnd   = InterceptCalc.getFirstIntercept(start, middle, link);
        Arrow arrow = sourceArrows.get(link);
        arrow.setStartPos(lineStart);
        arrow.setEndPos(lineEnd);
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
}
