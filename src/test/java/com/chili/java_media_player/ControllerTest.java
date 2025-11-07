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
 * TestFX allows us to interact with the JavaFX UI components in a testing environment.
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
        // Loop through windows until it finds one that isn't the main window (prob a better way to do this)
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
}