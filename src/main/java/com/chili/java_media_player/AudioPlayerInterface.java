package com.chili.java_media_player;

import java.util.function.BooleanSupplier;

/**
 * Interface for JMP AudioPlayer Classes
 */
public interface AudioPlayerInterface {
    public void load(String audio);

    public void play();

    public void pause();

    public String getCurrentTrack();

    public boolean currentlyPlaying();

    // public void setVolume(float volume);

    // public void removeFromQueue(float volume);
}
