package com.chili.java_media_player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlaylistTest {

    private Playlist playlist;

    @BeforeEach
    public void setUp() {
        playlist = new Playlist();
    }


    //can it add a track?    @Test
    public void testAddTrack() {
        playlist.addTrack("song1.mp3");
        assertEquals(1, playlist.getSize());
        assertEquals("song1.mp3", playlist.getCurrentTrack());
    }

    //can it add multiple tracks?    
    @Test
    public void testAddMultipleTracks() {
        playlist.addTrack("song1.mp3");
        playlist.addTrack("song2.mp3");
        playlist.addTrack("song3.mp3");
        assertEquals(3, playlist.getSize());
        assertEquals("song1.mp3", playlist.getCurrentTrack());
    }

    //can it get the next track?    
    @Test
    public void testGetNextTrack() {
        playlist.addTrack("song1.mp3");
        playlist.addTrack("song2.mp3");
        playlist.addTrack("song3.mp3");
        
        assertEquals("song1.mp3", playlist.getCurrentTrack());
        assertEquals("song2.mp3", playlist.getNextTrack());
        assertEquals("song2.mp3", playlist.getCurrentTrack());
        assertEquals("song3.mp3", playlist.getNextTrack());
        assertEquals("song3.mp3", playlist.getCurrentTrack());
        assertNull(playlist.getNextTrack()); // No more tracks
    }
    //can it get the previous track?
    @Test
    public void testGetPreviousTrack() {
        playlist.addTrack("song1.mp3");
        playlist.addTrack("song2.mp3");
        playlist.addTrack("song3.mp3");
        
        playlist.getNextTrack();
        playlist.getNextTrack();
        assertEquals("song3.mp3", playlist.getCurrentTrack());

    }

    //can it check if there's a next track?
    @Test
    public void testHasNextTrack() {
        playlist.addTrack("song1.mp3");
        playlist.addTrack("song2.mp3");
        
        assertTrue(playlist.hasNextTrack());
        playlist.getNextTrack();
        assertFalse(playlist.hasNextTrack());
    }

    //can it check if there's a previous track?
    @Test
    public void testHasPreviousTrack() {
        playlist.addTrack("song1.mp3");
        playlist.addTrack("song2.mp3");
        //expecting the player to be on the first index, trying to move backwards on the first index should cause an error
        //moving forward and then checking back shouldnt
        assertFalse(playlist.hasPreviousTrack());
        playlist.getNextTrack();
        assertTrue(playlist.hasPreviousTrack());
    }

    //can it remove a track?
    @Test
    //Test to see if removing a track doesn't crash the entire playlist, I want it to be able to remove a track but still maintain the list
    public void testRemoveTrack() {
        playlist.addTrack("song1.mp3");
        playlist.addTrack("song2.mp3");
        playlist.addTrack("song3.mp3");
        
        assertEquals(3, playlist.getSize());
        playlist.removeTrack(1); 
        assertEquals(2, playlist.getSize());
        
        assertEquals("song1.mp3", playlist.getCurrentTrack());
    }

    //can it remove the current track without blowing up?
    @Test
    public void testRemoveCurrentTrack() {
        playlist.addTrack("song1.mp3");
        playlist.addTrack("song2.mp3");
        playlist.addTrack("song3.mp3");
        
        playlist.getNextTrack(); 
        assertEquals("song2.mp3", playlist.getCurrentTrack());
        assertEquals(1, playlist.getCurrentIndex());
        
        playlist.removeTrack(1); 
        assertEquals(2, playlist.getSize());
        assertEquals("song3.mp3", playlist.getCurrentTrack());
    }

    //can it clear the playlist?
    public void testClearPlaylist() {
        playlist.addTrack("song1.mp3");
        playlist.addTrack("song2.mp3");
        
        playlist.clear();
        assertEquals(0, playlist.getSize());
        assertTrue(playlist.isEmpty());
        assertNull(playlist.getCurrentTrack());
    }
}
