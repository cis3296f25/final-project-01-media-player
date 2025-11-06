package com.chili.java_media_player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections; //imported here to add a shuffle, can remove later if shuffle not required
import java.util.List;

import eu.hansolo.tilesfx.Alarm;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;

import javax.swing.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

import javax.swing.*;

/**
 * Controler to Hello View Screen
 * 
 **/
public class Controller {
    @FXML
    public Button playButton;
    @FXML
    public Button replayButton;
    @FXML
    public Button shuffleButton;
    @FXML
    public Label statusLabel;
    @FXML
    private Slider volumeSlider;


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
        initializeAudio();
        initializeVolumeControl();
    }

    /**
     * Event that occurs on button click.
     */
    @FXML
    protected void onHelloButtonClick() {

        List<String> welcomeList = new ArrayList<>();
        welcomeList.add("Welcome to Java Media Player & Visualizer!");
        welcomeList.add("Welcome to JMD!");
        welcomeList.add("Hello this is JMD");
        welcomeList.add("JMD, hello this is tester");
        Collections.shuffle(welcomeList);

        welcomeText.setText(welcomeList.get(0));

    }
    @FXML
    protected void openSettings() {
        Parent root;
        try {
            root = FXMLLoader.load(JavaMediaPlayer.class.getResource("settingsMenu.fxml"));
            Stage stage = new Stage();
            stage.setTitle("JMP Settings");
            stage.setScene(new Scene(root, 550, 550));
            stage.getScene().getStylesheets().add(JavaMediaPlayer.class.getResource("style/settings.css").toExternalForm());
            stage.show();
            stage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }

    //DEBUG, FILL FUNCTION IN LATER
    //STATUS LABEL IS JUST TO SHOW THAT THE ID LOADED IN hello-view.fxml CAN INTERACT WITH CONTROLLER
    //NO FUNCTIONALITY, ONLY TEXT CHANGES FOR STATUS LABEL.
    public void onPlayClick(ActionEvent actionEvent) {
        if (playButton.getText().equals("Play")){
            playButton.setText("Pause");
            statusLabel.setText("The media has started playing");
        }
        else{
            playButton.setText("Play");
            statusLabel.setText("The media has stopped");
        }
    }
    //So far I have two ideas,
    // one where replay just sets the timer all the way back to 00:00, might as well just be restart
    // two where it is a toggleable feature that detects if the timer has reached the audio file's max time and sets it back to zero
    public void replayClick(ActionEvent actionEvent) {
        statusLabel.setText("Replayed");
    }
    //basic shuffle, play list must be implemented to work on this
    public void shuffleClick(ActionEvent actionEvent) {
        statusLabel.setText("Shuffled");
    }
    public void onFileOpen(ActionEvent actionEvent) {
        statusLabel.setText("Opening file...");
    }

    public void onFileExit(ActionEvent actionEvent) {
        statusLabel.setText("Closing file...");
    }

    public void onSettingsPreferences(ActionEvent actionEvent) {
        statusLabel.setText("Saving settings...");
    }

    public void onSettingsAbout(ActionEvent actionEvent) {
        statusLabel.setText("About");
    }


    //Audio drop in detection, no functionality for now but detects .mp3 and .waw
    @FXML
    private StackPane dropArea;
    public void initializeAudio() {
        dropArea.setOnDragOver(event -> {
            if (event.getGestureSource() != dropArea && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        dropArea.setOnDragDropped(event -> {
            var db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                for (var file : db.getFiles()) {
                    System.out.println("Dropped file: " + file.getAbsolutePath());
                    // Optional: check if it’s an audio file by extension
                    if (file.getName().endsWith(".mp3") || file.getName().endsWith(".wav")) {
                        System.out.println("Audio file detected!");
                    }
                    else{
                        System.out.println("Audio file not detected!");
                    }
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }
    @FXML
    private void initializeVolumeControl() {
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int vol = newValue.intValue();
            statusLabel.setText("Volume: " + vol + "%");
        });
    }


    }
}
