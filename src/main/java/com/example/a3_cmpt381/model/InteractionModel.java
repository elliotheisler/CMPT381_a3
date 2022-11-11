package com.example.a3_cmpt381.model;

import com.example.a3_cmpt381.model.sm_item.SMItem;
import com.example.a3_cmpt381.model.sm_item.SMItemType;
import com.example.a3_cmpt381.model.sm_item.SMStateNode;
import com.example.a3_cmpt381.model.sm_item.SMTransitionLink;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

import static java.lang.Math.pow;

public class InteractionModel extends ModelBase {
    // (square of) radius of mouse movement required before drag initiated
    public static final double DRAG_THRESHOLD = 100;
    public static final double WIDTH = 1000;
    public static final double HEIGHT = 800;


    // panning
    Point2D translatePos = new Point2D(0, 0);

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
    
    public void dragStart(SMModel smModel, SMItem item, Point2D newCursorPos) {
        deselect();
        lastChange = ModelTransition.SELECT;
        changedItem = selected = smModel.pop(item);
        initCursor = cursorPos = newCursorPos;
        initSelected = item.getMin();
        dragInitiated = false;
        notifySubscribers();
    }

    public void dragUpdate(Point2D newCursorPos) {
        this.cursorPos = newCursorPos;
        if (dragInitiated || squareDistance(initCursor, newCursorPos) >= DRAG_THRESHOLD) {
            lastChange = (selected.type == SMItemType.NODE)
                    ? ModelTransition.DRAGGING_NODE
                    : ModelTransition.DRAGGING_LINK;
            dragInitiated = true;
            selected.setMin(newCursorPos.subtract(initCursor).add(initSelected));
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
    
    public void linkStart(SMItem item, Point2D newCursorPos) {
        deselect();
        // initiate linking
        changedItem = selected = item;
        initCursor = cursorPos = newCursorPos;
        notifySubscribers();
    }
    
    public void linkUpdate(Point2D newCursorPos) {
        lastChange = ModelTransition.UPDATE_LINKING;
        this.cursorPos = newCursorPos;
        notifySubscribers();
    }
    
    public void linkRelease(SMModel smModel) {
        SMStateNode end = smModel.getNode(viewportToWorld(cursorPos));
        if (end == null)
            return;
        setLastChange(ModelTransition.ADD_LINK);
        SMTransitionLink newLink = SMTransitionLink.fromSourceDrain((SMStateNode) getSelectedItem(), end);
        changedItem = newLink;
        smModel.addLink(newLink);
    }

    public void panStart(Point2D cursorPos) {
        initCursor = this.cursorPos = cursorPos;
    }

    public void panUpdate(Point2D cursorPos) {
        lastChange = ModelTransition.UPDATE_PANNING;
        translatePos = translatePos.add(cursorPos).subtract(this.cursorPos);
        this.cursorPos = cursorPos;
        notifySubscribers();
    }

    public void deselect() {
        lastChange = ModelTransition.DESELECT;
        changedItem = selected;
        notifySubscribers();
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

    public Point2D viewportToWorld(Point2D p) {
        return p == null ? null : p.subtract(translatePos);
    }

    public Point2D worldToViewport(Point2D p) {
        return p == null ? null : p.add(translatePos);
    }
    
    private static double squareDistance(Point2D a, Point2D b) {
        return pow(b.getX() - a.getX(), 2) + pow(b.getY() - a.getY(), 2);
    }
}
