package com.chili.java_media_player;


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

    // public void removeFromQueue(float volume);
}
