package com.chili.java_media_player;

import java.util.ArrayList;
import java.util.Collections; //imported here to add a shuffle, can remove later if shuffle not required
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

/**
 * Controler to Hello View Screen
 * 
 **/
public class Controller {
    @FXML
    private Label welcomeText;
    @FXML
    private ProgressIndicator testProgressBar;

    private AnimationTimer testProgressBarTimer;
    private long startTime;

    /**
     * Initializes Controller, and instantiates the AnimationTimer needed for the
     * Progress Bar test.
     * 
     */
    @FXML
    private void initialize() {
        this.startTime = System.nanoTime();

        this.testProgressBarTimer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                double elapsedSeconds = (now - startTime) / 1_000_000_000.0;
                double progress = (elapsedSeconds % 5) / 5.0; // Loops every 5 seconds
                testProgressBar.setProgress(progress);
                // testProgressBar.setProgress(now(double));
            }
        };
        testProgressBarTimer.start();
    }

    /**
     * Event that occurs on button click.
     */
    @FXML
    protected void onHelloButtonClick() {

        List<String> welcomeList = new ArrayList<>();
        welcomeList.add("Welcome to Java Media Player & Visualizer!");
        welcomeList.add("Welcome to JMD!");
        Collections.shuffle(welcomeList);

        welcomeText.setText(welcomeList.get(0));

    }

}
