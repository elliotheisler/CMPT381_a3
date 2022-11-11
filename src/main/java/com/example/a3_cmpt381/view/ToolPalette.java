package com.example.a3_cmpt381.view;

import com.example.a3_cmpt381.AppController;
import com.example.a3_cmpt381.model.InteractionModel;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

public class ToolPalette extends StackPane implements ModelListener {

    private final static double WIDTH = 40;
    private final static double HEIGHT = 40;

    private final static double SELECT_SCALE = 1.2;

    private Button pointButton = createButton("icons8-cursor-100.png");
    private Button moveButton = createButton("icons8-drag-100.png");
    private Button linkButton = createButton("icons8-plus-math-100.png");

    public void setIModel(InteractionModel iModel) {
        this.iModel = iModel;
    }

    private InteractionModel iModel;

    public void modelChanged(Class<?> c) {
        if (c == InteractionModel.class) {
            resetButtonScales();
            switch(iModel.getCursorMode()) {
                case DRAG:
                    getScene().setCursor(Cursor.DEFAULT);
                    pointButton.setScaleX(SELECT_SCALE);
                    pointButton.setScaleY(SELECT_SCALE);
                    break;
                case PAN:
                    getScene().setCursor(Cursor.W_RESIZE);
                    moveButton.setScaleX(SELECT_SCALE);
                    moveButton.setScaleY(SELECT_SCALE);
                    break;
                case LINK:
                    getScene().setCursor(Cursor.CROSSHAIR);
                    linkButton.setScaleX(SELECT_SCALE);
                    linkButton.setScaleY(SELECT_SCALE);
                    break;
            }
        }
    }

    public void setController(AppController c) {
        pointButton.setOnAction(e -> c.selectPointer(e));
        linkButton.setOnAction(e -> c.selectLink(e));
        moveButton.setOnAction(e -> c.selectMove(e));
    }

    private void resetButtonScales() {
        pointButton.setScaleX(1);
        linkButton.setScaleX(1);
        moveButton.setScaleX(1);
        pointButton.setScaleY(1);
        linkButton.setScaleY(1);
        moveButton.setScaleY(1);
    }

    private static Button createButton(String iconPath) {
        Button b = new Button();

        ImageView img = new ImageView(new Image(iconPath));
        img.setPreserveRatio(true);
        // this seems to make it scale with the button scale...
        img.setFitWidth(WIDTH);
        img.setFitHeight(HEIGHT);
        // this however, does not
//        img.fitWidthProperty().bind(b.widthProperty());
//        img.fitHeightProperty().bind(b.heightProperty());

        b.setGraphic(img);
        b.setPrefSize(WIDTH, HEIGHT);
        b.setMinSize(WIDTH, HEIGHT);
        return b;
    }

    public ToolPalette() {
        getStyleClass().add("ToolPalette");
        VBox root = new VBox(pointButton, moveButton, linkButton);
        root.setAlignment(Pos.TOP_CENTER);
        root.getStyleClass().add("region");
        getChildren().add(root);
    }
}
