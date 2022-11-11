package com.example.a3_cmpt381.view;

import com.example.a3_cmpt381.AppController;
import com.example.a3_cmpt381.model.InteractionModel;
import com.example.a3_cmpt381.model.SMModel;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class MainUI extends StackPane {
    private HBox root;

    public MainUI() {
        getStyleClass().add("MainUI");

        DiagramView diagramView = new DiagramView();
        ToolPalette toolPalette = new ToolPalette();

        AppController controller = new AppController();

        InteractionModel iModel = new InteractionModel();
        SMModel smModel = new SMModel();

        toolPalette.setController(controller);
        toolPalette.setIModel(iModel);

        diagramView.setController(controller);
        diagramView.setSMModel(smModel);
        diagramView.setIModel(iModel);

        controller.setIModel(iModel);
        controller.setSMModel(smModel);

        iModel.addSubscribers(toolPalette, diagramView);
        smModel.addSubscribers(diagramView);

        root = new HBox();
        root.getChildren().addAll(toolPalette, diagramView);
        getChildren().add(root);
        diagramView.setViewOrder(1);
    }
}
