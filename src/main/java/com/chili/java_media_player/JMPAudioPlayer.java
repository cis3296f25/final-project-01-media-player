package com.chili.java_media_player;

import java.nio.file.Paths;

import javafx.scene.media.AudioClip;

/**
 * Main class in charge of back-end audio functions.
 * Uses AudioClip to play audio tracks and supports playlist functionality.
 */
public class JMPAudioPlayer implements AudioPlayerInterface {

    private AudioClip player;
    private String current_track_path;
    private final Playlist playlist;

    /**
     * Initialize JMPAudioPlayer with an empty playlist.
     */
    public JMPAudioPlayer() {
        this.playlist = new Playlist();
        this.player = null;
        this.current_track_path = null;
    }

    @Override
    public void load(String audio) {
        this.player = new AudioClip(Paths.get(audio).toUri().toString());
        this.current_track_path = audio;
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
        return this.player.isPlaying();
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
            pause(); // Stop current track
            load(nextTrackPath);
            play(); // Auto-play the next track
        }
        return nextTrackPath;
    }

    @Override
    public String previousTrack() {
        String previousTrackPath = this.playlist.getPreviousTrack();
        if (previousTrackPath != null) {
            pause(); // Stop current track
            load(previousTrackPath);
            play(); // Auto-play the previous track
        }
        return previousTrackPath;
    }

    @Override
    public Playlist getPlaylist() {
        return this.playlist;
    }
}

