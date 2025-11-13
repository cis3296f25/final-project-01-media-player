package com.chili.java_media_player;

import java.io.IOException;

import com.chili.java_media_player.settings.SettingsManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaMediaPlayer extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Load settings from file or create defaults
        SettingsManager.getInstance().loadSettings();

        FXMLLoader fxmlLoader = new FXMLLoader(JavaMediaPlayer.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320 * 3, 240 * 3);
        stage.setTitle("Java Media Player & Audio Visualizer");
        stage.setScene(scene);
        stage.show();

    }
}