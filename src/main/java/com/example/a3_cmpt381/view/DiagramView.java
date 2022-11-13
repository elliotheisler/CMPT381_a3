package com.example.a3_cmpt381.view;

import com.example.a3_cmpt381.AppController;
import com.example.a3_cmpt381.model.SMModel;
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.min;
import static java.lang.Math.pow;

public class DiagramView extends StackPane implements ModelListener {
    static final double SIZE = 800;

    private Map<SMItem, ItemProjection> miniProjections = new HashMap();
    private Map<SMTransitionLink, Arrow> miniSourceArrows = new HashMap();
    private Map<SMTransitionLink, Arrow> miniDrainArrows = new HashMap();
    private Rectangle miniRect = createMiniRect();

    private Rectangle createMiniRect() {
        Rectangle r = new Rectangle(0, 0, 1, 1);
        r.getStyleClass().add("mini");
        return r;
    }

    private void updateMiniRect() {
        miniRect.setScaleX(miniSize());
        miniRect.setScaleY(miniSize());
        Point2D corner = iModel.getTranslatePos().multiply(scale());
        miniRect.setTranslateX(corner.getX() + miniSize() / 2);
        miniRect.setTranslateY(corner.getY() + miniSize() / 2);
    }

    protected NodeProjection projectionFromNodeMini(SMStateNode node) {
        NodeProjection projection = new NodeProjection();
        projection.setPos(node.getMin().multiply(scale()));

        projection.setScaleX(scale());
        projection.setScaleY(scale());
        projection.getStyleClass().addAll("mini");
        return projection;
    }

    protected LinkProjection projectionFromLinkMini(SMTransitionLink link) {
        LinkProjection projection = new LinkProjection();
        projection.setPos(link.getMin().multiply(scale()));

        projection.setEvent(link.getEvent());
        projection.setContext(link.getContext());
        projection.setSideEffect(link.getSideEffect());

        projection.setScaleX(scale());
        projection.setScaleY(scale());
        projection.getStyleClass().addAll("mini");
        return projection;
    }

    public DiagramView() {
        super();
        getStyleClass().add("DiagramView");
        viewport.setPrefSize(SIZE, SIZE);
        HBox.setHgrow(this, Priority.ALWAYS);
        viewport.getChildren().addAll(miniRect, mainRect);
        viewport.getChildren().addAll(linkingArrow.getShapes());
        linkingArrow.setVisible(false);
        getChildren().add(viewport);
        getStyleClass().add("DiagramView");
        viewport.getStyleClass().add("viewport");
        miniRect.toFront();
    }

    public Rectangle createMainRect() {
        Rectangle mainRect = new Rectangle();
        mainRect.getStyleClass().add("world");
        mainRect.toFront();
        mainRect.setFill(Color.WHITESMOKE);
        mainRect.setWidth(SMModel.SIZE);
        mainRect.setHeight(SMModel.SIZE);
        return mainRect;
    }

    public void updateMainRect() {
        Point2D newCorner = worldToViewport(new Point2D(0, 0));
        mainRect.setTranslateX(newCorner.getX());
        mainRect.setTranslateY(newCorner.getY());
    }

    public Point2D worldToViewport(Point2D p) {
        return iModel.worldToViewport(p);
    }

    //==========================================================================//

    protected Pane viewport = new Pane();

    protected Rectangle mainRect = createMainRect();

    protected Map<SMItem, ItemProjection> itemProjections = new HashMap();
    protected Map<SMTransitionLink, Arrow> sourceArrows = new HashMap();
    protected Map<SMTransitionLink, Arrow> drainArrows = new HashMap();

    protected Arrow linkingArrow = new Arrow();

    protected SMModel smModel;


    protected InteractionModel iModel;


    public void setController(AppController controller) {
        setOnMousePressed(e -> controller.mousePressCanvas(e));
        setOnMouseClicked(e -> controller.mouseClickCanvas(e));
        setOnMouseReleased(e -> controller.mouseReleaseCanvas(e));
        setOnMouseDragged(e -> controller.mouseDraggedCanvas(e));
    }

    public void modelChanged(Class<?> c) {
        SMItem changedItem = iModel.getChangedItem();
        SMStateNode changedNode;
        SMTransitionLink changedLink;
        ItemProjection projection;
        ItemProjection mini;
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
                updateItemProjection(changedNode);
                updateDrainArrows(changedNode.getIncoming());
                updateSourceArrows(changedNode.getOutgoing());
                break;
            case DRAGGING_LINK:
                changedLink = (SMTransitionLink) changedItem;
                updateItemProjection(changedLink);
                updateDrainArrow(changedLink);
                updateDrainMini(changedLink);
                updateSourceArrow(changedLink);
                updateSourceMini(changedLink);
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
                mini = projectionFromNodeMini(changedNode);
                itemProjections.put(changedItem, projection);
                miniProjections.put(changedItem, mini);
                viewport.getChildren().addAll(mini, projection);
                projection.setViewOrder(-1);
                updateItemProjection(changedItem);
                break;
            case END_LINKING:
                // linking arrow becomes invisible again
                linkingArrow.setVisible(false);
                if (changedItem == null)
                    break;
                changedLink = (SMTransitionLink) changedItem;
                // add rectangle projection
                projection = projectionFromLink(changedLink);
                mini = projectionFromLinkMini(changedLink);
                itemProjections.put(changedItem, projection);
                miniProjections.put(changedItem, mini);
                viewport.getChildren().addAll(projection, mini);
                projection.setViewOrder(-2);
                updateItemProjection(changedLink);
                
                // add source arrow, big and mini
                Arrow sourceArrow = new Arrow();
                sourceArrows.put(changedLink, sourceArrow);
                updateSourceArrow(changedLink);
                viewport.getChildren().addAll(sourceArrow.getShapes());
                
//                sourceArrow = new Arrow();
//                miniSourceArrows.put(changedLink, sourceArrow);
//                updateSourceMini(changedLink);
//                viewport.getChildren().addAll(sourceArrow.getShapes());
                
                // add drain arrow
                Arrow drainArrow = new Arrow();
                drainArrows.put(changedLink, drainArrow);
                updateDrainArrow(changedLink);
                viewport.getChildren().addAll(drainArrow.getShapes());

//                drainArrow = new Arrow();
//                miniDrainArrows.put(changedLink, sourceArrow);
//                updateDrainMini(changedLink);
//                viewport.getChildren().addAll(drainArrow.getShapes());
                break;
            case DELETE:
                deleteItem(changedItem);
                break;
            case UPDATE_PANNING:
                for (SMItem item : itemProjections.keySet()) {
                    updateItemProjection(item);
                }
                updateMainRect();
                updateMiniRect();
                updateSourceArrows(smModel.getLinks());
                updateDrainArrows(smModel.getLinks());
                break;
            case UPDATE_TEXT:
                updateItemText(changedItem);
                break;
        }
    }

    protected void updateItemProjection(SMItem item) {
        ItemProjection projection = itemProjections.get(item);
        Point2D newCorner = worldToViewport(item.getMin());
        projection.relocate(newCorner.getX(), newCorner.getY());
        // now the mini
        projection = miniProjections.get(item);
        newCorner = item.getMin().multiply(scale());
        projection.relocate(newCorner.getX(), newCorner.getY());
    }

    protected void updateDrainArrows(Collection<SMTransitionLink> links) {
        for (SMTransitionLink link : links) {
            updateDrainArrow(link);
            updateDrainMini(link);
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

    private void updateSourceMini(SMTransitionLink link) {
//        Point2D start, middle, end;
//        start  = link.getSource().getMiddle().multiply(scale());
//        middle = link            .getMiddle().multiply(scale());
////      end    = link.getDrain() .getMiddle().multiply(scale());
//
//        Arrow arrow = miniSourceArrows.get(link);
//        arrow.setStartPos(start);
//        arrow.setEndPos(middle);
    }

    private void updateDrainMini(SMTransitionLink link) {
//        Point2D start, middle, end;
////      start  = link.getSource().getMiddle().multiply(scale());
//        middle = link            .getMiddle().multiply(scale());
//        end    = link.getDrain() .getMiddle().multiply(scale());
//
//        Arrow arrow = miniDrainArrows.get(link);
//        arrow.setStartPos(middle);
//        arrow.setEndPos(end);
    }

    protected void deleteLinks(Collection<SMTransitionLink> links) {
        for (SMTransitionLink link : links) {
            deleteLink(link);
        }
    }

    protected void deleteLink(SMTransitionLink link) {
        // remove from viewport
        viewport.getChildren().removeAll(sourceArrows.get(link).getShapes());
        viewport.getChildren().removeAll(drainArrows.get(link).getShapes());
        viewport.getChildren().removeAll(itemProjections.get(link));
        viewport.getChildren().removeAll(miniProjections.get(link));
        // remove from maps
        sourceArrows.remove(link);
        drainArrows.remove(link);
        itemProjections.remove(link);
        miniProjections.remove(link);
    }

    protected void updateSourceArrows(Collection<SMTransitionLink> links) {
        for (SMTransitionLink link : links) {
            updateSourceArrow(link);
            updateSourceMini(link);
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
        return projection;
    }

    protected LinkProjection projectionFromLink(SMTransitionLink link) {
        LinkProjection projection = new LinkProjection();
        projection.setPos(worldToViewport(link.getMin()));
        projection.setEvent(link.getEvent());
        projection.setContext(link.getContext());
        projection.setSideEffect(link.getSideEffect());
        return projection;
    }

    protected void updateItemText(SMItem item) {
        switch (item.TYPE) {
            case NODE:
                SMStateNode node = (SMStateNode) item;
                NodeProjection projectionNode = (NodeProjection) itemProjections.get(node);
                projectionNode.updateText(node);
                // mini
                ((NodeProjection) miniProjections.get(node)).updateText(node);
                break;
            case LINK:
                SMTransitionLink link = (SMTransitionLink) item;
                LinkProjection projectionLink = (LinkProjection) itemProjections.get(link);
                projectionLink.updateText(link);
                // mini
                ((LinkProjection) miniProjections.get(link)).updateText(link);
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
        // delete projections of this node
        ItemProjection projection = itemProjections.get(node);
        itemProjections.remove(node);
        viewport.getChildren().remove(projection);
        // mini
        projection = miniProjections.get(node);
        miniProjections.remove(node);
        viewport.getChildren().remove(projection);
        // delete adjacent links
        deleteLinks(iModel.getDeletedLinks());
    }

    private double miniSize() {
        return pow(min(viewport.heightProperty().get(), viewport.widthProperty().get()), 2) / SMModel.SIZE;
    }
    private double scale() {
        return min(viewport.heightProperty().get(), viewport.widthProperty().get()) / SMModel.SIZE;
    }

    public void setIModel(InteractionModel iModel) {
        this.iModel = iModel;
    }
    public void setSMModel(SMModel smModel) {
        this.smModel = smModel;
    }
}
