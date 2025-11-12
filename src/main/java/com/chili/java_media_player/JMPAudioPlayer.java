package com.chili.java_media_player;

import java.nio.file.Paths;

import javafx.scene.media.AudioClip;

/**
 * Main class in charge of back-end audio functions.
 * Currently just uses the AudioClip class to play one song, but I suspect it
 * will probably be better to have audio functions in its own class now then
 * have to do a lot of refactoring
 */
public class JMPAudioPlayer implements AudioPlayerInterface {

    private AudioClip player;
    private String current_track_path;

    @Override
    public void load(String audio) {
        this.player = new AudioClip(Paths.get(audio).toUri().toString());
        this.current_track_path = audio;

    }

    @Override
    public void pause() {
        this.player.stop();
    }

    @Override
    public void play() {
        this.player.play();
    }

    @Override
    public String getCurrentTrack() {
        return this.current_track_path;
    }

    @Override
    public boolean currentlyPlaying() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'currentlyPlaying'");
        return this.player.isPlaying();
    }

}
