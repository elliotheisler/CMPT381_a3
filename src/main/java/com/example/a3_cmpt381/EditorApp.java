package com.example.a3_cmpt381;

import com.example.a3_cmpt381.view.MainUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class EditorApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Pane root = new MainUI();
        Scene scene = new Scene(root, 1000, 900);

        stage.setScene(scene);
        stage.setTitle("Hello!");
        String css = getClass().getResource("style.css").toExternalForm();
        scene.getStylesheets().add(Misc.getResource(getClass(), "style.css"));

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}