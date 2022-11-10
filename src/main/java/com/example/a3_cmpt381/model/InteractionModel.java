package com.example.a3_cmpt381.model;

import com.example.a3_cmpt381.model.sm_item.SMItem;
import javafx.geometry.Point2D;

import static java.lang.Math.pow;

public class InteractionModel extends ModelBase {
    // (square of) radius of mouse movement required before drag initiated
    public static final double DRAG_THRESHOLD = 100;

    // cursor drag state
    private Point2D cursorPos, // current drag position
            initCursor, // position where the drag started
            initSelected; // initial corner position of selected
    private boolean dragInitiated;

    private SMItem selected;

    public void dragStart(SMModel smModel, SMItem item, Point2D pos) {
        lastChange = ModelChange.SELECT;
        changedItem = selected = smModel.pop(item);
        initCursor = cursorPos = pos;
        initSelected = item.getMin();
        dragInitiated = false;
        notifySubscribers();
    }

    public void dragUpdate(Point2D pos) {
        cursorPos = pos;
        if (dragInitiated || squareDistance(initCursor, pos) >= DRAG_THRESHOLD) {
            lastChange = ModelChange.UPDATE;
            dragInitiated = true;
            selected.setMin(pos.subtract(initCursor).add(initSelected));
            notifySubscribers();
        }
    }
    
    public void dragRelease(SMModel smModel) {
        if (smModel.anyIntersects(selected)) {
            lastChange = ModelChange.UPDATE;
            selected.setMin(initSelected);
        }
        smModel.push(selected);
        notifySubscribers();
    }

    // to notify view. most recently added/updated/deleted
    SMItem changedItem;
    public SMItem getChangedItem() {
        return changedItem;
    }
    ModelChange lastChange = ModelChange.NONE;
    public ModelChange getLastChange() {
        return lastChange;
    }
    public void setLastChange(ModelChange lastChange) {
        this.lastChange = lastChange;
    }
    public void setLastChange(ModelChange lastChange, SMItem changedItem) {
        this.lastChange = lastChange;
        this.changedItem = changedItem;
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


    private static double squareDistance(Point2D a, Point2D b) {
        return pow(b.getX() - a.getX(), 2) + pow(b.getY() - a.getY(), 2);
    }
}
