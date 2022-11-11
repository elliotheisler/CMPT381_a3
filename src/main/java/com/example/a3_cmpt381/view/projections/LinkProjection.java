package com.example.a3_cmpt381.view.projections;

import com.example.a3_cmpt381.model.sm_item.SMItem;
import com.example.a3_cmpt381.model.sm_item.SMTransitionLink;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.TextBoundsType;

public class LinkProjection extends ItemProjection {
    private static final double RADIUS = 5;
    private Label event, context, sideEffect;

    private static final String
            eventHeaderStr = "Event:",
            contextHeaderStr = "Context:",
            sideEffectHeaderStr = "Side Effect:";

    private final Label
            eventHeader = createHeader(eventHeaderStr),
            contextHeader = createHeader(contextHeaderStr),
            sideEffectHeader = createHeader(sideEffectHeaderStr);

    public LinkProjection() {
        super();
        VBox textBox = new VBox();
        textBox.setMaxSize(
                SMTransitionLink.WIDTH - RADIUS - 2,
                SMTransitionLink.HEIGHT - RADIUS - 2
        );
        event = createBody(textBox);
        context = createBody(textBox);
        sideEffect = createBody(textBox);

        setEvent(null);
        setContext(null);
        setSideEffect(null);

        textBox.getChildren().addAll(
                eventHeader, event,
                contextHeader, context,
                sideEffectHeader, sideEffect
        );
        textBox.getStyleClass().add("link_textbox");
        getChildren().add(textBox);
    }

    public void setEvent(String event) {
        setText(this.event, event, "No Event");
    }

    public void setContext(String context) {
        setText(this.context, context, "No Context");
    }

    public void setSideEffect(String sideEffect) {
        setText(this.sideEffect, sideEffect, "No Side Effect");
    }

    public Rectangle createRect() {
        Rectangle rect = new Rectangle(0, 0, SMTransitionLink.WIDTH, SMTransitionLink.HEIGHT);
        rect.setFill(Color.BEIGE);
        rect.setStroke(Color.BLACK);
        rect.setArcWidth(RADIUS);
        rect.setArcHeight(RADIUS);
        return rect;
    }
    
    public static Label createHeader(String s) {
        Label t = new Label(s == null ? "" : s);
        t.getStyleClass().add("link_header");
        t.setMaxWidth(SMTransitionLink.WIDTH - RADIUS - 2);
        t.setMaxHeight(SMTransitionLink.HEIGHT - RADIUS - 2);
        return t;
    }

    public void updateText(SMTransitionLink link) {
        setEvent(link.getEvent());
        setContext(link.getContext());
        setSideEffect(link.getSideEffect());
    }

    public void onDeselect() {
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(1);
    }

    protected static Label createBody(VBox vBox) {
        Label l = ItemProjection.createBody(vBox);
        l.getStyleClass().add("link_body");
        return l;
    }

}
