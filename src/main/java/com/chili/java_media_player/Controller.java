package com.chili.java_media_player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.chili.java_media_player.visualizer.Visualizer;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.animation.AnimationTimer; //imported here to add a shuffle, can remove later if shuffle not required
import javafx.application.Platform;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


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
    @FXML
    private Slider speedSlider;

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
    private ImageView albumArtView;
    @FXML
    private Label albumArtPlaceholder;
    @FXML
    private ListView<String> metaDataListView;


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
    // replayOn: when true, auto-play is disabled and replay behaviour is active
    private boolean replayOn = false;
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
            // If replay mode is ON, restart the same track instead of advancing
            if (replayOn) {
                lastNavigationTime = System.currentTimeMillis();
                String trackPath = playlist.getCurrentTrack();
                if (trackPath != null) {
                    audio_player.load(trackPath);
                    audio_player.play();
                    updatePlaylistDisplay();
                }
                return;
            }

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

        MapChangeListener<String, Object> metadataListener = change -> {
            // check if media is loaded before trying to get its metadata map
            javafx.scene.media.Media currentMedia = ((JMPAudioPlayer) this.audio_player).media;

            if (currentMedia != null) {
                // Runs whenever a track changes (or more specifically if the metadata changes)
                updateMetaDataDisplay(currentMedia.getMetadata());
            }
        };
        this.audio_player.setMetadataListener(metadataListener);
        initializeAutoPlayTimer();
        initializeAudio();
        initializeVolumeControl();
        initializeSpeedControl();
        // Ensure replay button reflects default state
        if (replayButton != null) {
            replayButton.setText("Replay: Off");
        }
        // this.visualizerCanvas = new Canvas();
        this.visualizer = new Visualizer(audio_player, visualizerCanvas);
    }

    private void initializeAutoPlayTimer() {
        this.autoPlayTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // If replay toggle is enabled, auto-advance should be disabled
                if (replayOn) {
                    return;
                }
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

    public void onPlayClick() {
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
    public void replayClick() {
        // Toggle replay mode. When replay is ON, auto-play is disabled.
        replayOn = !replayOn;
        if (replayOn) {
            replayButton.setText("Replay: On");
            statusLabel.setText("Replay enabled — autoplay disabled");
        } else {
            replayButton.setText("Replay: Off");
            // debounce so autoplay doesn't immediately trigger after turning it back on
            lastNavigationTime = System.currentTimeMillis();
            statusLabel.setText("Replay disabled — autoplay enabled");
        }
    }

    // basic shuffle, play list must be implemented to work on this
    public void shuffleClick() {
        Playlist playlist = this.audio_player.getPlaylist();
        if (playlist == null || playlist.isEmpty() || playlist.getSize() < 2) {
            statusLabel.setText("Not enough tracks to shuffle");
            return;
        }
        // Shuffle the playlist and reset current position to the first track
        boolean wasPlaying = this.audio_player.currentlyPlaying();

        List<String> tracks = playlist.getAllTracks();
        Collections.shuffle(tracks);

        // Rebuild playlist in-place
        playlist.clear();
        for (String t : tracks) {
            playlist.addTrack(t);
        }

        // Reset to first track
        playlist.resetToFirstTrack();

        // Debounce navigation so autoplay doesn't immediately advance
        lastNavigationTime = System.currentTimeMillis();

        // Update UI
        updatePlaylistDisplay();
        statusLabel.setText("Playlist shuffled and reset to first track");

        // If it was playing, load and play the first track
        if (wasPlaying) {
            String nowCurrent = playlist.getCurrentTrack();
            if (nowCurrent != null) {
                this.audio_player.load(nowCurrent);
                this.audio_player.play();
                playButton.setText("Pause");
                isPaused = false;
            }
        }
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
    public void onSettingsPreferences() {
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

    public void onSettingsAbout() {
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

    @FXML
    private void initializeSpeedControl() {
        audio_player.setSpeed(speedSlider.getValue());

        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double speed = newValue.doubleValue();
            audio_player.setSpeed(speed);
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
    public void nextTrackClick() {
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
    public void previousTrackClick() {
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

    public void removeTrackClick() {
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

    public void clearPlaylistClick() {
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

    /*
    *   Controller function for the metadata and album art
    **/
    public void updateMetaDataDisplay(ObservableMap<String, Object> metadata) {
        // ==== Album Art ====
        Platform.runLater(() -> {
            if (metadata != null && metadata.containsKey("image")) {
                // The "image" key returns an Image object
                Image albumArt = (Image) metadata.get("image");
                if (albumArt != null) {
                    albumArtView.setImage(albumArt);
                } else {
                    albumArtView.setImage(null);
                }
            } else {
                albumArtView.setImage(null);
            }
        });

        // ==== Metadata List View ====
        List<String> formattedMetadata = processMetadataForDisplay(metadata);

        // Update UI
        Platform.runLater(() -> {
            metaDataListView.getItems().clear();
            metaDataListView.getItems().addAll(formattedMetadata);
        });
    }


    public List<String> processMetadataForDisplay(ObservableMap<String, Object> metadata) {
        List<String> formattedList = new ArrayList<>();

        if (metadata == null) {
            formattedList.add("No Metadata Available");
            return formattedList;
        }

        // Define the list of fields to display: [Display Label, JavaFX Key]
        String[][] fieldsToDisplay = {
                {"Artist: ", "artist"},
                {"Track Title: ", "title"},
                {"Album: ", "album"},
                {"Date: ", "year"},
                {"Track Number: ", "track"},
                {"Comment: ", "comment-0"}
        };


        for (String[] field : fieldsToDisplay) {
            String label = field[0];
            String key = field[1];
            String value = "";
            // Get value or an empty string if missing
            Object tempValue = metadata.get(key);
            if (tempValue != null) {
                // Special handling for numerical values and general toString
                value = tempValue.toString();
            } else {
                value = ""; // empty value if there's no metadata
            }

            // Hack to deal with the language options in comments
            // will only remove the first one if there's multiple languages (rare)
            // old itunes mp3s are bugged out
            if (key.equals("comment-0")) {
                // matches if string starts with "[eng]=", the "eng" can be any 3 chars
                String regex = "^\\[.{3}\\]=.*";
                if (java.util.regex.Pattern.matches(regex, value)) {
                    // The prefix is 5 characters long: [ + 3 chars + ] + =
                    value = value.substring(6);
                }
            }


            // Format: "Key: Value" (e.g., "Artist: John Jones")
            String displayString = label + value;
            formattedList.add(displayString);
        }

        return formattedList;
    }
}