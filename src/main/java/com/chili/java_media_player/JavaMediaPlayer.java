package com.chili.java_media_player;

import java.io.IOException;

import com.chili.java_media_player.settings.SettingsManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class JavaMediaPlayer extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Load settings from file or create defaults
        SettingsManager.getInstance().loadSettings();

        FXMLLoader fxmlLoader = new FXMLLoader(JavaMediaPlayer.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320 * 3, 240 * 3);
        stage.setTitle("Java Media Player & Audio Visualizer");
        Controller controller  = fxmlLoader.getController();
        // function that runs when user exits the program
        // cleans up child stages

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                System.out.println(event.getCode());
                switch(event.getCode()) {
                    case P:
                        controller.onPlayClick();
                        break;
                    case R:
                        controller.replayClick();
                        break;
                    case S:
                        controller.shuffleClick();
                        break;
                    case C:
                        controller.clearPlaylistClick();
                        break;
                    case BACK_SPACE:
                        controller.removeTrackClick();
                        break;
                    case OPEN_BRACKET:
                        controller.previousTrackClick();
                        break;
                    case CLOSE_BRACKET:
                        controller.nextTrackClick();
                        break;
                    case BACK_QUOTE:
                        controller.onSettingsAbout();
                        break;
                    case BACK_SLASH:
                        controller.onSettingsPreferences();
                        break;
                    case ESCAPE:
                        Platform.exit();
                        break;
                    case X:
                        if (stage.isMaximized() == true)
                            {stage.setMaximized(false);}
                        else {stage.setMaximized(true);}
                        break;
                    default:
                        break;
                }
            }
        });
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
            }
        });
        stage.setScene(scene);
        stage.show();

    }
}