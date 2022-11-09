package com.example.a3_cmpt381.model;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

import static java.lang.Double.max;
import static java.lang.Math.pow;

public class InteractionModel extends ModelBase {
    // (square of) radius of mouse movement required before drag initiated
    public static final double DRAG_THRESHOLD = 100;

    // need to store seperate from selectedNode in order to mutate it, since Rectangle2D is immutable
    private double x, y,
            initX, initY, // drag starting position
            maxDX, maxDY; // maximum distance from initial position attained
    public double getX() {
        return selectedNode.getMinX() + x - initX;
    }
    public double getY() {
        return selectedNode.getMinY() + y - initY;
    }

    public void setSelectedPos(double x, double y) {
        this.x = x;
        this.y = y;
        maxDX = max(x - initX, maxDX);
        maxDY = max(y - initY, maxDY);
        notifySubscribers();
    }

    private SMStateNode selectedNode;

    public SMStateNode getOldSelectedNode() {
        return selectedNode;
    }

    public SMStateNode getNewSelectedNode() {
        if (selectedNode != null && maxDX*maxDX + maxDY*maxDY > DRAG_THRESHOLD) {
            return new SMStateNode(getX(), getY());
        }
        return selectedNode;
    }

    public void setSelectedNode(SMStateNode selectedNode, double x, double y) {
        this.selectedNode = selectedNode;
        if (selectedNode == null) {
            notifySubscribers();
        } else {
            initX = x;
            initY = y;
            setSelectedPos(x, y);
        }
    }

    private CursorMode cursorMode = CursorMode.DRAG;

    public CursorMode getCursorMode() {
        return cursorMode;
    }

    public void setCursorMode(CursorMode c) {
        cursorMode = c;
        notifySubscribers();
    }

    private InteractionState iState = InteractionState.READY;

    public InteractionState getInteractionState() {
        return iState;
    }

    public void setInteractionState(InteractionState iState) {
        this.iState = iState;
    }
}
