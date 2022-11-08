package com.example.a3_cmpt381.view;

import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

public class ToolPalette extends StackPane {
    private final static float WIDTH = 20;
    private final static float HEIGHT = 20;

    private Button pointButton = createButton("icons8-cursor-100.png");
    private Button moveButton = createButton("icons8-drag-100.png");
    private Button linkButton = createButton("icons8-plus-math-100.png");

    public ToolPalette() {
        getStyleClass().add("ToolPalette");
        VBox root = new VBox(pointButton, moveButton, linkButton, new Label("asdf"));
        getChildren().add(root);
    }

    private static Button createButton(String iconPath) {
        Button b = new Button();

        ImageView img = new ImageView(new Image(iconPath));
        img.setPreserveRatio(true);
        img.setFitWidth(WIDTH);
        img.setFitHeight(HEIGHT);
//        img.fitWidthProperty().bind(b.widthProperty());
//        img.fitHeightProperty().bind(b.heightProperty());

        b.setGraphic(img);
        return b;
    }
}
