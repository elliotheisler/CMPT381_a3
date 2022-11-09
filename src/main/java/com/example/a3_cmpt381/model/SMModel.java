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

    public SMStateNode getNode(Point2D canvasPoint) {
        for (SMStateNode node: nodes) {
            if (node.contains(canvasPoint))
                return node;
        }
        return null;
    }

    public SMStateNode tryAddNode(double x, double y) {
        SMStateNode candidate = new SMStateNode(
                x - SMStateNode.WIDTH / 2,
                y - SMStateNode.HEIGHT / 2
                );
        if (!anyIntersects(candidate)) {
            nodes.add(candidate);
            notifySubscribers();
            return candidate;
        }
        return null;
    }

    private boolean anyIntersects(Rectangle2D candidate) {
        for (SMStateNode node : nodes) {
            if (node.intersects(candidate))
                    return true;
        }
        return false;
    }
}
