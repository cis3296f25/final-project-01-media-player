package com.chili.java_media_player;

import java.util.ArrayList;
import java.util.Collections; //imported here to add a shuffle, can remove later if shuffle not required
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {

        List<String> welcomeList = new ArrayList<>();
        welcomeList.add("Welcome to Java Media Player & Visualizer!");
        welcomeList.add("Welcome to JMD!");
        Collections.shuffle(welcomeList);

        welcomeText.setText(welcomeList.get(0));
    }
}
