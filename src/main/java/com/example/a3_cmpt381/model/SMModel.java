package com.example.a3_cmpt381.model;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

import java.util.HashSet;
import java.util.Collection;

public class SMModel extends ModelBase {
    private Collection<SMStateNode> nodes = new HashSet();
    public Collection<SMStateNode> getNodes() {
        return nodes;
    }

    public SMModel() {};

    public SMStateNode getNode(double x, double y) {
        for (SMStateNode node: nodes) {
            if (node.contains(x, y))
                return node;
        }
        return null;
    }

    public SMStateNode tryAddNode(Point2D p) {
        return tryAddNode(p.getX(), p.getY());
    }
    public SMStateNode tryAddNode(double x, double y) {
        SMStateNode candidate = new SMStateNode(x, y);
        if (!anyIntersects(candidate)) {
            nodes.add(candidate);
            notifySubscribers();
            return candidate;
        }
        return null;
    }

    public SMStateNode popNode(SMStateNode node) {
        if (!nodes.contains(node))
            return null;
        nodes.remove(node);
        return node;
    }
    public boolean addNode(SMStateNode node) {
        boolean res = nodes.add(node);
        notifySubscribers();
        return res;
    }

    public boolean anyIntersects(Rectangle2D candidate) {
        for (SMStateNode node : nodes) {
            if (node.intersects(candidate))
                    return true;
        }
        return false;
    }
}
