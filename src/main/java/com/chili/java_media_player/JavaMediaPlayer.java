package com.chili.java_media_player;

import java.io.IOException;
import java.io.InputStream;

import com.chili.java_media_player.settings.SettingsManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class JavaMediaPlayer extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        InputStream iconStream = getClass().getResourceAsStream("icon.png");
        Image applicationIcon = new Image(iconStream);
        stage.getIcons().add(applicationIcon);

        // Load settings from file or create defaults
        SettingsManager.getInstance().loadSettings();

        FXMLLoader fxmlLoader = new FXMLLoader(JavaMediaPlayer.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320 * 3, 240 * 3);
        stage.setTitle("Java Media Player & Audio Visualizer");
        // function that runs when user exits the program
        // cleans up child stages
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