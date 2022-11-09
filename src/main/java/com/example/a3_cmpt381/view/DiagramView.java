package com.example.a3_cmpt381.view;

import com.example.a3_cmpt381.AppController;
import com.example.a3_cmpt381.model.*;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Collection;

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

    public void modelChanged(Class<?> c) {
        if (c == SMModel.class) {
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
        gc.setFill(Color.BEIGE);
        gc.setStroke(Color.BLACK);
        for (SMStateNode node: smModel.getNodes()) {
            node.draw(gc);
        }
    }

    private void fillBackground(Color c) {
        gc.setFill(c);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

}
