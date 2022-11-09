package com.example.a3_cmpt381;

import com.example.a3_cmpt381.model.*;
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

    public void mouseDraggedCanvas(MouseEvent e) {
    }

    public void mouseReleaseCanvas(MouseEvent e) {
        iModel.setInteractionState(InteractionState.READY);
    }

    public void mouseClickCanvas(MouseEvent e) {
    }

    public void mousePressCanvas(MouseEvent e) {
        Point2D canvasPoint = new Point2D(e.getX(), e.getY());
        switch (iModel.getInteractionState()) {
            case READY:
                switch (iModel.getCursorMode()) {
                    case DRAG:
                        SMStateNode selected = smModel.getNode(canvasPoint);
                        if (selected == null) {
                            smModel.tryAddNode(e.getX(), e.getY());
                        } else {
                            iModel.setSelectedNode(selected);
                            System.out.println("selected: " + iModel.getSelectedNode());
                            iModel.setInteractionState(InteractionState.DRAGGING);
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

}