package com.trackingbp.animation;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Animations {

    //анимация прогресс бара
    public static void animateProgress(ProgressBar progressBar, double targetProgress) {
        double startProgress = progressBar.getProgress();

        if (startProgress == targetProgress) return;

        Timeline timeLine = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), startProgress)),
                new KeyFrame(Duration.millis(300), new KeyValue(progressBar.progressProperty(), targetProgress, Interpolator.EASE_BOTH)));

        timeLine.play();
    }

    //анимация показа Vbox
    public static void showVBoxGrow(VBox vbox) {
        vbox.setVisible(true);
        vbox.setScaleX(0.2);
        vbox.setScaleY(0.2);
        vbox.setOpacity(0);
        vbox.setTranslateY(80);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(vbox.scaleXProperty(), 0.2),
                        new KeyValue(vbox.scaleYProperty(), 0.2),
                        new KeyValue(vbox.opacityProperty(), 0),
                        new KeyValue(vbox.translateYProperty(), 80)),
                new KeyFrame(Duration.millis(200),
                        new KeyValue(vbox.scaleXProperty(), 1.05, Interpolator.EASE_OUT),
                        new KeyValue(vbox.scaleYProperty(), 1.05, Interpolator.EASE_OUT),
                        new KeyValue(vbox.opacityProperty(), 1),
                        new KeyValue(vbox.translateYProperty(), -5, Interpolator.EASE_OUT)),
                new KeyFrame(Duration.millis(320),
                        new KeyValue(vbox.scaleXProperty(), 1, Interpolator.EASE_IN),
                        new KeyValue(vbox.scaleYProperty(), 1, Interpolator.EASE_IN),
                        new KeyValue(vbox.translateYProperty(), 0, Interpolator.EASE_IN))
        );
        timeline.play();
    }

    //анимация сокрытия Vbox
    public static void hideVBoxShrink(VBox vbox) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                new KeyValue(vbox.scaleXProperty(), 1),
                new KeyValue(vbox.scaleYProperty(), 1),
                new KeyValue(vbox.opacityProperty(), 1),
                        new KeyValue(vbox.translateYProperty(), 0)),
                new KeyFrame(Duration.millis(200),
                        new KeyValue(vbox.scaleXProperty(), 0.4, Interpolator.EASE_IN),
                        new KeyValue(vbox.scaleYProperty(), 0.4, Interpolator.EASE_IN),
                        new KeyValue(vbox.opacityProperty(), 0, Interpolator.EASE_IN),
                        new KeyValue(vbox.translateYProperty(), 60, Interpolator.EASE_IN)));

        timeline.setOnFinished(e -> vbox.setVisible(false));
        timeline.play();
    }
}
