package com.bpcounter;

import javafx.application.Application;
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

    @Override
    public void start(Stage stage) throws Exception {
        // Создаем простую надпись
        Label label = new Label("JavaFX работает!");

        // Размещаем надпись на панели
        StackPane root = new StackPane(label);

        // Создаем сцену размером 400x300
        Scene scene = new Scene(root, 400, 300);

        // Настраиваем окно
        stage.setTitle("Тест JavaFX");
        stage.setScene(scene);
        stage.show();
    }
}