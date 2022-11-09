package com.example.a3_cmpt381.model;

public class InteractionModel extends ModelBase {

    ;
    
    private SMStateNode selectedNode;

    public SMStateNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(SMStateNode selectedNode) {
        this.selectedNode = selectedNode;
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
