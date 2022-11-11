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

public class DiagramView extends StackPane implements ModelListener {
    public static final double WIDTH = 800;
    public static final double HEIGHT = WIDTH;

    private Pane viewport = new Pane();

    private Rectangle background = createBackground();
    
    private Map<SMItem, ItemProjection> itemProjections = new HashMap();
    private Map<SMTransitionLink, Arrow> sourceArrows = new HashMap();
    private Map<SMTransitionLink, Arrow> drainArrows = new HashMap();

    private Arrow linkingArrow = new Arrow();
    
    private SMModel smModel;
    public void setSMModel(SMModel smModel) {
        this.smModel = smModel;
    }


    private InteractionModel iModel;
    public void setIModel(InteractionModel iModel) {
        this.iModel = iModel;
    }


//    private ViewportModel vModel;
//    public void setViewportModel(ViewportModel vModel) {
//        this.vModel = vModel;
//    }


    public void setController(AppController controller) {
        viewport.setOnMousePressed(e -> controller.mousePressCanvas(e));
        viewport.setOnMouseClicked(e -> controller.mouseClickCanvas(e));
        viewport.setOnMouseReleased(e -> controller.mouseReleaseCanvas(e));
        viewport.setOnMouseDragged(e -> controller.mouseDraggedCanvas(e));
    }


    public DiagramView() {
        getStyleClass().add("DiagramView");
        viewport.getStyleClass().add("viewport");
        viewport.setPrefSize(WIDTH, HEIGHT);
        HBox.setHgrow(this, Priority.ALWAYS);
        viewport.getChildren().add(background);
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
                start = iModel.worldToViewport(iModel.getSelectedItem().getMiddle());
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
                updateBackground();
                updateSourceArrows(smModel.getLinks());
                updateDrainArrows(smModel.getLinks());
                break;
            case UPDATE_TEXT:
                updateItemText(changedItem);
                break;
        }
    }

    private void updateItemRect(SMItem item) {
        ItemProjection projection = itemProjections.get(item);
        Point2D newCorner = iModel.worldToViewport(item.getMin());
        projection.setPos(newCorner);
    }

    private void updateDrainArrows(Collection<SMTransitionLink> links) {
        for (SMTransitionLink link : links) {
            updateDrainArrow(link);
        }
    }
    private void updateDrainArrow(SMTransitionLink link) {
        Point2D middle, end;
        middle = link.getMiddle();
        end = link.getDrain().getMiddle();

        Point2D lineStart, lineEnd;
        lineStart = iModel.worldToViewport(
                InterceptCalc.getFirstIntercept(middle, end, link)
        );
        lineEnd   = iModel.worldToViewport(
                InterceptCalc.getFirstIntercept(middle, end, link.getDrain())
        );
        if (lineStart == null || lineEnd == null)
            return;
        Arrow arrow = drainArrows.get(link);
        arrow.setStartPos(lineStart);
        arrow.setEndPos(lineEnd);
    }

    private void deleteLinks(Collection<SMTransitionLink> links) {
        for (SMTransitionLink link : links) {
            deleteLink(link);
        }
    }
    private void deleteLink(SMTransitionLink link) {
        viewport.getChildren().removeAll(sourceArrows.get(link).getShapes());
        viewport.getChildren().removeAll(drainArrows.get(link).getShapes());
        viewport.getChildren().removeAll(itemProjections.get(link));
        sourceArrows.remove(link);
        drainArrows.remove(link);
        itemProjections.remove(link);
    }

    private void updateSourceArrows(Collection<SMTransitionLink> links) {
        for (SMTransitionLink link : links) {
            updateSourceArrow(link);
        }
    }
    private void updateSourceArrow(SMTransitionLink link) {
        Point2D start, middle;
        start = link.getSource().getMiddle();
        middle = link.getMiddle();

        Point2D lineStart, lineEnd;
        lineStart = iModel.worldToViewport(
                InterceptCalc.getFirstIntercept(start, middle, link.getSource())
        );
        lineEnd   = iModel.worldToViewport(
                InterceptCalc.getFirstIntercept(start, middle, link)
        );
        if (lineStart == null || lineEnd == null)
            return;
        Arrow arrow = sourceArrows.get(link);
        arrow.setStartPos(lineStart);
        arrow.setEndPos(lineEnd);
    }

    private NodeProjection projectionFromNode(SMStateNode node) {
        NodeProjection projection = new NodeProjection();
        projection.setPos(iModel.worldToViewport(node.getMin()));
        return projection;
    }
    
    private LinkProjection projectionFromLink(SMTransitionLink link) {
        LinkProjection projection = new LinkProjection();
        projection.setPos(iModel.worldToViewport(link.getMin()));
        projection.setEvent(link.getEvent());
        projection.setContext(link.getContext());
        projection.setSideEffect(link.getSideEffect());
        return projection;
    }
    
    private void updateItemText(SMItem item) {
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

    private Rectangle createBackground() {
        Rectangle background = new Rectangle();
        background.getStyleClass().add("world");
        background.toFront();
        background.setFill(Color.WHITESMOKE);
        background.setWidth(SMModel.DIMENSIONS.getX());
        background.setHeight(SMModel.DIMENSIONS.getY());
        return background;
    }
    
    private void updateBackground() {
        Point2D newCorner = iModel.worldToViewport(new Point2D(0, 0));
        background.setTranslateX(newCorner.getX());
        background.setTranslateY(newCorner.getY());
    }

    private void deleteItem(SMItem changedItem) {
        switch (changedItem.TYPE) {
            case NODE:
                ItemProjection projection = itemProjections.get(changedItem);
                // delete projection of this node
                itemProjections.remove(changedItem);
                // delete projections of links to/from this node
                SMStateNode changedNode = (SMStateNode) changedItem;
                deleteLinks(smModel.getIncomingLinks(changedNode));
                deleteLinks(smModel.getOutgoingLinks(changedNode));
                viewport.getChildren().remove(projection);
                break;
            case LINK:
                deleteLink((SMTransitionLink) changedItem);
        }
    }
}
