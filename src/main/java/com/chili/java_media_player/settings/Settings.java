package com.chili.java_media_player.settings;

/**
 * Object of the JSON settings file.
 */
public class Settings {
    // Default Settings Values
    // Using 50% volume (0.5) as the default volume from 0.0 to 1.0 range
    // Currently volume setting does nothing
    private static final double DEFAULT_VOLUME = 0.5;
    private static final boolean DEFAULT_AUTO_PLAY = true;
    private static final boolean DEFAULT_LOOP_PLAYLIST = false;
    private static final boolean DEFAULT_EXAMPLE_1 = false;
    private static final boolean DEFAULT_EXAMPLE_2 = false;

    // Actual Settings Fields
    // Volume is stored as a double (0.0 to 1.0)
    private double volume;
    // Maps to Checkbox 1 in settings menu
    private boolean autoPlayNextTrack;
    // Maps to Checkbox 2 in settings menu
    private boolean loopPlaylist;
    private boolean exampleSetting1;
    private boolean exampleSetting2;

    /**
     * Creates a Settings object with default values.
     */
    public Settings() {
        this.volume = DEFAULT_VOLUME;
        this.autoPlayNextTrack = DEFAULT_AUTO_PLAY;
        this.loopPlaylist = DEFAULT_LOOP_PLAYLIST;
        this.exampleSetting1 = DEFAULT_EXAMPLE_1;
        this.exampleSetting2 = DEFAULT_EXAMPLE_2;
    }

    // --- Accessors ---\

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public boolean isAutoPlayNextTrack() {
        return autoPlayNextTrack;
    }

    public void setAutoPlayNextTrack(boolean autoPlayNextTrack) {
        this.autoPlayNextTrack = autoPlayNextTrack;
    }

    public boolean isLoopPlaylist() {
        return loopPlaylist;
    }


    public void setLoopPlaylist(boolean loopPlaylist) {
        this.loopPlaylist = loopPlaylist;
    }

    public void setExampleSetting1(boolean ex1) {
        this.exampleSetting1 = ex1;
    }

    public void setExampleSetting2(boolean ex2) {
        this.exampleSetting2 = ex2;
    }

    public boolean ExampleSetting1() {
        return this.exampleSetting1;
    }
    public boolean ExampleSetting2() {
        return this.exampleSetting2;
    }
}