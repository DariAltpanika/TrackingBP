package com.bpcounter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // 1. Загружаем FXML файл
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/bpcounter/view/main-view.fxml")
        );

        // 2. Создаем сцену
        Scene scene = new Scene(loader.load(), 800, 600);

        // 3. Настраиваем окно
        primaryStage.setTitle("BPCounter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}