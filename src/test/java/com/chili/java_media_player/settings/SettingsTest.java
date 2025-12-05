package com.chili.java_media_player.settings;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Settings data model class.
 */
public class SettingsTest {

    // Helper constant values from Settings.java for clear testing
    private static final double DEFAULT_VOLUME = 0.5;
    private static final boolean DEFAULT_AUTO_PLAY = true;
    private static final boolean DEFAULT_LOOP_PLAYLIST = false;
    private static final boolean DEFAULT_EXAMPLE_1 = false;
    private static final boolean DEFAULT_EXAMPLE_2 = false;

    @Test
    void testDefaultConstructorValues() {
        Settings settings = new Settings();

        // Test for default values
        assertEquals(DEFAULT_VOLUME, settings.getVolume(), "Default volume should be " + DEFAULT_VOLUME);
        assertEquals(DEFAULT_AUTO_PLAY, settings.isAutoPlayNextTrack(), "Default autoPlayNextTrack should be " + DEFAULT_AUTO_PLAY);
        assertEquals(DEFAULT_LOOP_PLAYLIST, settings.isLoopPlaylist(), "Default loopPlaylist should be " + DEFAULT_LOOP_PLAYLIST);
        assertEquals(DEFAULT_EXAMPLE_1, settings.ExampleSetting1(), "Default exampleSetting1 should be " + DEFAULT_EXAMPLE_1);
        assertEquals(DEFAULT_EXAMPLE_2, settings.ExampleSetting2(), "Default exampleSetting2 should be " + DEFAULT_EXAMPLE_2);
    }

    @Test
    void testSetVolume() {
        Settings settings = new Settings();
        double newVolume = 0.85;
        settings.setVolume(newVolume);
        assertEquals(newVolume, settings.getVolume(), "Volume should be updated to " + newVolume);
    }

    @Test
    void testSetAutoPlayNextTrack() {
        Settings settings = new Settings();
        settings.setAutoPlayNextTrack(false);
        assertFalse(settings.isAutoPlayNextTrack(), "AutoPlayNextTrack should be set to false");
        settings.setAutoPlayNextTrack(true);
        assertTrue(settings.isAutoPlayNextTrack(), "AutoPlayNextTrack should be set to true");
    }

    @Test
    void testSetLoopPlaylist() {
        Settings settings = new Settings();
        settings.setLoopPlaylist(true);
        assertTrue(settings.isLoopPlaylist(), "LoopPlaylist should be set to true");
        settings.setLoopPlaylist(false);
        assertFalse(settings.isLoopPlaylist(), "LoopPlaylist should be set to false");
    }

    // Testing the custom ExampleSetting accessors
    @Test
    void testSetAndGetExampleSetting1() {
        Settings settings = new Settings();
        settings.setExampleSetting1(true);
        assertTrue(settings.ExampleSetting1(), "ExampleSetting1 should be true after setting");
        settings.setExampleSetting1(false);
        assertFalse(settings.ExampleSetting1(), "ExampleSetting1 should be false after setting");
    }

    @Test
    void testSetAndGetExampleSetting2() {
        Settings settings = new Settings();
        settings.setExampleSetting2(true);
        assertTrue(settings.ExampleSetting2(), "ExampleSetting2 should be true after setting");
        settings.setExampleSetting2(false);
        assertFalse(settings.ExampleSetting2(), "ExampleSetting2 should be false after setting");
    }
}