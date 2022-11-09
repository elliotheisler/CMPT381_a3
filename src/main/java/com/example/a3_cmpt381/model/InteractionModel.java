package com.example.a3_cmpt381.model;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class InteractionModel extends ModelBase {

    // need to store seperate from selectedNode in order to mutate it, since Rectangle2D is immutable
    private double x, y, initX, initY;
    public double getX() {
        return selectedNode.getMinX() + x - initX;
    }
    public double getY() {
        return selectedNode.getMinX() + y - initY;
    }

    public void setSelectedPos(double x, double y) {
        this.x = x;
        this.y = y;
        notifySubscribers();
    }

    private SMStateNode selectedNode;

    public SMStateNode getSelectedNode() {
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
    public SMStateNode popNewSelectedNode() {
        this.selectedNode = null;
        return new SMStateNode(getX(), getY());
    }

    public CursorMode getCursorMode() {
        return cursorMode;
    }
    private CursorMode cursorMode = CursorMode.DRAG;

    public void setInteractionState(InteractionState iState) {
        this.iState = iState;
    }

    public InteractionState getInteractionState() {
        return iState;
    }
    private InteractionState iState = InteractionState.READY;

    public void setCursorMode(CursorMode c) {
        cursorMode = c;
        notifySubscribers();
    }
}
