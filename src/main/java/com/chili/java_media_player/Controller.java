package com.chili.java_media_player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections; //imported here to add a shuffle, can remove later if shuffle not required
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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
    public Label playlistStatusLabel;
    @FXML
    private Slider volumeSlider;

    @FXML
    private Label welcomeText;
    @FXML
    private ProgressIndicator testProgressBar;

    private AnimationTimer testProgressBarTimer;
    private AnimationTimer autoPlayTimer;
    private long startTime;
    private AudioPlayerInterface audio_player;
    private boolean isPaused = false;
    private Stage settingsStage;
    private Stage aboutStage;

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

        this.audio_player = new JMPAudioPlayer();

        // Initialize auto-play timer
        initializeAutoPlayTimer();
        initializeAudio();
        initializeVolumeControl();
    }

    /**
     * Initialize the auto-play timer that checks if current track finished and plays next.
     */
    private void initializeAutoPlayTimer() {
        this.autoPlayTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Only auto-advance if NOT paused and playback stopped
                if (!isPaused && !audio_player.currentlyPlaying() && !audio_player.getPlaylist().isEmpty()) {
                    if (audio_player.getCurrentTrack() != null) {
                        String nextTrack = audio_player.nextTrack();
                        if (nextTrack != null) {
                            updatePlaylistDisplay();
                        } else {
                            // At the end of playlist, loop back to the beginning
                            Playlist playlist = audio_player.getPlaylist();
                            if (!playlist.isEmpty()) {
                                // Reset to first track
                                while (playlist.getCurrentIndex() > 0) {
                                    playlist.getPreviousTrack();
                                }
                                audio_player.load(playlist.getCurrentTrack());
                                audio_player.play();
                                updatePlaylistDisplay();
                            }
                        }
                    }
                }
            }
        };
        autoPlayTimer.start();
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
        welcomeList.add("Hello it is me JMD");
        welcomeList.add("JMD but with an actual audio player");
        Collections.shuffle(welcomeList);

        welcomeText.setText(welcomeList.get(0));

    }

    @FXML

    // DEBUG, FILL FUNCTION IN LATER
    // STATUS LABEL IS JUST TO SHOW THAT THE ID LOADED IN hello-view.fxml CAN
    // INTERACT WITH CONTROLLER
    // NO FUNCTIONALITY, ONLY TEXT CHANGES FOR STATUS LABEL.
    public void onPlayClick(ActionEvent actionEvent) {
        if (playButton.getText().equals("Play")) {
            playButton.setText("Pause");
            statusLabel.setText("The media has started playing");
            isPaused = false;
            this.audio_player.play();
        } else {
            playButton.setText("Play");
            statusLabel.setText("The media has stopped");
            isPaused = true;
            this.audio_player.pause();
        }
    }

    // So far I have two ideas,
    // one where replay just sets the timer all the way back to 00:00, might as well just be restart
    // two where it is a toggleable feature that detects if the timer has reached
    // the audio file's max time and sets it back to zero
    public void replayClick(ActionEvent actionEvent) {
        statusLabel.setText("Replayed");
    }

    // basic shuffle, play list must be implemented to work on this
    public void shuffleClick(ActionEvent actionEvent) {
        statusLabel.setText("Shuffled");
    }

    public void onFileOpen(ActionEvent actionEvent) {
        statusLabel.setText("Opening file...");
    }

    public void onFileExit(ActionEvent actionEvent) {
        statusLabel.setText("Closing file...");
    }

    // Controller.java (Modified method)
    public void onSettingsPreferences(ActionEvent actionEvent) {
        if (settingsStage != null && settingsStage.isShowing()) {
            // If window is already open, bring it to focus
            settingsStage.toFront();
            statusLabel.setText("Settings window brought to front");
            return;
        }

        try {
            Parent root = FXMLLoader.load(JavaMediaPlayer.class.getResource("settingsMenu.fxml"));

            // 1. Create and store the stage if it's the first time
            if (settingsStage == null) {
                settingsStage = new Stage();
                settingsStage.setTitle("JMP Settings");
                Scene scene = new Scene(root, 550, 550);
                scene.getStylesheets()
                        .add(JavaMediaPlayer.class.getResource("style/settings.css").toExternalForm());
                settingsStage.setScene(scene);
            }

            // 2. Show the stage (or re-show if it was previously hidden/closed)
            settingsStage.show();
            statusLabel.setText("Settings window opened");

        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error opening settings");
        }
    }

    public void onSettingsAbout(ActionEvent actionEvent) {
        // Check if the About window is already open and bring it to focus
        if (aboutStage != null && aboutStage.isShowing()) {
            aboutStage.toFront();
            statusLabel.setText("About window brought to front");
            return;
        }

        try {
            Parent root = FXMLLoader.load(JavaMediaPlayer.class.getResource("about.fxml"));

            // Create and store the stage if it's the first time
            if (aboutStage == null) {
                aboutStage = new Stage();
                aboutStage.setTitle("JMP Settings");
                Scene scene = new Scene(root, 250, 140);
                scene.getStylesheets()
                        .add(JavaMediaPlayer.class.getResource("style/settings.css").toExternalForm());
                aboutStage.setScene(scene);
            }

            // Show the stage
            aboutStage.show();
            statusLabel.setText("About window opened");

        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error opening About window");
        }
    }

    // Audio drop in detection, no functionality for now but detects .mp3 and .waw
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
                    // Check if it's an audio file by extension
                    if (file.getName().endsWith(".mp3") || file.getName().endsWith(".wav")) {
                        System.out.println("Audio file detected!");
                        this.audio_player.addToPlaylist(file.getAbsolutePath());
                    } else {
                        System.out.println("Audio file not detected!");
                    }
                }
                success = true;
                updatePlaylistDisplay();
                // Update button state to reflect that audio is loaded and playing
                playButton.setText("Pause");
                isPaused = false;
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


    /**
     * Update the display to show current track information from playlist.
     */
    private void updatePlaylistDisplay() {
        Playlist playlist = this.audio_player.getPlaylist();
        if (!playlist.isEmpty()) {
            String currentTrack = playlist.getCurrentTrack();
            int currentIndex = playlist.getCurrentIndex();
            int totalTracks = playlist.getSize();
            playlistStatusLabel.setText("Now Playing: " + (currentIndex + 1) + "/" + totalTracks + " - " + 
                    new java.io.File(currentTrack).getName());
        } else {
            playlistStatusLabel.setText("No tracks loaded");
        }
    }


    //next track button, has a check to see if the next is empty
    public void nextTrackClick(ActionEvent actionEvent) {
        String nextTrack = this.audio_player.nextTrack();
        if (nextTrack != null) {
            updatePlaylistDisplay();
        } else {
            statusLabel.setText("No more tracks in playlist");
        }
    }

    //next track, but reverse
    public void previousTrackClick(ActionEvent actionEvent) {
        String prevTrack = this.audio_player.previousTrack();
        if (prevTrack != null) {
            updatePlaylistDisplay();
        } else {
            statusLabel.setText("No previous tracks");
        }
    }

    /**
     * Remove the current track from the playlist.
     */
    public void removeTrackClick(ActionEvent actionEvent) {
        Playlist playlist = this.audio_player.getPlaylist();
        if (playlist.isEmpty()) {
            statusLabel.setText("Playlist is empty");
            return;
        }
        
        int currentIndex = playlist.getCurrentIndex();
        this.audio_player.pause();
        playlist.removeTrack(currentIndex);
        
        if (playlist.isEmpty()) {
            statusLabel.setText("Track removed - Playlist is now empty");
            playButton.setText("Play");
        } else {
            updatePlaylistDisplay();
            statusLabel.setText("Track removed");
        }
    }

    /**
     * Clear all tracks from the playlist.
     */
    public void clearPlaylistClick(ActionEvent actionEvent) {
        this.audio_player.pause();
        this.audio_player.getPlaylist().clear();
        statusLabel.setText("Playlist cleared");
        playButton.setText("Play");
    }
}
