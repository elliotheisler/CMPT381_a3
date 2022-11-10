package com.example.a3_cmpt381.model;

import com.example.a3_cmpt381.model.sm_item.CustomRectangle;
import com.example.a3_cmpt381.model.sm_item.SMItem;
import com.example.a3_cmpt381.model.sm_item.SMStateNode;
import com.example.a3_cmpt381.model.sm_item.SMTransitionLink;
import javafx.geometry.Point2D;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Collection;

public class SMModel extends ModelBase {
    private Collection<SMStateNode> nodes = new HashSet();
    public Collection<SMStateNode> getNodes() {
        return nodes;
    }
    private Collection<SMTransitionLink> links = new LinkedList();
    public Collection<SMTransitionLink> getLinks() {
        return links;
    }

    public void addLink(SMTransitionLink link) {
        links.add(link);
        notifySubscribers();
    }

    public SMModel() {};

    public SMItem getItem(Point2D p) {
        for (SMStateNode node : nodes) {
            if (node.contains(p))
                return node;
        }
        for (SMTransitionLink link : links) {
            if (link.contains(p))
                return link;
        }
        return null;
    }

    public boolean replaceNode(SMStateNode oldNode, SMStateNode newNode) {
        if (!nodes.contains(oldNode))
            return false;
        nodes.remove(oldNode);
        return nodes.add(newNode);
    }
    public void addNode(SMStateNode node) {
        nodes.add(node);
        notifySubscribers();
    }

    public boolean anyIntersects(CustomRectangle candidate) {
        for (SMStateNode node : nodes) {
            if (node.intersects(candidate))
                return true;
        }
        for (SMTransitionLink link : links) {
            if (link.intersects(candidate))
                return true;
        }
        return false;
    }

    public SMItem pop(SMItem target) {
        Class<?> c = target.getClass();
        if (c == SMStateNode.class) {
            nodes.remove(target);
            return target;
        } else if (c == SMTransitionLink.class) {
            System.out.println("cant select transition links yet");
            System.exit(1);
        } else {
            System.out.println("???");
            System.exit(1);
        }
        return null;
    }

    public void push(SMItem target) {
        Class<?> c = target.getClass();
        if (c == SMStateNode.class) {
            nodes.add((SMStateNode) target);
        } else if (c == SMTransitionLink.class) {
            System.out.println("cant push transition links yet");
        } else {
            System.out.println("???");
        }
    }

    public boolean contains(SMItem i) {
        return nodes.contains(i) || links.contains(i);
    }
}
