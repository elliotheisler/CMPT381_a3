package com.example.a3_cmpt381.model;

import static java.lang.Double.max;

public class InteractionModel extends ModelBase {
    // (square of) radius of mouse movement required before drag initiated
    public static final double DRAG_THRESHOLD = 100;

    // cursor drag state
    private double x, y, // current position
            initX, initY, // drag starting position
            maxDX, maxDY; // maximum distance from initial position attained
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getSelectedX() {
        return selectedNode.getMinX() + x - initX;
    }
    public double getSelectedY() {
        return selectedNode.getMinY() + y - initY;
    }

    public void setCursorPos(double x, double y) {
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
            return new SMStateNode(getSelectedX(), getSelectedY());
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
            setCursorPos(x, y);
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
