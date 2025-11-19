package com.chili.java_media_player;

import com.chili.java_media_player.visualizer.Visualizer;

public interface AudioPlayerInterface {
    public void load(String audio);

    public void play();

    public void pause();

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
}
