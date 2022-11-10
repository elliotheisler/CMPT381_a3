package com.example.a3_cmpt381.model.sm_item;


import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

public abstract class SMItem extends CustomRectangle {
    public static final double WIDTH = 0;
    public static final double HEIGHT = 0;
    // like tagged-unions, but (a bit) safer
    // hacky way to make SMItem a sum type of nodes and links
    public SMItemType type;
    protected abstract void setType();

    public SMItem(double v, double v1, double v2, double v3) {
        super(v, v1, v2, v3);
        setType();
    }

    public static Point2D middleToCorner(Point2D middle) {
        return middle.subtract(new Point2D(WIDTH / 2, HEIGHT / 2));
    }
}
