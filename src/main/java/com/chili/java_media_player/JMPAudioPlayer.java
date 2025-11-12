package com.chili.java_media_player;

import java.nio.file.Paths;

import javafx.scene.media.AudioClip;

/**
 * Main class in charge of back-end audio functions.
 * Currently just uses the AudioClip class to play one song, but I suspect it will 
 * probably be better to have audio functions in its own class now then have to do
 * a lot of refactoring
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
