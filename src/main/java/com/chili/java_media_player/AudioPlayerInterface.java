package com.chili.java_media_player;

import com.chili.java_media_player.visualizer.Visualizer;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.util.Duration;

public interface AudioPlayerInterface {
    public void load(String audio);

    public void play();

    public void pause();

    public void seek(Duration timeStamp);

    public String getCurrentTrack();

    public boolean currentlyPlaying();

    public void addToPlaylist(String audio);

    public String nextTrack();

    public String previousTrack();

    public Playlist getPlaylist();

    public void setVolume(double volume);

    public void setSpeed(double speed);

    public void setSpectrumListener(Visualizer visualizer);

    // public void removeFromQueue(float volume);

    public void setMetadataListener(javafx.collections.MapChangeListener<String, Object> listener);

    public Media media = null;

    public void setSeekSlider(Slider seekSlider, Label startTime, Label lengthTime);

}
