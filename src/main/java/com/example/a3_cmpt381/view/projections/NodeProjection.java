package com.example.a3_cmpt381.view.projections;

import com.example.a3_cmpt381.model.sm_item.SMStateNode;
import javafx.geometry.Point2D;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class NodeProjection extends ItemProjection {

    public NodeProjection() {
        super();
    }

    public Rectangle createRect() {
        Rectangle r = new Rectangle(0, 0, SMStateNode.WIDTH, SMStateNode.HEIGHT);
        r.setFill(Color.WHITE);
        r.setStroke(Color.BLACK);
        return r;
    }

    public void onDeselect() {
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(1);
    }
}
