package com.example.a3_cmpt381.view;

import com.example.a3_cmpt381.model.sm_item.CustomRectangle;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import static java.lang.Math.*;

final class InterceptCalc {
    private static class Intercept extends Point2D {
        public Intercept(double x, double y) {
            super(x, y);
        }
    };

    private InterceptCalc() {};

    // NOTE: I realize now I could have used Shape.subtract() to generate the intersected lines, but this
    // was an interesting challenge.


    /* for rendering of transition arrows. given a start point, end point, and a rectangle,
     * calculate where that directed line first intercepts the rectangle.
     */
    protected static Intercept getFirstIntercept(Point2D start, Point2D end, CustomRectangle rect) {
        double slope = (end.getY() - start.getY()) / (end.getX() - start.getX());
        // a*startX + b = startY
        // b = startY - a*startX
        double yIntercept = start.getY() - slope*start.getX();
        return getIntercepts(slope, yIntercept, rect)
                // filter down to (at most 2) intercepts in front of the arrow's start...
                .filter( i -> start.getX() < end.getX() == start.getX() < i.getX() )
                // ... and pick the nearest one
                .min( (i1, i2) -> (int)
                        awayFromZero((start.getX() < end.getX()
                                ? i1.getX() - i2.getX()
                                : i2.getX() - i1.getX())))
                .orElse(null);
    }

    private static int awayFromZero(double a) {
        return (int) (a > 0 ? ceil(a) : floor(a));
    }

    private static Stream<Intercept> getIntercepts(double slope, double yIntercept, CustomRectangle rect) {
        Stream<Intercept> verticalIntercepts = getVerticalIntercepts(slope, yIntercept, rect);
        Stream<Intercept> horizontalIntercepts = getHorizontalIntercepts(slope, yIntercept, rect);
        return Stream.concat(verticalIntercepts, horizontalIntercepts);

    }
    // postcondition: intercepts are sorted by min to max Y
    private static Stream<Intercept> getHorizontalIntercepts(double slope, double yIntercept, CustomRectangle rect) {
        Collection<Intercept> intercepts = new ArrayList(2);
        intercepts.add(getHorizontalIntercept(slope, yIntercept, rect.getMinY()));
        intercepts.add(getHorizontalIntercept(slope, yIntercept, rect.getMaxY()));
        return intercepts.stream()
                .filter(i -> rect.getMinX() <= i.getX() && i.getX() <= rect.getMaxX());
    }

    private static Intercept getHorizontalIntercept(double slope, double yIntercept, double horiz) {
        slope = slope == 0 ? 0.000001 : slope;
        // slope * x + yIntercept = horiz
        return new Intercept(
                (horiz - yIntercept) / slope,
                horiz
        );
    }
    // postcondition: intercepts are sorted by min to max X
    private static Stream<Intercept> getVerticalIntercepts(double slope, double yIntercept, CustomRectangle rect) {
        Collection<Intercept> intercepts = new ArrayList(2);
        intercepts.add(getVerticalIntercept(slope, yIntercept, rect.getMinX()));
        intercepts.add(getVerticalIntercept(slope, yIntercept, rect.getMaxX()));
        return intercepts.stream()
                .filter(i -> rect.getMinY() <= i.getY() && i.getY() <= rect.getMaxY());
    }

    private static Intercept getVerticalIntercept(double slope, double yIntercept, double vert) {
        slope = slope == 0 ? 0.000001 : slope;
        return new Intercept(
                vert,
                slope * vert + yIntercept
        );
    }
}
