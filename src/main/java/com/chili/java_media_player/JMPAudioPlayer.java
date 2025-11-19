package com.chili.java_media_player;

import java.nio.file.Paths;

import com.chili.java_media_player.visualizer.Visualizer;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class JMPAudioPlayer implements AudioPlayerInterface {

    private MediaPlayer player;
    private String current_track_path;
    private final Playlist playlist;
    private double currentVolume = 0.5; // Default volume at 50%
    // Store the onEndOfMedia handler
    private Runnable onTrackEndHandler;
    private Visualizer listener;

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
        // Stop previous player if exists
        if (this.player != null) {
            this.player.dispose();
        }
        Media media = new Media(Paths.get(audio).toUri().toString());
        this.player = new MediaPlayer(media);
        setupSpectrum();
        this.current_track_path = audio;
        setVolume(currentVolume * 100);
        // Always set the onEndOfMedia handler if present
        if (this.onTrackEndHandler != null) {
            this.player.setOnEndOfMedia(this.onTrackEndHandler);
        }
    }

    @Override
    public void pause() {
        if (this.player != null) {
            this.player.stop();
        }
    }

    @Override
    public void play() {
        if (this.player != null) {
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
    public void setSpectrumListener(Visualizer visualizer) {
        // TODO Auto-generated method stub
        this.listener = visualizer;
    }
}
