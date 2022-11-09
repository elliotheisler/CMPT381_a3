package com.example.a3_cmpt381.view;

import com.example.a3_cmpt381.AppController;
import com.example.a3_cmpt381.model.*;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Collection;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class DiagramView extends StackPane implements ModelListener {
    public static final double WIDTH = 300;
    public static final double HEIGHT = WIDTH * 2 / 3;

    public static final Color BACKGROUND_COLOR = Color.valueOf("#E0F0FF");
    private Canvas canvas = new Canvas(WIDTH, HEIGHT);
    private GraphicsContext gc = canvas.getGraphicsContext2D();

    private SMModel smModel;
    public void setSMModel(SMModel smModel) {
        this.smModel = smModel;
    }

    private InteractionModel iModel;
    public void setIModel(InteractionModel iModel) {
        this.iModel = iModel;
    }

    public void modelChanged(Class<?> c) {
        if (c == SMModel.class) {
            redraw();
        } else if (c == InteractionModel.class) {
            redraw();
        }
    }

    public void setController(AppController controller) {
        canvas.setOnMousePressed(e -> controller.mousePressCanvas(e));
        canvas.setOnMouseClicked(e -> controller.mouseClickCanvas(e));
        canvas.setOnMouseReleased(e -> controller.mouseReleaseCanvas(e));
        canvas.setOnMouseDragged(e -> controller.mouseDraggedCanvas(e));
    }
    public DiagramView() {
        getStyleClass().add("DiagramView");
        canvas.getStyleClass().add("canvas");
        getChildren().add(canvas);
        fillBackground(BACKGROUND_COLOR);
    }

    private void redraw() {
        fillBackground(BACKGROUND_COLOR);
        for (SMStateNode node: smModel.getNodes()) {
            if (node == iModel.getOldSelectedNode())
                drawSelectedNode();
            else
                drawNode(node);
        }
    }

    public void drawNode(SMStateNode node) {
        gc.setFill(Color.BEIGE);
        gc.setStroke(Color.BLACK);
        gc.fillRect(
                node.getMinX(),
                node.getMinY(),
                node.getWidth(),
                node.getHeight()
        );
        gc.strokeText("default", node.getMinX(), node.getMaxY(), getWidth());
    }

    public void drawSelectedNode() {
        SMStateNode selectedNode = iModel.getNewSelectedNode();
        if (selectedNode == null) {
            return;
        }
        gc.setFill(Color.BEIGE);
        gc.setStroke(Color.RED);
        double x = selectedNode.getMinX();
        double y = selectedNode.getMinY();
        gc.fillRect(
                x,
                y,
                selectedNode.getWidth(),
                selectedNode.getHeight()
        );
        gc.strokeRect(
                x,
                y,
                selectedNode.getWidth(),
                selectedNode.getHeight()
        );
        gc.setStroke(Color.BLACK);
        gc.strokeText("selected", x, y + selectedNode.getHeight(), selectedNode.getWidth());
    }

    public void drawLink(SMTransitionLink link) {

    }

    private void fillBackground(Color c) {
        gc.setFill(c);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /* for rendering of transition arrows. given a slope and a starting point inside a rectangle,
     * calculate where that line intercepts the rectangular boundary
     */
    
    private static Intercepts getIntercepts(double startX, double startY, double slope, Rectangle2D rect) {
        double nearX = abs(rect.getMinX() - startX) < abs(rect.getMaxX() - startX)
                ? rect.getMinX()
                : rect.getMaxX();
        double nearY = abs(rect.getMinY() - startY) < abs(rect.getMaxY() - startY)
                ? rect.getMinY()
                : rect.getMaxY();
        Intercepts res = return getIntercepts(startX, startY, slope, nearX, nearY);
        return rect.contains(startX, startY)
                ? res.near
    }

    private static Intercepts getIntercepts(double startX, double startY, double slope, double vert, double horiz) {
        // startX * slope + yIntercept = startY
        double yIntercept = startY - (startX * slope);
        Point2D vertIntercept = getVerticalIntercept(slope, yIntercept, vert);
        Point2D horizIntercept = getHorizontalIntercept(slope, yIntercept, horiz);
        // return the nearest intercept
        // TODO potential optimization: only need to compare distance in 1 dimension to infer which is closer
        return vertIntercept.distance(startX, startY) < horizIntercept.distance(startX, startY)
                ? new Intercepts(vertIntercept, horizIntercept)
                : new Intercepts(horizIntercept, vertIntercept);
    }

    private static Point2D getHorizontalIntercept(double slope, double yIntercept, double horiz) {
        // slope * x + yIntercept = horiz
        return new Point2D(
                (horiz - yIntercept) / slope,
                horiz
                );
    }

    private static Point2D getVerticalIntercept(double slope, double yIntercept, double vert) {
        return new Point2D(
                vert,
                slope * vert + yIntercept
        );
    }

    // when intersecting a line with a 2d-axis, there are two intercepts. one is closer. 
    private record Intercepts(Point2D near, Point2D far) {};
}
