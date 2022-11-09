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

    public boolean replaceNode(SMStateNode oldNode, SMStateNode newNode) {
        if (!nodes.contains(oldNode))
            return false;
        nodes.remove(oldNode);
        return nodes.add(newNode);
    }
    public boolean addNode(SMStateNode node) {
        boolean res = nodes.add(node);
        notifySubscribers();
        return res;
    }

//    public boolean anyIntersects(Rectangle2D candidate) {
//        for (SMStateNode node : nodes) {
//            if (node.intersects(candidate))
//                    return true;
//        }
//        return false;
//    }

    public SMStateNode anyIntersects(Rectangle2D candidate) {
        for (SMStateNode node : nodes) {
            if (node.intersects(candidate))
                return node;
        }
        return null;
    }
}
