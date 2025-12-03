package com.chili.java_media_player;

import java.nio.file.Paths;

import com.chili.java_media_player.visualizer.Visualizer;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class JMPAudioPlayer implements AudioPlayerInterface {

    private MediaPlayer player;
    private String current_track_path;
    private final Playlist playlist;
    private double currentVolume = 0.5; // Default volume at 50%
    private double currentSpeed = 1.0;
    // Store the onEndOfMedia handler
    private Runnable onTrackEndHandler;
    public Media media;
    private javafx.collections.MapChangeListener<String, Object> metadataListener;
    private Visualizer listener;
    private Duration currentTime;

    // private SpectrumDataListener listener;

    public JMPAudioPlayer() {
        this.playlist = new Playlist();
        this.player = null;
        this.current_track_path = null;

    }

    /**
     * Links this Audio Player to the Visualizer Class so that it can recieve
     * waveform data whenever it is generated
     */
    private void setupSpectrum() {
        player.setAudioSpectrumListener((ts, dur, mags, phases) -> {
            if (listener != null) {
                listener.onSpectrum(ts, dur, mags, phases);
            }
        });
    }

    public void setOnTrackEnd(Runnable onTrackEnd) {
        this.onTrackEndHandler = onTrackEnd;
        if (this.player != null) {
            this.player.setOnEndOfMedia(onTrackEnd);
        }
    }

    @Override
    public void load(String audio) {
        if (currentTime == null || this.currentTime.toSeconds() > 0)
            this.currentTime = new Duration(0);
        // Stop previous player if exists
        if (this.player != null) {
            // Remove listener from old media obj
            if (this.media != null && this.metadataListener != null) {
                this.media.getMetadata().removeListener(this.metadataListener);
            }
            this.player.dispose();
        }
        this.media = new Media(Paths.get(audio).toUri().toString());
        this.player = new MediaPlayer(media);
        setupSpectrum();
        this.current_track_path = audio;
        setVolume(currentVolume * 100);
        setSpeed(currentSpeed);
        // Always set the onEndOfMedia handler if present

        if (this.onTrackEndHandler != null) {
            this.player.setOnEndOfMedia(this.onTrackEndHandler);
        }
        if (this.metadataListener != null) {
            this.media.getMetadata().addListener(this.metadataListener);
        }
    }

    @Override
    public void pause() {
        if (this.player != null) {
            this.currentTime = this.player.getCurrentTime();
            this.player.stop();
        }
    }

    @Override
    public void play() {
        if (this.player != null) {
            this.player.setStartTime(currentTime);
            this.player.play();
        }
    }

    @Override
    public String getCurrentTrack() {
        return this.current_track_path;
    }

    @Override
    public boolean currentlyPlaying() {
        if (this.player == null) {
            return false;
        }
        return this.player.getStatus() == MediaPlayer.Status.PLAYING;
    }

    @Override
    public void addToPlaylist(String audio) {
        this.playlist.addTrack(audio);
        // Auto-load the first track added to the playlist
        if (this.playlist.getSize() == 1) {
            load(audio);
        }
    }

    @Override
    public String nextTrack() {
        String nextTrackPath = this.playlist.getNextTrack();
        if (nextTrackPath != null) {
            load(nextTrackPath);
        }
        return nextTrackPath;
    }

    @Override
    public String previousTrack() {
        String previousTrackPath = this.playlist.getPreviousTrack();
        if (previousTrackPath != null) {
            load(previousTrackPath);
        }
        return previousTrackPath;
    }

    @Override
    public Playlist getPlaylist() {
        return this.playlist;
    }

    @Override
    public void setVolume(double volume) {
        // Normalize volume from 0-100 range to 0.0-1.0 range
        double normalizedVolume = Math.max(0.0, Math.min(1.0, volume / 100.0));
        // Store the volume for future audio loads
        this.currentVolume = normalizedVolume;

        // Apply volume to currently loaded audio
        if (this.player != null) {
            this.player.setVolume(normalizedVolume);
        }
    }

    // needed for testing when getting volume
    public double getCurrentVolume() {
        return this.currentVolume;
    }

    @Override
    public void setMetadataListener(javafx.collections.MapChangeListener<String, Object> listener) {
        this.metadataListener = listener;
        // Apply to current player if exists (although load() should handle it)
        if (this.media != null && this.metadataListener != null) {
            this.media.getMetadata().addListener(this.metadataListener);
        }
    }

    public void setSpeed(double speed) {
        this.currentSpeed = speed;
        if (this.player != null) {
            this.player.setRate(speed);
        }
    }

    @Override
    public void setSpectrumListener(Visualizer visualizer) {
        // TODO Auto-generated method stub
        this.listener = visualizer;
    }

    @Override
    public void setupSeekBar() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'setupSeekBar'");

    }
}
