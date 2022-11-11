package com.example.a3_cmpt381;

import com.example.a3_cmpt381.model.*;
import com.example.a3_cmpt381.model.sm_item.SMItem;
import com.example.a3_cmpt381.model.sm_item.SMStateNode;
import com.example.a3_cmpt381.model.sm_item.SMTransitionLink;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;

public class AppController {
    private SMModel smModel;
    public void setSMModel(SMModel smModel) {
        this.smModel = smModel;
    }

    private InteractionModel iModel;
    public void setIModel(InteractionModel iModel) {
        this.iModel = iModel;
    }

    public void selectPointer(ActionEvent e) {
        iModel.setCursorMode(CursorMode.DRAG);
    }

    public void selectMove(ActionEvent e) {
        iModel.setCursorMode(CursorMode.PAN);
    }

    public void selectLink(ActionEvent e) {
        iModel.setCursorMode(CursorMode.LINK);
    }
    
    public void mouseClickCanvas(MouseEvent e) {
    }

    public void mousePressCanvas(MouseEvent e) {
        switch (iModel.getInteractionState()) {
            case READY:
                Point2D cursorPos = new Point2D(e.getX(), e.getY());
                Point2D worldPos = iModel.viewportToWorld(cursorPos);
                SMItem selected;
                switch (iModel.getCursorMode()) {
                    case DRAG:
                        selected = smModel.getItem(worldPos);
                        if (selected == null) {
                            SMStateNode newNode = new SMStateNode(SMStateNode.middleToCorner(worldPos));
                            if (smModel.anyIntersects(newNode))
                                break;
                            iModel.setLastChange(ModelTransition.ADD_NODE, newNode);
                            smModel.addNode(newNode);
                        } else {
                            iModel.setInteractionState(InteractionState.DRAGGING);
                            iModel.dragStart(smModel, selected, cursorPos);
                        }
                        break;
                    case PAN:
                        iModel.setInteractionState(InteractionState.PANNING);
                        iModel.panStart(cursorPos);
                        break;
                    case LINK:
                        selected = smModel.getNode(worldPos);
                        if (selected == null)
                            break;
                        iModel.setInteractionState(InteractionState.LINKING);
                        iModel.linkStart(
                                selected,
                                cursorPos
                        );
                }
        }
    }

    public void mouseDraggedCanvas(MouseEvent e) {
        Point2D cursorPos = new Point2D(e.getX(), e.getY());
        switch (iModel.getInteractionState()) {
            case DRAGGING:
                iModel.dragUpdate(cursorPos);
                break;
            case PANNING:
                iModel.panUpdate(cursorPos);
                break;
            case LINKING:
                iModel.linkUpdate(cursorPos);
        }
    }

    public void mouseReleaseCanvas(MouseEvent e) {
        Point2D cursorPos = new Point2D(e.getX(), e.getY());
        switch (iModel.getInteractionState()) {
            case DRAGGING:
                iModel.setInteractionState(InteractionState.READY);
                iModel.dragRelease(smModel);
                break;
            case PANNING:
                iModel.setInteractionState(InteractionState.READY);
                break;
            case LINKING:
                iModel.setInteractionState(InteractionState.READY);
                iModel.linkRelease(smModel);
        }
    }

    private Point2D fromMiddlePoint(Point2D p, double width , double height) {
        return fromMiddlePoint(p.getX(), p.getY(), width, height);
    }
    private Point2D fromMiddlePoint(double x, double y, double width, double height) {
        return new Point2D(x - width / 2, y - height / 2);
    }
}