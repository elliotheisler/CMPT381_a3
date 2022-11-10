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
                        break;
                    case PAN:
                        break;
                    case LINK:
                }
        }
    }

    public void mouseDraggedCanvas(MouseEvent e) {
        switch (iModel.getInteractionState()) {
            case DRAGGING:
                break;
            case PANNING:
                break;
            case LINKING:
        }
    }

    public void mouseReleaseCanvas(MouseEvent e) {
        switch (iModel.getInteractionState()) {
            case DRAGGING:
                break;
            case PANNING:
                break;
            case LINKING:
        }
    }

    private Point2D fromMiddlePoint(double x, double y, Rectangle2D r) {
        return fromMiddlePoint(x, y, r.getWidth(), r.getHeight());
    }
    private Point2D fromMiddlePoint(double x, double y, double width, double height) {
        return new Point2D(x - width / 2, y - height / 2);
    }
}