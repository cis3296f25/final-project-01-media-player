package com.chili.java_media_player.settings;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;

/**
 * Controller for the settings menu (settingsMenu.fxml).
 * Handles the settings menu changes being updated
 * based off of the settings menu
 */
public class SettingsController {

    // FXML fields for UI components, maps vars to fxml objects based on id
    @FXML
    private Button applyButton;

    @FXML
    private Button closeButton;

    @FXML
    private Label settingsStatusLabel;

    @FXML
    private CheckBox exampleSetting1Checkbox;

    @FXML
    private CheckBox exampleSetting2Checkbox;

    private Settings currentSettings;

    /**
     * Initializes the controller. This is called automatically after the FXML
     * file has been loaded.
     */
    @FXML
    public void initialize() {
        // Get the settings instance
        this.currentSettings = SettingsManager.getInstance().getSettings();

        // load stored settings to set the initial state of the checkboxes
        exampleSetting1Checkbox.setSelected(currentSettings.ExampleSetting1());
        exampleSetting2Checkbox.setSelected(currentSettings.ExampleSetting2());
    }

    /**
     * Handles the 'Apply' button click event.
     * Saves the current settings state to the configuration file.
     */
    @FXML
    public void onApplyClick(ActionEvent event) {
        // get current UI state
        boolean ex1 = exampleSetting1Checkbox.isSelected();
        boolean ex2 = exampleSetting2Checkbox.isSelected();

        // Update the settings object based off checkboxes
        currentSettings.setExampleSetting1(ex1);
        currentSettings.setExampleSetting2(ex2);

        // Save the changes to the JSON file
        SettingsManager.getInstance().saveSettings();

        settingsStatusLabel.setText("Settings applied and saved!");
    }

    /**
     * Handles the 'Close' button click event.
     * Closes the settings window.
     */
    @FXML
    public void onCloseClick(ActionEvent event) {
        // Get the stage from the button and close it
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}