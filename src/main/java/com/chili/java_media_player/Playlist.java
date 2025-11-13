package com.chili.java_media_player;

import java.util.ArrayList;
import java.util.List;

//Playlist java class to manage audio play list, similar structure to JMPAudioPlayer
public class Playlist {
    private final List<String> tracks;
    private int currentIndex;


    public Playlist() {
        this.tracks = new ArrayList<>();
        this.currentIndex = -1; 
    }


    public void addTrack(String filePath) {
        tracks.add(filePath);
        if (currentIndex == -1) {
            currentIndex = 0; 
        }
    }


    public void removeTrack(int index) {
        if (index < 0 || index >= tracks.size()) {
            throw new IndexOutOfBoundsException("Invalid track index: " + index);
        }
        tracks.remove(index);
        
        if (index < currentIndex) {
            currentIndex--;
        } else if (index == currentIndex) {
            if (currentIndex >= tracks.size()) {
                currentIndex = tracks.size() - 1;
            }
        }
    }


    //gets current track
    public String getCurrentTrack() {
        if (currentIndex < 0 || currentIndex >= tracks.size()) {
            return null;
        }
        return tracks.get(currentIndex);
    }


    //gets next track
    public String getNextTrack() {
        if (currentIndex < tracks.size() - 1) {
            currentIndex++;
            return tracks.get(currentIndex);
        }
        return null;
    }


    //Gets previous track
    public String getPreviousTrack() {
        if (currentIndex > 0) {
            currentIndex--;
            return tracks.get(currentIndex);
        }
        return null;
    }


    //Next track function
    public boolean hasNextTrack() {
        return currentIndex < tracks.size() - 1;
    }


    public boolean hasPreviousTrack() {
        return currentIndex > 0;
    }


    public int getSize() {
        return tracks.size();
    }


    public int getCurrentIndex() {
        return currentIndex;
    }


    public List<String> getAllTracks() {
        return new ArrayList<>(tracks);
    }


    public void clear() {
        tracks.clear();
        currentIndex = -1;
    }


    public boolean isEmpty() {
        return tracks.isEmpty();
    }
}
