package com.example.a3_cmpt381.model;

public class InteractionModel extends ModelBase {
    // (square of) radius of mouse movement required before drag initiated
    public static final double DRAG_THRESHOLD = 100;

    // cursor drag state
    private double x, y, // current drag position
            initX, initY, // position where the drag started
            maxDX, maxDY; // maximum distance from initial position attained

    private SMStateNode selected;
    public setSelected(SMItem item) {
        selected = item;
    }

    private CursorMode cursorMode = CursorMode.DRAG;
    public CursorMode getCursorMode() {
        return cursorMode;
    }
    public void setCursorMode(CursorMode c) {
        cursorMode = c;
    }

    private InteractionState iState = InteractionState.READY;
    public InteractionState getInteractionState() {
        return iState;
    }
    public void setInteractionState(InteractionState iState) {
        this.iState = iState;
    }
}
