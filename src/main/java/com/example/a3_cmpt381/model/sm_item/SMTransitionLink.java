package com.example.a3_cmpt381.model.sm_item;

import javafx.geometry.Point2D;

public class SMTransitionLink extends SMItem {
    public static final double WIDTH = SMStateNode.WIDTH * 1.5;
    public static final double HEIGHT = SMStateNode.WIDTH * 2;

    private String
            event = "",
            context = "",
            sideEffect = "";

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getSideEffect() {
        return sideEffect;
    }

    public void setSideEffect(String sideEffect) {
        this.sideEffect = sideEffect;
    }


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
//        type = SMItemType.LINK;
    }

    public static SMTransitionLink fromSourceDrain(SMStateNode source, SMStateNode drain) {
        Point2D p = getPosBetween(source, drain);
        return new SMTransitionLink(p.getX(), p.getY(), source, drain);
    }

    /* used to create transition links.
     * get the corner of a rectangle of this type when it is positioned between the midpoints
     * of the source and drain
     */
    private static Point2D getPosBetween(SMItem source, SMItem drain) {
        return source.getMidpoint(drain).subtract(new Point2D(
                WIDTH / 2,
                HEIGHT / 2
        ));
    }

    protected void setType() {
        type = SMItemType.LINK;
    }
}
