package com.chili.java_media_player.settings;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the SettingsManager class
 */
public class SettingsManagerTest {

    private Path tempSettingsDir;
    private Path tempSettingsFile;

    /**
     * Resets the instance and sets up a temporary directory and file path
     * before each test to so it doesn't mess up your settings.
     */
    @BeforeEach
    void setUp() throws Exception {
        // Reset the instance
        java.lang.reflect.Field instance = SettingsManager.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);

        // Create a temporary file path for testing
        tempSettingsDir = Files.createTempDirectory("jmp_settings_test");
        tempSettingsFile = tempSettingsDir.resolve("settings.json");

        // Force the instance to use the temporary path
        SettingsManager manager = SettingsManager.getInstance();
        java.lang.reflect.Field configPathField = SettingsManager.class.getDeclaredField("configPath");
        configPathField.setAccessible(true);
        configPathField.set(manager, tempSettingsFile);

        // Ensure the temp directory exists as part of the setup
        new File(tempSettingsDir.toUri()).mkdirs();
    }

    /**
     * Cleans up the temporary directory and resets the instance after each test.
     */
    @AfterEach
    void tearDown() throws Exception {
        // Reset instance
        java.lang.reflect.Field instance = SettingsManager.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);

        // Clean up temp files
        if (Files.exists(tempSettingsDir)) {
            Files.walk(tempSettingsDir)
                    .map(Path::toFile)
                    .forEach(File::delete);
            Files.deleteIfExists(tempSettingsDir);
        }
    }

    @Test
    void testSingletonInstance() {
        SettingsManager manager1 = SettingsManager.getInstance();
        SettingsManager manager2 = SettingsManager.getInstance();

        // Assert that both calls return the exact same object
        assertSame(manager1, manager2, "SettingsManager must be a Singleton instance");
    }

    @Test
    void testLoadSettings_fileNotFoundCreatesDefaults() {
        // Ensure the temp file does NOT exist
        assertFalse(Files.exists(tempSettingsFile), "Setup should ensure temp file does not exist initially (only an issue for testing)");

        // Load settings (should create defaults and save the file)
        SettingsManager manager = SettingsManager.getInstance();
        manager.loadSettings();

        // Assert a file was created with default settings
        assertTrue(Files.exists(tempSettingsFile), "Settings file should be created after loading when not found");

        // Assert the settings are the default ones
        assertTrue(manager.getSettings().isAutoPlayNextTrack(), "Settings should contain default AutoPlay=true");
    }

    @Test
    void testSaveAndLoadSettings_persistenceCheck() {
        SettingsManager manager = SettingsManager.getInstance();
        Settings initialSettings = manager.getSettings();

        // Modify settings
        initialSettings.setExampleSetting1(true);
        initialSettings.setVolume(0.99);

        // Save the modified settings
        manager.saveSettings();

        // Reset the manager instance to simulate a fresh application start
        try {
            java.lang.reflect.Field instance = SettingsManager.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (Exception e) {
            fail("Failed to reset SettingsManager instance using reflection: " + e.getMessage());
        }

        // Get the new instance and load from the saved file
        SettingsManager newManager = SettingsManager.getInstance();

        try {
            java.lang.reflect.Field configPathField = SettingsManager.class.getDeclaredField("configPath");
            configPathField.setAccessible(true);
            configPathField.set(newManager, tempSettingsFile);
        } catch (Exception e) {
            fail("Failed to set configPath on new instance: " + e.getMessage());
        }

        newManager.loadSettings();
        Settings loadedSettings = newManager.getSettings();

        // Assert the changes were persisted
        assertTrue(loadedSettings.ExampleSetting1(), "Loaded ExampleSetting1 should be true");
        assertEquals(0.99, loadedSettings.getVolume(), "Loaded volume should be 0.99");
    }
}