package com.example.a3_cmpt381.model;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SMStateNode extends SMItem {
    public static final double WIDTH = 40;
    public static final double HEIGHT = 30;


    public SMStateNode(double x, double y) {
        super(x, y, WIDTH, HEIGHT);
    }
    public SMStateNode(Point2D point) {
        this(point.getX(), point.getY());
    }

}
