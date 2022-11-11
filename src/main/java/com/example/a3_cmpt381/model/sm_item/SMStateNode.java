package com.example.a3_cmpt381.model.sm_item;

import javafx.geometry.Point2D;

public class SMStateNode extends SMItem {
    public static final double WIDTH = 80;
    public static final double HEIGHT = 60;

    public SMStateNode(double x, double y) {
        super(x, y, WIDTH, HEIGHT);
//        type = SMItemType.NODE;
    }
    public SMStateNode(Point2D point) {
        this(point.getX(), point.getY());
    }


    public Point2D getMiddle() {
        return new Point2D(
                getMinX() + getWidth() / 2,
                getMinY() + getHeight() / 2
        );
    }

    protected void setType() {
        type = SMItemType.NODE;
    }
}
