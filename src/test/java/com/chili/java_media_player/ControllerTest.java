package com.chili.java_media_player;

import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.api.FxRobot;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the JavaMediaPlayer application using TestFX.
 * Uses FxRobot to control the mouse.
 * When running tests don't move mouse or it will fail.
 */
@ExtendWith(ApplicationExtension.class)
public class ControllerTest {

    private Stage mainStage;

    /**
     * Create main window when the test starts for all the testing to use
     */
    @Start
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        new JavaMediaPlayer().start(stage);
    }

    /**
     * Tests if the main window opens correctly with the expected title.
     */
    @Test
    void testMainWindowOpensSuccessfully() {
        // Check if the main window is showing
        assertTrue(mainStage.isShowing(), "The main application window is not showing.");

        // 2. Check the title of the main window
        assertEquals("Java Media Player & Audio Visualizer", mainStage.getTitle(),
                "The main window title is not being found as 'Java Media Player & Audio Visualizer'.");

        // 3. Check if the scene root loaded (e.g., the BorderPane should be there)
        assertNotNull(mainStage.getScene().getRoot(), "The main scene content should not be null.");
    }

    /**
     * Tests the onSettingsPreferences action
     * Makes sure that it properly opens, and the title is correct
     */
    @Test
    void testOnSettingsPreferencesOpensCorrectWindow(FxRobot robot) {
        robot.clickOn("Settings");
        // wait .2 seconds in case there's lag from the menu
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        robot.clickOn("Preferences");

        WaitForAsyncUtils.waitForFxEvents();

        // Find settings window
        // Loop through windows until it finds one that isn't the main window (prob a
        // better way to do this)
        Stage settingsStage = null;
        for (Window window : Stage.getWindows()) {
            if (window instanceof Stage stage &&
                    stage.isShowing() &&
                    !stage.getTitle().equals(mainStage.getTitle())) {
                settingsStage = stage;
                break;
            }
        }

        // Make sure the window was open and the tester can find it
        assertNotNull(settingsStage, "The settings window was not found be opened.");

        // Make sure the title is correct
        assertEquals("JMP Settings", settingsStage.getTitle(),
                "The settings window title is not being found as 'JMP Settings'.");
    }

    /**
     * Tests the onSettingsAbout action
     * Makes sure that it properly opens, and the title is correct
     */
    @Test
    void testOnSettingsAboutOpensCorrectWindow(FxRobot robot) {
        robot.clickOn("Settings");
        // wait .2 seconds in case there's lag from the menu
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        robot.clickOn("About");

        WaitForAsyncUtils.waitForFxEvents();

        // Find about window
        Stage aboutStage = null;
        for (Window window : Stage.getWindows()) {
            if (window instanceof Stage stage &&
                    stage.isShowing() &&
                    !stage.getTitle().equals(mainStage.getTitle())) {
                aboutStage = stage;
                break;
            }
        }

        // Make sure the window was open and the tester can find it
        assertNotNull(aboutStage, "The about window was not found be opened.");

        // Test window title
        assertEquals("About", aboutStage.getTitle(),
                "The about window title is not being found as 'About'.");

        // Close window
        robot.interact(aboutStage::close);
    }

    /**
     * Tests that the Settings window is brought to the front if it's already open
     * instead of opening a new one.
     */
    @Test
    void testSettingsPreferencesBringsToFrontWhenOpen(FxRobot robot) {
        // Open the Settings window, wait for .2s in case of lag
        robot.clickOn("Settings");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        robot.clickOn("Preferences");
        WaitForAsyncUtils.waitForFxEvents();

        // Find the settings window
        Stage firstSettingsStage = null;
        for (Window window : Stage.getWindows()) {
            if (window instanceof Stage stage &&
                    stage.isShowing() &&
                    stage.getTitle().equals("JMP Settings")) {
                firstSettingsStage = stage;
                break;
            }
        }
        assertNotNull(firstSettingsStage, "Initial Settings window was not found.");

        // Record the hash code of the first stage
        int firstStageHashCode = firstSettingsStage.hashCode();

        // Click "Preferences" again
        robot.clickOn("Settings");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        robot.clickOn("Preferences");
        WaitForAsyncUtils.waitForFxEvents();

        // Check only 1 settings window is open
        Stage secondSettingsStage = null;
        int settingsWindowCount = 0;
        for (Window window : Stage.getWindows()) {
            if (window instanceof Stage stage &&
                    stage.isShowing() &&
                    stage.getTitle().equals("JMP Settings")) {
                secondSettingsStage = stage;
                settingsWindowCount++;
            }
        }

        assertEquals(1, settingsWindowCount, "Multiple or no Settings windows found.");
        assertNotNull(secondSettingsStage, "Second click did not find the Settings window.");
        assertEquals(firstStageHashCode, secondSettingsStage.hashCode(), "Second click opened a new window.");
    }

    /**
     * Tests that the About window is brought into focus if it's already open
     * instead of opening a new one.
     */
    @Test
    void testSettingsAboutBringsToFrontWhenOpen(FxRobot robot) {
        // Open the About window once, wait .2s in case of lag
        robot.clickOn("Settings");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        robot.clickOn("About");
        WaitForAsyncUtils.waitForFxEvents();

        // Find the about stage
        Stage firstAboutStage = null;
        for (Window window : Stage.getWindows()) {
            if (window instanceof Stage stage &&
                    stage.isShowing() &&
                    stage.getTitle().equals("About")) {
                firstAboutStage = stage;
                break;
            }
        }
        assertNotNull(firstAboutStage, "Initial About window was not found.");

        // Record the hash code of the first stage
        int firstStageHashCode = firstAboutStage.hashCode();

        // Click "About" again
        robot.clickOn("Settings");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        robot.clickOn("About");
        WaitForAsyncUtils.waitForFxEvents();

        // Check that the same window is still the only one open
        Stage secondAboutStage = null;
        int aboutWindowCount = 0;
        for (Window window : Stage.getWindows()) {
            if (window instanceof Stage stage &&
                    stage.isShowing() &&
                    stage.getTitle().equals("About")) {
                secondAboutStage = stage;
                aboutWindowCount++;
            }
        }

        assertEquals(1, aboutWindowCount, "Multiple or no About windows found.");
        assertNotNull(secondAboutStage, "Second click did not find the About window.");
        assertEquals(firstStageHashCode, secondAboutStage.hashCode(), "Second click opened a new window.");
    }

}