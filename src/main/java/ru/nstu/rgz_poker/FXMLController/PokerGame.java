package ru.nstu.rgz_poker.FXMLController;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ru.nstu.rgz_poker.Model.*;

import java.util.ArrayList;
import java.util.List;

public class PokerGame implements IGameRules{
    Player singlePlayer;
    List<Player> computerPlayers;
    Table table;
    TableControl tableControl;
    Stage primaryOwner;
    private Dealer dealer;
    private Player highHandWinner;
    private Player lowHandWinner;
    public String playerName;

    public PokerGame(ActionEvent event, String playerName) {
        this.playerName = "playerName";
        primaryOwner = (Stage) ((Node) event.getSource()).getScene().getWindow();

        startGame(playerName);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(tableControl);
        StackPane.setAlignment(tableControl, Pos.CENTER);
        stackPane.setStyle("-fx-background-color: #490440;");
        Scene scene = new Scene(stackPane);
        scene.setCamera(new PerspectiveCamera());
        primaryOwner.setScene(scene);
        primaryOwner.show();
    }

    public void startGame(String playerName) {
        createPlayers(playerName);
        createTable();
        createDealer();
        tableControl = new TableControl(dealer, this);
        tableControl.startGame();
        tableControl.changeChipsPositionsAnimations();
    }

    public Player getHighHandWinner() {
        return highHandWinner;
    }

    public void setHighHandWinner(Player player) {
        highHandWinner = player;
    }

    public Player getLowHandWinner() {
        return lowHandWinner;
    }

    public void setLowHandWinner(Player player) {
        lowHandWinner = player;
    }


    private void dealHoleCards() {
        int dealingOrder = dealer.getDealingPlayerOrder();
        dealer.dealToPlayers();
        tableControl.showDealingToPlayerAnimation(dealingOrder);

    }

    private void createPlayers(String playerName) {
        singlePlayer = new HumanPlayer(playerName, 0);
        singlePlayer.setDealer(true);

        computerPlayers = new ArrayList<Player>();
        for (int i = 1; i < GAME_PLAYERS; i++) {
            Player player = new ComputerPlayer("CP" + i, i);
            computerPlayers.add(player);
        }
    }

    private void createTable() {
        List<Player> allPlayers = new ArrayList<Player>(GAME_PLAYERS);
        allPlayers.add(singlePlayer);
        allPlayers.addAll(computerPlayers);
        table = new Table(allPlayers);
    }

    private void createDealer() {
        dealer = new Dealer();
        dealer.shuffleDeck();
        dealer.setTable(table);
    }
}
