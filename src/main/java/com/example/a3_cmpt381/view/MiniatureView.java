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
import javafx.beans.property.SimpleObjectProperty;
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

import static java.lang.Math.min;

public class MiniatureView extends ProjectionPane {
    Pane parent;
//    DoubleProperty scale = new SimpleDoubleProperty();
    double scale;

    public MiniatureView(Pane parent) {
        super();
        getStyleClass().add("MiniatureView");
        this. parent = parent;
//        scale.bind(Bindings.min(parent.widthProperty(), parent.heightProperty()));
    }

    public Point2D worldToViewport(Point2D p) {
        return p.multiply(scale());
    }

    public final void setController(AppController c) {} // no event dispatching, view only

    public Rectangle createMainRect() {
        Rectangle mainRect = new Rectangle();
        mainRect.getStyleClass().add("miniature_rect");
        mainRect.setFill(Color.BLACK);
        mainRect.setWidth(SMModel.SIZE);
        mainRect.setHeight(SMModel.SIZE);
        mainRect.setOpacity(0.1);
        return mainRect;
    }

    public void updateMainRect() {
        Point2D newCorner = worldToViewport(iModel.getTranslatePos());
        mainRect.setTranslateX(newCorner.getX());
        mainRect.setTranslateY(newCorner.getY());
        mainRect.setScaleX(scale());
        mainRect.setScaleY(scale());
    }

    public double scale() {
//        return scale.get();
        double s = min(parent.widthProperty().get(), parent.heightProperty().get()) / SMModel.SIZE;
        System.out.println("scale: " + s);
        return s;
    }
}
