package com.example.a3_cmpt381.model.sm_item;


import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

public abstract class SMItem extends CustomRectangle {
    public static final double WIDTH = 0;
    public static final double HEIGHT = 0;
    // like tagged-unions, but (a bit) safer
    // hacky way to make SMItem a sum TYPE of nodes and links
    public SMItemType TYPE;
    protected abstract void setType();

    public SMItem(double v, double v1, double v2, double v3) {
        super(v, v1, v2, v3);
        setType();
    }

    public Point2D middleToCorner(Point2D middle) {
        return middle.subtract(new Point2D(getWidth() / 2, getHeight() / 2));
    }
}
