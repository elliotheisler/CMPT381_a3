package com.example.a3_cmpt381.view;

import com.example.a3_cmpt381.AppController;
import com.example.a3_cmpt381.model.interaction_model.InteractionModel;
import com.example.a3_cmpt381.model.SMModel;
import com.example.a3_cmpt381.model.sm_item.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class EditorView extends StackPane implements ModelListener {
    private static double WIDTH = 250;

    InteractionModel iModel;
    SMModel smModel;

    TextField entryNode,
            entryEvent;

    TextArea entryContext,
        entrySideEffect;


    Button updateButton = new Button("Update");
    VBox nodePane = createNodePane();
    VBox linkPane = createLinkPane();
    VBox blankPane = createBlankPane();

    public EditorView() {
        getChildren().addAll(nodePane, linkPane, blankPane);
        setPrefSize(WIDTH, Double.MAX_VALUE);
        setMinWidth(WIDTH);
    }

    private VBox createBlankPane() {
        Label title = new Label("No Item Selected");
        StackPane titleWrapper = new StackPane(title);
        titleWrapper.setAlignment(Pos.CENTER);
        titleWrapper.getStyleClass().add("editor_title");
        titleWrapper.setPrefWidth(Double.MAX_VALUE);

        VBox vBox = new VBox(titleWrapper);
        vBox.getStyleClass().add("editor_pane");
        return vBox;
    }

    private VBox createNodePane() {
        Label title = new Label("State");
        StackPane titleWrapper = new StackPane(title);
        titleWrapper.setAlignment(Pos.CENTER);
        titleWrapper.getStyleClass().add("editor_title");
        titleWrapper.setPrefWidth(Double.MAX_VALUE);
        Label header = new Label("State Name:");
        header.getStyleClass().add("editor_header");
        entryNode = new TextField();

        VBox vBox = new VBox(titleWrapper, header, entryNode);
        vBox.getStyleClass().add("editor_pane");
        return vBox;
    }

    private VBox createLinkPane() {
        Label title = new Label("Transition");
        StackPane titleWrapper = new StackPane(title);
        titleWrapper.setAlignment(Pos.CENTER);
        titleWrapper.getStyleClass().add("editor_title");
        titleWrapper.setPrefWidth(Double.MAX_VALUE);

        Label headerEvent = new Label("Event:");
        headerEvent.getStyleClass().add("editor_header");
        entryEvent = new TextField();

        Label headerContext = new Label("Context:");
        headerContext.getStyleClass().add("editor_header");
        entryContext = new TextArea();
        entryContext.setPrefRowCount(15);

        Label headerSideEffect = new Label("Side Effects:");
        headerSideEffect.getStyleClass().add("editor_header");
        entrySideEffect = new TextArea();
        entrySideEffect.setPrefRowCount(10);

        updateButton.getStyleClass().add("button");

        VBox vBox = new VBox(titleWrapper,
                headerEvent, entryEvent,
                headerContext, entryContext,
                headerSideEffect, entrySideEffect,
                updateButton
        );
        vBox.getStyleClass().add("editor_pane");
        return vBox;
    }

    public void setController(AppController controller) {
        updateButton.setOnAction(e -> controller.updateText(new EditorText(
                new NodeText(entryNode.getText()),
                new LinkText(
                        entryEvent.getText(),
                        entryContext.getText(),
                        entrySideEffect.getText())
        )));
        entryNode.setOnKeyPressed(e -> controller.textViewKeyPress(e, new EditorText(
                new NodeText(entryNode.getText()),
                new LinkText(
                        entryEvent.getText(),
                        entryContext.getText(),
                        entrySideEffect.getText())
        )));
        entryEvent.setOnKeyPressed(e -> controller.textViewKeyPress(e, new EditorText(
                new NodeText(entryNode.getText()),
                new LinkText(
                        entryEvent.getText(),
                        entryContext.getText(),
                        entrySideEffect.getText())
        )));
    }

    public void setSMModel(SMModel smModel) {
        this.smModel = smModel;
    }

    public void setIModel(InteractionModel iModel) {
        this.iModel = iModel;
    }

    public void modelChanged(Class<?> c) {
        SMItem changedItem = iModel.getChangedItem();
        switch(iModel.getLastChange()) {
            case SELECT:
                updateTexts(changedItem);
                break;
            case DESELECT:
                updateTexts(null);
        }
    }

    public void updateTexts(SMItem item) {
        if (item == null) {
            blankPane.toFront();
            return;
        }
        switch (item.TYPE) {
            case NODE:
                SMStateNode node = (SMStateNode) item;
                entryNode.setText(node.getName());
                nodePane.toFront();
                break;
            case LINK:
                SMTransitionLink link = (SMTransitionLink) item;
                entryEvent.setText(link.getEvent());
                entryContext.setText(link.getContext());
                entrySideEffect.setText(link.getSideEffect());
                linkPane.toFront();
                break;
        }
    }

    private void setDefaults() {
        entryNode.setText("Default");
        entryEvent.setText("No Event");
        entryContext.setText("No Context");
        entrySideEffect.setText("No Side Effects");
    }

    public void clearFields() {
        entryNode.clear();
        entryEvent.clear();
        entryContext.clear();
        entrySideEffect.clear();
    }
}
