package com.chili.java_media_player.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class to manage the settings.
 * Deals with writing to file
 * Other classes can call it to read or update settings
 */
public class SettingsManager {
    private static SettingsManager instance;
    private Settings currentSettings;
    private final Path configPath;
    private final Gson gson;

    /**
     * Initialize
     */
    private SettingsManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.configPath = getSettingsFilePath();
        this.currentSettings = new Settings(); // Initialize with defaults
    }

    /**
     * Gets the instance of the SettingsManager.
     */
    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    /**
     * Determines the platform-specific path for the settings file.
     * Windows: %APPDATA%/JMP/settings.json
     * macOS/Linux: ~/.config/JMP/settings.json
     *
     * Returns the path to the file as a Path object
     */
    private Path getSettingsFilePath() {
        String os = System.getProperty("os.name").toLowerCase();
        String appDir = "JMP";
        Path basePath;

        if (os.contains("win")) {
            // Windows: %APPDATA%
            basePath = Paths.get(System.getenv("APPDATA"), appDir);
        } else if (os.contains("mac")) {
            // macOS: ~/Library/Application Support/
            basePath = Paths.get(System.getProperty("user.home"), "Library", "Application Support", appDir);
        } else {
            // Linux/Other: ~/.config/
            basePath = Paths.get(System.getProperty("user.home"), ".config", appDir);
        }

        // Ensure the directory exists
        File dir = basePath.toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return basePath.resolve("settings.json");
    }

    /**
     * Loads settings from the file. If the file does not exist or fails to load,
     * it creates a new file with default settings and loads those.
     */
    public void loadSettings() {
        File file = configPath.toFile();
        if (file.exists()) {
            try (FileReader reader = new FileReader(file.toString())) {
                Settings loadedSettings = gson.fromJson(reader, Settings.class);
                if (loadedSettings != null) {
                    this.currentSettings = loadedSettings;
                    System.out.println("Settings loaded successfully from: " + configPath);
                } else {
                    // Handle case where file is empty or invalid JSON
                    System.err.println("Settings file is empty or corrupted. Using default settings.");
                    saveSettings(); // Overwrite with defaults
                }
            } catch (IOException e) {
                System.err.println("Error reading settings file. Using default settings: " + e.getMessage());
                saveSettings(); // Create/overwrite with defaults
            }
        } else {
            System.out.println("Settings file not found. Creating and saving default settings.");
            saveSettings(); // Creates file with default settings
        }
    }

    /**
     * Saves the current settings object to the JSON file.
     */
    public void saveSettings() {
        try (FileWriter writer = new FileWriter(configPath.toString())) {
            gson.toJson(this.currentSettings, writer);
            System.out.println("Settings saved successfully to: " + configPath);
        } catch (IOException e) {
            System.err.println("Error writing settings file: " + e.getMessage());
        }
    }

    /**
     * Getter for the settings object.
     * All classes should use this to read and modify settings.
     */
    public Settings getSettings() {
        return currentSettings;
    }
}