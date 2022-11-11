package com.example.a3_cmpt381.view.projections;

import com.example.a3_cmpt381.model.sm_item.SMStateNode;
import com.example.a3_cmpt381.model.sm_item.SMTransitionLink;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class NodeProjection extends ItemProjection {
    private static final double RADIUS = 5;

    private Label name;

    public NodeProjection() {
        super();
        VBox textBox = new VBox();
        textBox.setAlignment(Pos.CENTER);
        textBox.setMaxSize(
                SMStateNode.WIDTH - RADIUS - 2,
                SMStateNode.HEIGHT - RADIUS - 2
        );
        name = createBody(textBox);
        setName(null);

        textBox.getChildren().add(name);
        getChildren().add(textBox);
    }

    public Rectangle createRect() {
        Rectangle r = new Rectangle(0, 0, SMStateNode.WIDTH, SMStateNode.HEIGHT);
        r.setFill(Color.WHITE);
        r.setStroke(Color.BLACK);
        return r;
    }

    public void setName(String s) {
        name.setText(s);
//        setText(name, s, "Default");
    }

    public void updateText(SMStateNode node) {
        setName(node.getName());
    }

    public void onDeselect() {
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(1);
    }

    protected static Label createBody(VBox vBox) {
        Label l = ItemProjection.createBody(vBox);
        l.getStyleClass().add("node_body");
        return l;
    }
}
