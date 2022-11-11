package com.example.a3_cmpt381.view;

import com.example.a3_cmpt381.AppController;
import com.example.a3_cmpt381.model.interaction_model.InteractionModel;
import com.example.a3_cmpt381.model.SMModel;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class MainUI extends StackPane {
    private HBox root;

    public MainUI() {
        getStyleClass().add("MainUI");

        DiagramView diagramView = new DiagramView();
        ToolPalette toolPalette = new ToolPalette();
        EditorView editorView = new EditorView();

        AppController controller = new AppController();

        InteractionModel iModel = new InteractionModel();
        SMModel smModel = new SMModel();

        toolPalette.setController(controller);
        toolPalette.setIModel(iModel);

        setController(controller);

        diagramView.setController(controller);
        diagramView.setSMModel(smModel);
        diagramView.setIModel(iModel);

        editorView.setController(controller);
        editorView.setSMModel(smModel);
        editorView.setIModel(iModel);

        controller.setIModel(iModel);
        controller.setSMModel(smModel);

        iModel.addSubscribers(toolPalette, diagramView, editorView);
        smModel.addSubscribers(diagramView);

        root = new HBox();
        root.getChildren().addAll(toolPalette, diagramView, editorView);
        getChildren().add(root);
        diagramView.setViewOrder(1);
        editorView.setViewOrder(-1);
    }

    public void setController(AppController controller) {
        setOnKeyPressed(controller::anyKeyPress);
    }
}
