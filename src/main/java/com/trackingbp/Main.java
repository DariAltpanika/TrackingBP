package com.trackingbp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // 1. Загружаем FXML файл
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/trackingbp/view/main-view.fxml")
        );

        // 2. Создаем сцену
        Scene scene = new Scene(loader.load(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("/com/trackingbp/view/style.css").toExternalForm());

        // 3. Настраиваем окно
        primaryStage.setTitle("TrackingBP");

        primaryStage.getIcons().add(new Image("/miniIcon.png"));

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setMaximized(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Throwable t) {
            t.printStackTrace();
            System.out.println("Нажмите Enter для выхода...");
            new java.util.Scanner(System.in).nextLine();
        }

    }
}