package com.example.a3_cmpt381.model.sm_item;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Collection;

public class SMStateNode extends SMItem {
    public static final double WIDTH = 80;
    public static final double HEIGHT = 60;

    private Collection<SMTransitionLink>
            incoming = new ArrayList(),
            outgoing = new ArrayList();

    public void removeOutgoing(SMTransitionLink... links) {
        for (SMTransitionLink link : links) {
            outgoing.remove(link);
        }
    }

    public void removeIncoming(SMTransitionLink... links) {
        for (SMTransitionLink link : links) {
            incoming.remove(link);
        }
    }

    public void addOutgoing(SMTransitionLink... links) {
        for (SMTransitionLink link : links) {
            outgoing.add(link);
        }
    }

    public void addIncoming(SMTransitionLink... links) {
        for (SMTransitionLink link : links) {
            incoming.add(link);
        }
    }

    public Collection<SMTransitionLink> getIncoming() {
        return incoming;
    }

    public Collection<SMTransitionLink> getOutgoing() {
        return outgoing;
    }

    private String name = "";
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public SMStateNode(double x, double y) {
        super(x, y, WIDTH, HEIGHT);
//        TYPE = SMItemType.NODE;
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
        TYPE = SMItemType.NODE;
    }
}
