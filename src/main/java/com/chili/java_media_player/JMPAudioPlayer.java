package com.chili.java_media_player;

import java.nio.file.Paths;

import javafx.scene.media.AudioClip;

/**
 * Main class in charge of back-end audio functions.
 * Currently just uses the AudioClip class to play one song, but I
 */
public class JMPAudioPlayer implements AudioPlayerInterface {

    private AudioClip player;

    @Override
    public void load(String audio) {
        this.player = new AudioClip(Paths.get(audio).toUri().toString());

    }

    @Override
    public void pause() {
        this.player.stop();
    }

    @Override
    public void play() {
        this.player.play();
    }

}
