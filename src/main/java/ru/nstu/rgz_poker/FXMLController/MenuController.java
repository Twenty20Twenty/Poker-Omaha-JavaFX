package ru.nstu.rgz_poker.FXMLController;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ru.nstu.rgz_poker.MainLauncher;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    public Button menuToGameConnectionButton;
    @FXML
    public Button menuToRulesButton;
    @FXML
    public Button menuToExitButton;
    @FXML
    public Label helloNameLabel;
    private PokerGame game;
    private static String playerName = "PlayerName";


    @FXML
    public void menuToGameConnectionAction(ActionEvent event) throws IOException {
        //playerName = "Twenty";
        if (game != null)
            game = null;
        game = new PokerGame(event, playerName);
    }

    @FXML
    public void menuToRulesAction(ActionEvent event) throws IOException {
        Parent rulesParent = FXMLLoader.load(MainLauncher.class.getResource("resources/rules.fxml"));
        Scene rulesScene = new Scene(rulesParent);
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        mainStage.setScene(rulesScene);
        mainStage.show();
    }

    @FXML
    public void exitAction(ActionEvent event) {
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        mainStage.close();
        Platform.exit();
    }

    @FXML
    public void menuToCombinationsAction(ActionEvent event) throws IOException {
        Parent rulesParent = FXMLLoader.load(MainLauncher.class.getResource("resources/cardCombinations.fxml"));
        Scene rulesScene = new Scene(rulesParent);
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        mainStage.setScene(rulesScene);
        mainStage.show();
    }

    public void menuToStatAction(ActionEvent event) throws IOException {
        Parent rulesParent = FXMLLoader.load(MainLauncher.class.getResource("resources/stat.fxml"));
        Scene rulesScene = new Scene(rulesParent);
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        mainStage.setScene(rulesScene);
        mainStage.show();
    }

    public static String getPlayerName() {
        return playerName;
    }

    public static void setPlayerName(String playerName) {
        MenuController.playerName = playerName;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        helloNameLabel.setText("");
        helloNameLabel.setText("Welcome " + playerName);
    }
}
