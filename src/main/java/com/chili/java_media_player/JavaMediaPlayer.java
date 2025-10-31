package com.chili.java_media_player;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class JavaMediaPlayer extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaMediaPlayer.class.getResource("FXMLsource.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320*4, 240*3);
        stage.setTitle("Java Media Player & Audio Visualizer");
        stage.setScene(scene);
        stage.show();
    }
}
