package com.example.a3_cmpt381.model;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

public abstract class SMItem extends Rectangle2D {
    public static final double WIDTH = 0;
    public static final double HEIGHT = 0;
    public SMItem(double v, double v1, double v2, double v3) {
        super(v, v1, v2, v3);
    }

    /* used to create transition links.
     * get the corner of a rectangle of this type when it is positioned between the midpoints
     * of the source and drain
     */
    public static Point2D getPosBetween(SMItem source, SMItem drain) {
        return source.getMidpoint(drain).subtract(new Point2D(
                WIDTH / 2,
                HEIGHT / 2
        ));
    }

    private Point2D getMidpoint(SMItem drain) {
        return getMiddle().midpoint(drain.getMiddle());
    }

    public Point2D getMiddle() {
        return new Point2D(
                getMinX() + WIDTH / 2,
                getMinY() + HEIGHT / 2
        );
    }
}
