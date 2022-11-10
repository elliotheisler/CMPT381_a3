package com.example.a3_cmpt381.model.sm_item;

import javafx.geometry.Point2D;

// the javafx Rectangle2D and Rectangle classes don't quite do what i want
// - Rectangle2D is immutable
// - Rectangle doesn't have all the convenience methods. and it is a scene object.
public class CustomRectangle {
    private double minX, minY, width, height;

    public CustomRectangle(double minX, double minY, double width, double height) {
        this.minX = minX;
        this.minY = minY;
        this.width = width;
        this.height = height;
    }

    public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getMaxX() {
        return minX + width;
    }

    public double getMaxY() {
        return minY + height;
    }

    public boolean intersects(CustomRectangle other) {
        return containsOneOf(
                other.getMin(),
                other.getMax(),
                other.getBottomLeft(),
                other.getTopRight()
        );
    }

    public boolean containsOneOf(Point2D... points) {
        for (Point2D p : points) {
            if (contains(p))
                return true;
        }
        return false;
    }

    public boolean contains(Point2D p) {
        return contains(p.getX(), p.getY());
    }
    public boolean contains(double x, double y) {
        return minX <= x && x <= getMaxX() &&
                minY <= y && y <= getMaxY();
    }

    // average of middles
    public Point2D getMidpoint(CustomRectangle end) {
        return getMiddle().midpoint(end.getMiddle());
    }

    public Point2D getMiddle() {
        return new Point2D(
                getMinX() + getWidth() / 2,
                getMinY() + getHeight() / 2
        );
    }

    public void setMin(Point2D p) {
        this.minX = p.getX();
        this.minY = p.getY();
    }

    public Point2D getMin() {
        return new Point2D(getMinX(), getMinY());
    }

    public Point2D getMax() {
        return new Point2D(getMaxX(), getMaxY());
    }

    public Point2D getTopRight() {
        return new Point2D(getMaxX(), getMinY());
    }

    public Point2D getBottomLeft() {
        return new Point2D(getMinX(), getMaxY());
    }
}
