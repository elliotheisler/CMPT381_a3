package com.example.a3_cmpt381.model;

import javafx.geometry.Point2D;

public class SMTransitionLink extends SMItem {
    public static final double WIDTH = 40;
    public static final double HEIGHT = 40;
    private SMStateNode source;
    private SMStateNode drain;

    public SMTransitionLink(double x, double y, SMStateNode source, SMStateNode drain) {
        super(0, 0, WIDTH, HEIGHT);
        this.source = source;
        this.drain = drain;
    }

    public static SMTransitionLink fromEndpoints(SMStateNode source, SMStateNode drain) {
        Point2D p = SMItem.getPosBetween(source, drain);
        return new SMTransitionLink(p.getX(), p.getY(), source, drain);
    }
}
