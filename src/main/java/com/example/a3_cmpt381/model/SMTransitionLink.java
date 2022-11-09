package com.example.a3_cmpt381.model;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

public class SMTransitionLink extends SMItem {
    public static final double WIDTH = 40;
    public static final double HEIGHT = 40;
    public static final double RADIUS = 4; // for rendering only


    private SMStateNode source;
    public SMStateNode getSource() {
        return source;
    }

    private SMStateNode drain;
    public SMStateNode getDrain() {
        return drain;
    }

    public SMTransitionLink(double x, double y, SMStateNode source, SMStateNode drain) {
        super(x, y, WIDTH, HEIGHT);
        this.source = source;
        this.drain = drain;
    }

    public static SMTransitionLink fromSourceDrain(SMStateNode source, SMStateNode drain) {
        Point2D p = SMItem.getPosBetween(source, drain);
        return new SMTransitionLink(p.getX(), p.getY(), source, drain);
    }

//    public boolean equals(SMTransitionLink other) {
//        return this.source == other.source &&
//                this.drain == other.drain;
//    }
}
