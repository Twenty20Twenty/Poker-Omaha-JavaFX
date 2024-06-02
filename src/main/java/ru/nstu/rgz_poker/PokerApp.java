package ru.nstu.rgz_poker;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class PokerApp extends javafx.application.Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage mainStage) throws IOException {

        Parent root = FXMLLoader.load(MainLauncher.class.getResource("resources/enterName.fxml"));
        Scene menuScene = new Scene(root);

        mainStage.setMaximized(false);
        mainStage.setResizable(false);
        mainStage.setTitle("Poker Omaha hi lo");

        mainStage.setScene(menuScene);
        mainStage.show();
    }
}