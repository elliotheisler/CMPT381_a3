package com.example.a3_cmpt381.model;

import com.example.a3_cmpt381.model.sm_item.SMItem;
import com.example.a3_cmpt381.model.sm_item.SMItemType;
import com.example.a3_cmpt381.model.sm_item.SMStateNode;
import com.example.a3_cmpt381.model.sm_item.SMTransitionLink;
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
    public Point2D getCursorPos() {
        return cursorPos;
    }

    private SMItem selected;
    public SMItem getSelectedItem() {
        return selected;
    }
    
    public void dragStart(SMModel smModel, SMItem item, Point2D pos) {
        lastChange = ModelTransition.SELECT;
        changedItem = this.selected = smModel.pop(item);
        initCursor = cursorPos = pos;
        initSelected = item.getMin();
        dragInitiated = false;
        notifySubscribers();
    }

    public void dragUpdate(Point2D pos) {
        cursorPos = pos;
        if (dragInitiated || squareDistance(initCursor, pos) >= DRAG_THRESHOLD) {
            lastChange = (selected.type == SMItemType.NODE)
                    ? ModelTransition.DRAGGING_NODE
                    : ModelTransition.DRAGGING_LINK;
            dragInitiated = true;
            selected.setMin(pos.subtract(initCursor).add(initSelected));
            notifySubscribers();
        }
    }
    
    public void dragRelease(SMModel smModel) {
        if (smModel.anyIntersects(selected)) {
            lastChange = (selected.type == SMItemType.NODE)
                    ? ModelTransition.DRAGGING_NODE
                    : ModelTransition.DRAGGING_LINK;
            selected.setMin(initSelected);
        }
        smModel.push(selected);
        notifySubscribers();
    }
    
    public void linkStart(SMItem item, Point2D cursorPos) {
        lastChange = ModelTransition.SELECT;
        changedItem = selected = item;
        initCursor = this.cursorPos = cursorPos;
        initSelected = item.getMin();
        dragInitiated = false;
        notifySubscribers();
    }
    
    public void linkUpdate(Point2D cursorPos) {
        lastChange = ModelTransition.UPDATE_LINKING;
        this.cursorPos = cursorPos;
        notifySubscribers();
    }
    
    public void linkRelease(SMModel smModel) {
        SMStateNode end = smModel.getNode(cursorPos);
        if (end == null)
            return;
        setLastChange(ModelTransition.ADD_LINK);
        SMTransitionLink newLink = SMTransitionLink.fromSourceDrain((SMStateNode) getSelectedItem(), end);
        changedItem = newLink;
        smModel.addLink(newLink);
    }

    // to notify view. most recently added/updated/deleted
    SMItem changedItem;
    public SMItem getChangedItem() {
        return changedItem;
    }
    ModelTransition lastChange = ModelTransition.NONE;
    public ModelTransition getLastChange() {
        return lastChange;
    }
    public void setLastChange(ModelTransition lastChange) {
        this.lastChange = lastChange;
    }
    public void setLastChange(ModelTransition lastChange, SMItem changedItem) {
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
