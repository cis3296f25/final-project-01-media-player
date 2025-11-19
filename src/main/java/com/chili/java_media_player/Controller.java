package com.chili.java_media_player;

import java.io.IOException;
import java.util.List;

import com.chili.java_media_player.visualizer.Visualizer;

import javafx.animation.AnimationTimer; //imported here to add a shuffle, can remove later if shuffle not required
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Controller {
    @FXML
    private javafx.scene.control.ListView<String> playlistListView;
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

    // Potential work on for spectro, meta, credit, and playback functions
    @FXML
    private javafx.scene.layout.StackPane spectrogramBox;
    @FXML
    private javafx.scene.control.Label spectrogramLabel;
    @FXML
    private javafx.scene.layout.StackPane metaDataBox;
    @FXML
    private javafx.scene.control.Label metaDataLabel;
    @FXML
    private javafx.scene.layout.StackPane exportCreditsBox;
    @FXML
    private javafx.scene.control.Label exportCreditsLabel;
    @FXML
    private javafx.scene.layout.VBox playbackSpeedBox;
    @FXML
    private javafx.scene.control.Label playbackSpeedLabel;

    @FXML
    private Label welcomeText;
    // @FXML
    // private ProgressIndicator testProgressBar;

    @FXML
    private Canvas visualizerCanvas;

    private AnimationTimer testProgressBarTimer;
    private AnimationTimer autoPlayTimer;
    private AudioPlayerInterface audio_player;
    private boolean isPaused = false;
    private long lastNavigationTime = 0; // Track when user last manually navigated
    private static final long NAVIGATION_DEBOUNCE_MS = 500; // Debounce time in milliseconds

    private Stage settingsStage;
    private Stage aboutStage;
    private Visualizer visualizer;

    // I have no idea how this code works, it's not even being used but it is
    // literally the backbone of everything in this code
    @FXML
    private void initialize() {

        this.audio_player = new JMPAudioPlayer();

        ((JMPAudioPlayer) this.audio_player).setOnTrackEnd(() -> {
            Playlist playlist = audio_player.getPlaylist();
            if (playlist.hasNextTrack()) {
                lastNavigationTime = System.currentTimeMillis();
                playlist.getNextTrack();
                String trackPath = playlist.getCurrentTrack();
                audio_player.load(trackPath);
                audio_player.play();
                updatePlaylistDisplay();
            } else if (!playlist.isEmpty()) {
                lastNavigationTime = System.currentTimeMillis();
                playlist.resetToFirstTrack();
                String trackPath = playlist.getCurrentTrack();
                audio_player.load(trackPath);
                audio_player.play();
                updatePlaylistDisplay();
            }
        });

        initializeAutoPlayTimer();
        initializeAudio();
        initializeVolumeControl();
        // this.visualizerCanvas = new Canvas();
        this.visualizer = new Visualizer(audio_player, visualizerCanvas);

    }

    private void initializeAutoPlayTimer() {
        this.autoPlayTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Check if user recently navigated (debounce period)
                long timeSinceNavigation = System.currentTimeMillis() - lastNavigationTime;
                if (timeSinceNavigation < NAVIGATION_DEBOUNCE_MS) {
                    return; // Skip auto-play during debounce period
                }

                // Only auto-advance if NOT paused and playback stopped
                if (!isPaused && !audio_player.currentlyPlaying() && !audio_player.getPlaylist().isEmpty()) {
                    Playlist playlist = audio_player.getPlaylist();

                    // Check if there's a next track
                    if (playlist.hasNextTrack()) {
                        // Advance playlist index
                        playlist.getNextTrack();
                        String trackPath = playlist.getCurrentTrack();
                        audio_player.load(trackPath);
                        audio_player.play();
                        updatePlaylistDisplay();
                    } else {
                        // At the end of playlist, loop back to the beginning
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
        };
        autoPlayTimer.start();
    }

    @FXML

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
    // one where replay just sets the timer all the way back to 00:00, might as well
    // just be restart
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
        statusLabel.setText("Exiting...");
        Platform.exit();
        System.exit(0);
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
                aboutStage.setTitle("About");
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
        // Set initial volume
        audio_player.setVolume(volumeSlider.getValue());

        // Listen for volume slider changes
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double volume = newValue.doubleValue();
            audio_player.setVolume(volume);
            statusLabel.setText("Volume: " + (int) volume + "%");
        });
    }

    private void updatePlaylistDisplay() {
        Playlist playlist = this.audio_player.getPlaylist();
        // Update the ListView with the current playlist
        if (playlistListView != null) {
            playlistListView.getItems().clear();
            List<String> tracks = playlist.getAllTracks();
            for (int i = 0; i < tracks.size(); i++) {
                String name = new java.io.File(tracks.get(i)).getName();
                playlistListView.getItems().add((i + 1) + ". " + name);
            }
            // Highlight the current track
            int currentIndex = playlist.getCurrentIndex();
            if (currentIndex >= 0 && currentIndex < playlistListView.getItems().size()) {
                playlistListView.getSelectionModel().select(currentIndex);
                playlistListView.scrollTo(currentIndex);
            }
        }
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

    // next track button, has a check to see if the next is empty
    public void nextTrackClick(ActionEvent actionEvent) {
        lastNavigationTime = System.currentTimeMillis(); // Set debounce timer
        String nextTrack = this.audio_player.nextTrack();
        if (nextTrack != null) {
            this.audio_player.play();
            isPaused = false;
            playButton.setText("Pause");
            updatePlaylistDisplay();
        } else {
            statusLabel.setText("No more tracks in playlist");
        }
    }

    // next track, but reverse
    public void previousTrackClick(ActionEvent actionEvent) {
        lastNavigationTime = System.currentTimeMillis(); // Set debounce timer
        String prevTrack = this.audio_player.previousTrack();
        if (prevTrack != null) {
            this.audio_player.play();
            isPaused = false;
            playButton.setText("Pause");
            updatePlaylistDisplay();
        } else {
            statusLabel.setText("No previous tracks");
        }
    }

    public void removeTrackClick(ActionEvent actionEvent) {
        Playlist playlist = this.audio_player.getPlaylist();
        if (playlist.isEmpty()) {
            statusLabel.setText("Playlist is empty");
            updatePlaylistDisplay();
            return;
        }

        int currentIndex = playlist.getCurrentIndex();
        boolean wasPlaying = this.audio_player.currentlyPlaying();
        this.audio_player.pause();
        playlist.removeTrack(currentIndex);
        // Debounce: prevent autoplay/timer from bouncing after removal
        lastNavigationTime = System.currentTimeMillis();

        // Ensure current index is valid after removal
        int newIndex = playlist.getCurrentIndex();
        if (!playlist.isEmpty() && (newIndex < 0 || newIndex >= playlist.getSize())) {
            // If index is out of bounds, set to last valid index
            int lastIndex = playlist.getSize() - 1;
            if (lastIndex >= 0) {
                // Directly set currentIndex if possible, or use resetToFirstTrack if that's the
                // only method
                playlist.resetToFirstTrack();
                // If more than one track, move to last
                for (int i = 0; i < lastIndex; i++) {
                    playlist.getNextTrack();
                }
            }
        }

        if (playlist.isEmpty()) {
            updatePlaylistDisplay();
            statusLabel.setText("Track removed - Playlist is now empty");
            playButton.setText("Play");
        } else {
            // Reload the current track and update UI
            String trackPath = playlist.getCurrentTrack();
            if (trackPath != null) {
                this.audio_player.load(trackPath);
                updatePlaylistDisplay();
                statusLabel.setText("Track removed");
                if (wasPlaying) {
                    this.audio_player.play();
                    playButton.setText("Pause");
                }
            }
        }
    }

    public void clearPlaylistClick(ActionEvent actionEvent) {
        this.audio_player.pause();
        this.audio_player.getPlaylist().clear();
        updatePlaylistDisplay();
        statusLabel.setText("Playlist cleared");
        playButton.setText("Play");
    }

    // POTENTIAL STUFF FOR FUTURE UI ELEMENTS
    public void updateSpectrogram() {
    }

    public void updateMetaData() {
    }

    public void updateExportCredits() {
    }
}
