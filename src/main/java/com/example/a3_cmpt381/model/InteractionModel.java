package com.example.a3_cmpt381.model;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class InteractionModel extends ModelBase {

    // need to store seperate from selectedNode in order to mutate it, since Rectangle2D is immutable
    private double x, y;
    public double getX() { return x; }
    public double getY() { return y; }

    public void setSelectedPos(Point2D p) {
        setSelectedPos(p.getX(), p.getY());
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

    public void setSelectedNode(SMStateNode selectedNode) {
        this.selectedNode = selectedNode;
        if (selectedNode == null)
            notifySubscribers();
        else
            setSelectedPos(selectedNode.getMinX(), selectedNode.getMinY());
    }
    public SMStateNode popNewSelectedNode() {
        this.selectedNode = null;
        return new SMStateNode(x, y);
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
