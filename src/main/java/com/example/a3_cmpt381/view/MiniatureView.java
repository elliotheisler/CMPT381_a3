package com.example.a3_cmpt381.view;

import com.example.a3_cmpt381.AppController;
import com.example.a3_cmpt381.model.SMModel;
import com.example.a3_cmpt381.model.sm_item.SMItem;
import com.example.a3_cmpt381.model.sm_item.SMTransitionLink;
import com.example.a3_cmpt381.view.projections.Arrow;
import com.example.a3_cmpt381.view.projections.ItemProjection;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;

public class MiniatureView extends ProjectionPane {
    Pane parent;
    DoubleProperty scale = new SimpleDoubleProperty();

    public MiniatureView(Pane parent) {
        super();
        getStyleClass().add("MiniatureView");
        this. parent = parent;
        scale.bind(Bindings.min(parent.widthProperty(), parent.heightProperty()));
        setOpacity(0);
        viewport.setOpacity(0);
    }

    public Point2D worldToViewport(Point2D p) {
        return p.multiply(scale());
    }

    public void setController(AppController c) {} // no event handling, view only

    public Rectangle createMainRect() {
        Rectangle mainRect = new Rectangle();
        mainRect.getStyleClass().add("miniature_rect");
        mainRect.toFront();
        mainRect.setFill(Color.BLACK);
        mainRect.setWidth(SMModel.SIZE);
        mainRect.setHeight(SMModel.SIZE);
        mainRect.setOpacity(0.3);
        return mainRect;
    }

    public void updateMainRect() {
        Point2D newCorner = worldToViewport(iModel.getTranslatePos());
        mainRect.setTranslateX(newCorner.getX());
        mainRect.setTranslateY(newCorner.getY());
        mainRect.setScaleX(scale());
        mainRect.setScaleX(scale());
    }

    public double scale() {
        return scale.get();
    }
}
