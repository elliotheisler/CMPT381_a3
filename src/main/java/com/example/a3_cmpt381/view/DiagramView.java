package com.example.a3_cmpt381.view;

import com.example.a3_cmpt381.AppController;
import com.example.a3_cmpt381.model.SMModel;
import com.example.a3_cmpt381.model.interaction_model.InteractionModel;
import com.example.a3_cmpt381.model.sm_item.SMItem;
import com.example.a3_cmpt381.model.sm_item.SMStateNode;
import com.example.a3_cmpt381.model.sm_item.SMTransitionLink;
import com.example.a3_cmpt381.view.projections.Arrow;
import com.example.a3_cmpt381.view.projections.ItemProjection;
import com.example.a3_cmpt381.view.projections.LinkProjection;
import com.example.a3_cmpt381.view.projections.NodeProjection;
import javafx.geometry.Point2D;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DiagramView extends ProjectionPane {
    public static final double WIDTH = 800;
    public static final double HEIGHT = WIDTH;

    private MiniatureView miniView = new MiniatureView(this);

    public DiagramView() {
        super();
        getStyleClass().add("DiagramView");
        viewport.getStyleClass().add("viewport");
        getChildren().add(miniView);
        miniView.toFront();
    }

    public void setSMModel(SMModel smModel) {
        this.smModel = smModel;
        miniView.smModel = smModel;
    }

    public void setIModel(InteractionModel iModel) {
        this.iModel = iModel;
        miniView.iModel = iModel;
    }

    public Rectangle createMainRect() {
        Rectangle mainRect = new Rectangle();
        mainRect.getStyleClass().add("world");
        mainRect.toFront();
        mainRect.setFill(Color.WHITESMOKE);
        mainRect.setWidth(SMModel.SIZE);
        mainRect.setHeight(SMModel.SIZE);
        return mainRect;
    }

    public void updateMainRect() {
        Point2D newCorner = worldToViewport(new Point2D(0, 0));
        mainRect.setTranslateX(newCorner.getX());
        mainRect.setTranslateY(newCorner.getY());
    }

    public Point2D worldToViewport(Point2D p) {
        return iModel.worldToViewport(p);
    }

    public double scale() {
        return 1D;
    }
}
