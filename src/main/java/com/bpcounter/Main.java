package com.bpcounter;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        Label label = new Label("JavaFX работает!");
        StackPane root = new StackPane(new Node[]{label});
        Scene scene = new Scene(root, 400.0, 300.0);
        stage.setTitle("Тест JavaFX");
        stage.setScene(scene);
        stage.show();
    }
}