package ru.nstu.rgz_poker.FXMLController;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import ru.nstu.rgz_poker.MainLauncher;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class EnterNameController implements Initializable {
    @FXML
    private TextField nameTextField;
    @FXML
    private Label errorLabel;

    @FXML
    public void toMenuAction(ActionEvent event) throws IOException {
        if (Objects.equals(nameTextField.getText(), "") || nameTextField.getText() == null) {
            event.consume();
            errorLabel.setVisible(true);
            return;
        }
        MenuController.setPlayerName(nameTextField.getText());
        Parent menuParent = FXMLLoader.load(MainLauncher.class.getResource("resources/menu.fxml"));
        Scene menuScene = new Scene(menuParent);
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        mainStage.setScene(menuScene);
        mainStage.show();
    }

    @FXML
    public void exitAction(ActionEvent event) {
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        mainStage.close();
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameTextField.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if ("/-*&!@#$%^&*()_+}{[]\"'.<>~`\\|".contains(keyEvent.getCharacter())) {
                    keyEvent.consume();
                }
            }
        });
    }
}
