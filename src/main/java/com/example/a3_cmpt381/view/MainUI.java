package com.example.a3_cmpt381.view;

import javafx.scene.layout.StackPane;

public class MainUI extends StackPane {
    public MainUI() {
        getStyleClass().add("MainUI");
        ToolPalette t = new ToolPalette();
        getChildren().addAll(t);
    }
}
