package com.example.a3_cmpt381.view.projections;

import com.example.a3_cmpt381.model.sm_item.SMItem;
import com.example.a3_cmpt381.model.sm_item.SMTransitionLink;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class ItemProjection extends StackPane {
    public ItemProjection() {
        getChildren().add(rect);
    }
    protected Rectangle rect = createRect();

    public void setPos(Point2D p) {
        relocate(p.getX(), p.getY());
//        setTranslateX(p.getX());
//        setTranslateY(p.getY());
    }
    public abstract Rectangle createRect();


    public void onSelect() {
        rect.setStroke(Color.RED);
        rect.setStrokeWidth(2);
    }

    public abstract void onDeselect();

    protected static void setText(Label t, String s, String defaultStr) {
        if (s == null || s.equals("") || s.isBlank()) {
            t.setText(defaultStr);
        } else {
            t.setText(s);
        }
    }

    protected static Label createBody(VBox vBox) {
        Label t = new Label();
        t.maxWidthProperty().bind(vBox.maxWidthProperty());
        t.maxHeightProperty().bind(vBox.maxHeightProperty());
        t.setWrapText(true);
        return t;
    }
}
