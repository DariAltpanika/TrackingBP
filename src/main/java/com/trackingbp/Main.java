package com.trackingbp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.InputStream;
import java.net.URL;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // 1. Загружаем FXML
        URL fxmlUrl = getClass().getResource("/com/trackingbp/view/main-view.fxml");
        if (fxmlUrl == null) {
            System.err.println("FXML not found!");
            return;
        }
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        primaryStage.initStyle(StageStyle.TRANSPARENT);

        // 2. Создаем сцену
        Scene scene = new Scene(root, 800, 600);
        scene.setFill(Color.TRANSPARENT);

        // 3. Загружаем CSS
        URL cssUrl = getClass().getResource("/com/trackingbp/view/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        // 4. Настройки окна
        primaryStage.setTitle("TrackingBP v1.1.21");

        //тест

        // 5. Загружаем иконку
        InputStream iconStream = getClass().getResourceAsStream("/miniIcon.png");
        if (iconStream != null) {
            primaryStage.getIcons().add(new Image(iconStream));
        }

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}