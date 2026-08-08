package Application.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BillingController {
    @FXML
    private Label DateTimeLabel;

    @FXML
    public void initialize() {

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        e -> updateDateTime()
                ),
                new KeyFrame(Duration.seconds(1))
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateDateTime() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd MMM yyyy | hh:mm:ss a");

        DateTimeLabel.setText(
                LocalDateTime.now().format(formatter)
        );
    }
}