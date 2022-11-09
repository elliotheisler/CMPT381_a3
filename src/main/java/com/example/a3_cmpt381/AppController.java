package com.example.a3_cmpt381;

import com.example.a3_cmpt381.model.*;
import javafx.event.ActionEvent;
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
                switch (iModel.getCursorMode()) {
                    case DRAG:
                        SMStateNode selected = smModel.getNode(e.getX(), e.getY());
                        /* TODO: change selected behaviour to avoid code like this
                         * stop popping/pushing selected to/from SMModel
                         */
                        if (selected == null) {
                            selected =
                                    iModel.getSelectedNode() != null && iModel.getSelectedNode().contains(e.getX(), e.getY())
                                    ? iModel.getSelectedNode()
                                    : null;
                        }
                        if (selected == null) {
                            tryAddNode(fromMiddlePoint(e.getX(), e.getY(), SMStateNode.WIDTH, SMStateNode.HEIGHT));
                        } else {
                            if (iModel.getSelectedNode() != null)
                                smModel.addNode(iModel.getSelectedNode());
                            iModel.setSelectedNode(smModel.popNode(selected), e.getX(), e.getY());
                            iModel.setInteractionState(InteractionState.PRESSED);
                        }
                        break;
                    case PAN:
                        System.out.println("panning not implemented");
                        break;
                    case LINK:
                        System.out.println("linking not implemented");
                }
        }
    }

    public void mouseDraggedCanvas(MouseEvent e) {
        switch (iModel.getInteractionState()) {
            case PRESSED:
                iModel.setSelectedPos(e.getX(), e.getY());
                iModel.setInteractionState(InteractionState.DRAGGING);
                break;
            case DRAGGING:
                iModel.setSelectedPos(e.getX(), e.getY());
                break;
            case PANNING:
                System.out.println("PANNING drag not implemented");
                break;
            case LINKING:
                System.out.println("LINKING drag not implemmented");
        }
    }

    public void mouseReleaseCanvas(MouseEvent e) {
        switch (iModel.getInteractionState()) {
            case PRESSED:
                iModel.setInteractionState(InteractionState.READY);
                break;
            case DRAGGING:
                SMStateNode oldSelected = iModel.getSelectedNode();
                SMStateNode newSelected = iModel.popNewSelectedNode();
                smModel.addNode(smModel.anyIntersects(newSelected)
                                ? oldSelected
                                : newSelected
                );
                iModel.setInteractionState(InteractionState.READY);
                break;
            case PANNING:
                System.out.println("panning release not");
                break;
            case LINKING:
                System.out.println("linking release not");
        }
    }

    private SMStateNode tryAddNode(Point2D p) {
        return tryAddNode(p.getX(), p.getY());
    }
    private SMStateNode tryAddNode(double x, double y) {
        SMStateNode candidate = new SMStateNode(x, y);
        if (!(smModel.anyIntersects(candidate)) && (iModel.getSelectedNode() == null || !iModel.getSelectedNode().intersects(candidate))) {
            smModel.addNode(candidate);
            return candidate;
        }
        return null;
    }

    private Point2D fromMiddlePoint(double x, double y, Rectangle2D r) {
        return fromMiddlePoint(x, y, r.getWidth(), r.getHeight());
    }
    private Point2D fromMiddlePoint(double x, double y, double width, double height) {
        return new Point2D(x - width / 2, y - height / 2);
    }
}