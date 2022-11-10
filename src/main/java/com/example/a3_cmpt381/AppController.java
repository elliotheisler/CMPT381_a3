package com.example.a3_cmpt381;

import com.example.a3_cmpt381.model.*;
import com.example.a3_cmpt381.model.sm_item.SMItem;
import com.example.a3_cmpt381.model.sm_item.SMStateNode;
import javafx.event.ActionEvent;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
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
                SMItem selected = smModel.getItem(cursorPos);
                switch (iModel.getCursorMode()) {
                    case DRAG:
                        if (selected == null) {
                            Point2D corner = fromMiddlePoint(cursorPos, SMStateNode.WIDTH, SMStateNode.HEIGHT);
                            SMStateNode newNode = new SMStateNode(corner);
                            if (smModel.anyIntersects(newNode))
                                break;
                            iModel.setLastChange(ModelChange.ADD, newNode);
                            smModel.addNode(newNode);
                        } else {
                            iModel.setInteractionState(InteractionState.DRAGGING);
                            iModel.dragStart(smModel, selected, cursorPos);
                        }
                        break;
                    case PAN:
                        break;
                    case LINK:

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
                break;
            case LINKING:
        }
    }

    public void mouseReleaseCanvas(MouseEvent e) {
        switch (iModel.getInteractionState()) {
            case DRAGGING:
                iModel.setInteractionState(InteractionState.READY);
                iModel.dragRelease(smModel);
                break;
            case PANNING:
                break;
            case LINKING:
        }
    }

    private Point2D fromMiddlePoint(Point2D p, double width , double height) {
        return fromMiddlePoint(p.getX(), p.getY(), width, height);
    }
    private Point2D fromMiddlePoint(double x, double y, double width, double height) {
        return new Point2D(x - width / 2, y - height / 2);
    }
}