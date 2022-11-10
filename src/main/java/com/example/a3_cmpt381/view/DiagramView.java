package com.example.a3_cmpt381.view;

import com.example.a3_cmpt381.AppController;
import com.example.a3_cmpt381.model.*;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.*;

public class DiagramView extends StackPane implements ModelListener {
    public static final double WIDTH = 300;
    public static final double HEIGHT = WIDTH * 2 / 3;

    private Pane viewport;

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

    public void modelChanged(Class<?> c) {
        if (c == SMModel.class) {
            redraw();
        } else if (c == InteractionModel.class) {
            redraw();
        }
    }


    public void setController(AppController controller) {
        viewport.setOnMousePressed(e -> controller.mousePressCanvas(e));
        viewport.setOnMouseClicked(e -> controller.mouseClickCanvas(e));
        viewport.setOnMouseReleased(e -> controller.mouseReleaseCanvas(e));
        viewport.setOnMouseDragged(e -> controller.mouseDraggedCanvas(e));
    }


    public DiagramView() {
        getStyleClass().add("DiagramView");
        viewport.getStyleClass().add("viewport");
        getChildren().add(viewport);
    }


    private void redraw() {
    }


    /* for rendering of transition arrows. given a slope and a starting point inside a rectangle,
     * calculate where that line intercepts the rectangular boundary
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
