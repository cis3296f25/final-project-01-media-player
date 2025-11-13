package com.chili.java_media_player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VolumeSliderTest {

    private JMPAudioPlayer audioPlayer;

    @BeforeEach
    public void setUp() {
        audioPlayer = new JMPAudioPlayer();
    }

    // Test setting volume to minimum (0%)
    @Test
    public void testVolumeAtMinimum() {
        audioPlayer.setVolume(0);
        assertEquals(0.0, audioPlayer.getCurrentVolume(), 0.01);
    }

    // Test setting volume to maximum (100%)
    @Test
    public void testVolumeAtMaximum() {
        audioPlayer.setVolume(100);
        assertEquals(1.0, audioPlayer.getCurrentVolume(), 0.01);
    }

    // Test setting volume to middle (50%)
    @Test
    public void testVolumeAtMiddle() {
        audioPlayer.setVolume(50);
        assertEquals(0.5, audioPlayer.getCurrentVolume(), 0.01);
    }

}

