package ru.nstu.rgz_poker.FXMLController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ru.nstu.rgz_poker.MainLauncher;

import java.io.IOException;

public class RulesController {
    @FXML
    public Button rulesToMenuButton;

    @FXML
    public void rulesToMenuAction(ActionEvent event) throws IOException {
        Parent menuParent = FXMLLoader.load(MainLauncher.class.getResource("resources/menu.fxml"));
        Scene menuScene = new Scene(menuParent);
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        mainStage.setScene(menuScene);
        mainStage.show();
    }
}
