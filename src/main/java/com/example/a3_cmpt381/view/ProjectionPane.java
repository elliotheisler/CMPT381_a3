package com.example.a3_cmpt381.view;

import com.example.a3_cmpt381.AppController;
import com.example.a3_cmpt381.model.*;
import com.example.a3_cmpt381.model.interaction_model.InteractionModel;
import com.example.a3_cmpt381.model.sm_item.SMItem;
import com.example.a3_cmpt381.model.sm_item.SMStateNode;
import com.example.a3_cmpt381.model.sm_item.SMTransitionLink;
import com.example.a3_cmpt381.view.projections.Arrow;
import com.example.a3_cmpt381.view.projections.ItemProjection;
import com.example.a3_cmpt381.view.projections.LinkProjection;
import com.example.a3_cmpt381.view.projections.NodeProjection;
import javafx.geometry.Point2D;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;

abstract class ProjectionPane extends StackPane implements ModelListener {
    public static final double WIDTH = 800;
    public static final double HEIGHT = WIDTH;

    protected Pane viewport = new Pane();

    protected Rectangle mainRect = createMainRect();
    
    protected Map<SMItem, ItemProjection> itemProjections = new HashMap();
    protected Map<SMTransitionLink, Arrow> sourceArrows = new HashMap();
    protected Map<SMTransitionLink, Arrow> drainArrows = new HashMap();

    protected Arrow linkingArrow = new Arrow();
    
    protected SMModel smModel;


    protected InteractionModel iModel;


    public void setController(AppController controller) {
        viewport.setOnMousePressed(e -> controller.mousePressCanvas(e));
        viewport.setOnMouseClicked(e -> controller.mouseClickCanvas(e));
        viewport.setOnMouseReleased(e -> controller.mouseReleaseCanvas(e));
        viewport.setOnMouseDragged(e -> controller.mouseDraggedCanvas(e));
    }


    public ProjectionPane() {
        getStyleClass().add("ProjectionPane");
        viewport.setPrefSize(WIDTH, HEIGHT);
        HBox.setHgrow(this, Priority.ALWAYS);
        viewport.getChildren().add(mainRect);
        viewport.getChildren().addAll(linkingArrow.getShapes());
        linkingArrow.setVisible(false);
        getChildren().add(viewport);
    }
    
    public void modelChanged(Class<?> c) {
        SMItem changedItem = iModel.getChangedItem();
        SMStateNode changedNode;
        SMTransitionLink changedLink;
        ItemProjection projection;
        Point2D start, middle, end;
        switch (iModel.getLastChange()) {
            case SELECT:
                projection = itemProjections.get(changedItem);
                projection.onSelect();
                break;
            case DESELECT:
                if (changedItem == null)
                    break;
                projection = itemProjections.get(changedItem);
                projection.onDeselect();
                break;
            case DRAGGING_NODE:
                changedNode = (SMStateNode) changedItem;
                updateItemRect(changedNode);
                updateDrainArrows(smModel.getIncomingLinks(changedNode));
                updateSourceArrows(smModel.getOutgoingLinks(changedNode));
                break;
            case DRAGGING_LINK:
                changedLink = (SMTransitionLink) changedItem;
                updateItemRect(changedLink);
                updateDrainArrow(changedLink);
                updateSourceArrow(changedLink);
                break;
            case UPDATE_LINKING:
                linkingArrow.setVisible(true);
                start = worldToViewport(iModel.getSelectedItem().getMiddle());
                end = iModel.getCursorPos();
                linkingArrow.setStartPos(start);
                linkingArrow.setEndPos(end);
                break;
            case ADD_NODE:
                changedNode = (SMStateNode) changedItem;
                projection = projectionFromNode(changedNode);
                itemProjections.put(changedItem, projection);
                viewport.getChildren().add(projection);
                updateItemRect(changedItem);
                break;
            case END_LINKING:
                // linking arrow becomes invisible again
                linkingArrow.setVisible(false);
                if (changedItem == null)
                    break;
                changedLink = (SMTransitionLink) changedItem;
                // add rectangle projection
                projection = projectionFromLink(changedLink);
                itemProjections.put(changedItem, projection);
                viewport.getChildren().add(projection);
                updateItemRect(changedLink);
                // add source arrow
                Arrow sourceArrow = new Arrow();
                sourceArrows.put(changedLink, sourceArrow);
                updateSourceArrow(changedLink);
                Arrow drainArrow = new Arrow();
                viewport.getChildren().addAll(sourceArrow.getShapes());
                // add drain arrow
                drainArrows.put(changedLink, drainArrow);
                updateDrainArrow(changedLink);
                viewport.getChildren().addAll(drainArrow.getShapes());
                break;
            case DELETE:
                deleteItem(changedItem);
                break;
            case UPDATE_PANNING:
                for (SMItem item : itemProjections.keySet()) {
                    updateItemRect(item);
                }
                updateMainRect();
                updateSourceArrows(smModel.getLinks());
                updateDrainArrows(smModel.getLinks());
                break;
            case UPDATE_TEXT:
                updateItemText(changedItem);
                break;
        }
    }

    protected void updateItemRect(SMItem item) {
        ItemProjection projection = itemProjections.get(item);
        Point2D newCorner = worldToViewport(item.getMin());
        projection.setPos(newCorner);
    }

    protected void updateDrainArrows(Collection<SMTransitionLink> links) {
        for (SMTransitionLink link : links) {
            updateDrainArrow(link);
        }
    }
    protected void updateDrainArrow(SMTransitionLink link) {
        Point2D middle, end;
        middle = link.getMiddle();
        end = link.getDrain().getMiddle();

        Point2D lineStart, lineEnd;
        lineStart = worldToViewport(
                InterceptCalc.getFirstIntercept(middle, end, link)
        );
        lineEnd   = worldToViewport(
                InterceptCalc.getFirstIntercept(middle, end, link.getDrain())
        );
        if (lineStart == null || lineEnd == null)
            return;
        Arrow arrow = drainArrows.get(link);
        arrow.setStartPos(lineStart);
        arrow.setEndPos(lineEnd);
    }

    protected void deleteLinks(Collection<SMTransitionLink> links) {
        for (SMTransitionLink link : links) {
            deleteLink(link);
        }
    }
    
    protected void deleteLink(SMTransitionLink link) {
        viewport.getChildren().removeAll(sourceArrows.get(link).getShapes());
        viewport.getChildren().removeAll(drainArrows.get(link).getShapes());
        viewport.getChildren().removeAll(itemProjections.get(link));
        sourceArrows.remove(link);
        drainArrows.remove(link);
        itemProjections.remove(link);
    }

    protected void updateSourceArrows(Collection<SMTransitionLink> links) {
        for (SMTransitionLink link : links) {
            updateSourceArrow(link);
        }
    }
    
    protected void updateSourceArrow(SMTransitionLink link) {
        Point2D start, middle;
        start = link.getSource().getMiddle();
        middle = link.getMiddle();

        Point2D lineStart, lineEnd;
        lineStart = worldToViewport(
                InterceptCalc.getFirstIntercept(start, middle, link.getSource())
        );
        lineEnd   = worldToViewport(
                InterceptCalc.getFirstIntercept(start, middle, link)
        );
        if (lineStart == null || lineEnd == null)
            return;
        Arrow arrow = sourceArrows.get(link);
        arrow.setStartPos(lineStart);
        arrow.setEndPos(lineEnd);
    }

    protected NodeProjection projectionFromNode(SMStateNode node) {
        NodeProjection projection = new NodeProjection();
        projection.setPos(worldToViewport(node.getMin()));

        projection.setScaleX(scale());
        projection.setScaleY(scale());
        return projection;
    }

    protected LinkProjection projectionFromLink(SMTransitionLink link) {
        LinkProjection projection = new LinkProjection();
        projection.setPos(worldToViewport(link.getMin()));
        projection.setEvent(link.getEvent());
        projection.setContext(link.getContext());
        projection.setSideEffect(link.getSideEffect());

        projection.setScaleX(scale());
        projection.setScaleY(scale());
        return projection;
    }
    
    protected void updateItemText(SMItem item) {
        switch (item.TYPE) {
            case NODE:
                SMStateNode node = (SMStateNode) item;
                NodeProjection projectionNode = (NodeProjection) itemProjections.get(node);
                projectionNode.updateText(node);
                break;
            case LINK:
                SMTransitionLink link = (SMTransitionLink) item;
                LinkProjection projectionLink = (LinkProjection) itemProjections.get(link);
                projectionLink.updateText(link);
        }
    }

    protected void deleteItem(SMItem changedItem) {
        switch (changedItem.TYPE) {
            case NODE:
                deleteNode((SMStateNode) changedItem);
                break;
            case LINK:
                deleteLink((SMTransitionLink) changedItem);
        }
    }

    protected void deleteNode(SMStateNode node) {
        ItemProjection projection = itemProjections.get(node);
        // delete projection of this node
        itemProjections.remove(node);
        viewport.getChildren().remove(projection);
        // delete adjacent links
        deleteLinks(iModel.getDeletedLinks());
    }

    public abstract Point2D worldToViewport(Point2D p);
    public abstract double scale();
    public abstract Rectangle createMainRect();
    public abstract void updateMainRect();
}
