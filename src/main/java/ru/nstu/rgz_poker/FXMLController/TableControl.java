/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.nstu.rgz_poker.FXMLController;

import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.nstu.rgz_poker.MainLauncher;
import ru.nstu.rgz_poker.Model.*;
import ru.nstu.rgz_poker.Model.Dealer.Round;
import ru.nstu.rgz_poker.Model.Player.PlayerAction;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.*;
import java.sql.*;

import static ru.nstu.rgz_poker.Model.IDealerRules.PLAYER_HAND_SIZE;
import static ru.nstu.rgz_poker.Model.IPlayerRules.PLAYER_INITIAL_CHIPS;
import static ru.nstu.rgz_poker.Model.ITableRules.*;


/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class TableControl extends AnchorPane implements IGameRules {
    @FXML
    private AnchorPane tableAnchor;
    @FXML
    private HBox singlePlayerCards, playerCards1, playerCards2, playerCards3, playerCards4, tableCards;
    @FXML
    private Text singlePlayerName, player1Name, player2Name, player3Name, player4Name;
    @FXML
    private Text singlePlayerChips, player1Chips, player2Chips, player3Chips, player4Chips;
    @FXML
    private Rectangle singlePlayerMessageBox, player1MessageBox, player2MessageBox, player3MessageBox, player4MessageBox;
    @FXML
    private Text singlePlayerMessageText, player1MessageText, player2MessageText, player3MessageText, player4MessageText;
    @FXML
    private Text roundMessageText;
    @FXML
    private Rectangle roundMessageBox;
    @FXML
    private Text singlePlayerPot, player1Pot, player2Pot, player3Pot, player4Pot;
    @FXML
    private Pane playerActionsPane;
    @FXML
    private ProgressIndicator playerTimeIndicatior;
    @FXML
    private Button callButton, raiseButton, checkButton, foldButton, allInButton;
    @FXML
    private AnchorPane winnersAnchorPane, gameOverAnchorPane;
    @FXML
    private Button newGameButton, quitGameButton;
    @FXML
    private Text balanceText;
    @FXML
    private Text highWinnerName, lowWinnerName, highHandType;
    @FXML
    private HBox highWinningCards, lowWinnngCards;
    @FXML
    private Text highWinnings, lowWinnings;
    @FXML
    private Button playAgainButton, exitGameButton, closeGameButton;
    @FXML
    private Group dealerChip, smallBlindChip, bigBlindChip;

    private List<HBox> playerCardsList;
    private Dealer dealer;
    private PokerGame application;
    private Table table;
    private PlayerAction playerSelectedAction;
    private Timeline timelinePlayerActionAnimation;
    private int roundsCount = 0;
    private Boolean stopFlag = false;


    public TableControl(Dealer dealerRef, final PokerGame application) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainLauncher.class.getResource("resources/FXMLTable2.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.dealer = dealerRef;
        table = dealerRef.getTable();
        this.application = application;
        this.stopFlag = false;
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        setUpPlayerActions();
        setUpWinnerScreenActions();
        initializeTableControls();
    }

    private void initializeTableControls() {
        playerCardsList = new ArrayList<HBox>();
        playerCardsList.add(singlePlayerCards);
        playerCardsList.add(playerCards1);
        playerCardsList.add(playerCards2);
        playerCardsList.add(playerCards3);
        playerCardsList.add(playerCards4);
        playerSelectedAction = PlayerAction.FOLD;
    }

    private void setUpPlayerActions() {
        callButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                hidePlayerControls();
                playerSelectedAction = PlayerAction.CALL;
                if (timelinePlayerActionAnimation != null) {
                    timelinePlayerActionAnimation.jumpTo(Duration.millis(PLAYER_TIME_OUT_SECONDS * 1000));
                }
                setPlayerActionText(playerSelectedAction);
                dealer.getCallFrom(0);
            }
        });

        raiseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                hidePlayerControls();
                playerSelectedAction = PlayerAction.RAISE;
                if (timelinePlayerActionAnimation != null) {
                    timelinePlayerActionAnimation.jumpTo(Duration.millis(PLAYER_TIME_OUT_SECONDS * 1000));
                }
                setPlayerActionText(playerSelectedAction);
                dealer.getRaiseFrom(0);

            }
        });

        checkButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                hidePlayerControls();
                playerSelectedAction = PlayerAction.CHECK;
                if (timelinePlayerActionAnimation != null) {
                    timelinePlayerActionAnimation.jumpTo(Duration.millis(PLAYER_TIME_OUT_SECONDS * 1000));
                }

                setPlayerActionText(playerSelectedAction);
            }
        });

        foldButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                hidePlayerControls();
                playerSelectedAction = PlayerAction.FOLD;
                if (timelinePlayerActionAnimation != null) {
                    timelinePlayerActionAnimation.jumpTo(Duration.millis(PLAYER_TIME_OUT_SECONDS * 1000));
                }

                setPlayerActionText(playerSelectedAction);
                dealer.setFlod(0);
            }
        });

        allInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                hidePlayerControls();
                playerSelectedAction = PlayerAction.ALL_IN;
                if (timelinePlayerActionAnimation != null) {
                    timelinePlayerActionAnimation.jumpTo(Duration.millis(PLAYER_TIME_OUT_SECONDS * 1000));
                }

                setPlayerActionText(playerSelectedAction);
                dealer.getAllInFrom(0);
            }
        });
    }


    private void setUpWinnerScreenActions() {
        playAgainButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                winnersAnchorPane.setVisible(false);
                winnersAnchorPane.setLayoutY(winnersAnchorPane.getLayoutY() - 10);
                lowWinnings.setText("$ 0");
                resetWinners();
                clearTable();
                adjustDealer();
                adjustTable();
                if (adjustPlayers()) {
                    changeChipsPositionsAnimations();
                    startGame();
                } else {
                    showGameOver();
                }
            }
        });

        newGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                application.startGame(application.playerName);
                gameOverAnchorPane.setVisible(false);
                gameOverAnchorPane.setLayoutY(winnersAnchorPane.getLayoutY() - 10);
                resetWinners();
                clearTable();
                adjustDealer();
                adjustTable();
                adjustPlayersNewGame();
                dealer.setDealingPlayerOrder(1);
                startGame();
            }
        });

        exitGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Parent menuParent = null;
                try {
                    menuParent = FXMLLoader.load(MainLauncher.class.getResource("resources/enterName.fxml"));
                } catch (IOException ioe) {
                    throw new RuntimeException(ioe);
                }
                Scene menuScene = new Scene(menuParent);
                Stage mainStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                mainStage.setScene(menuScene);
                mainStage.show();
                stopFlag = true;
                SQLConnection.insertPlayer(application.singlePlayer.getName(), new Timestamp(System.currentTimeMillis()) ,(int) application.singlePlayer.getPlayerChips());
            }
        });

        closeGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Parent menuParent = null;
                try {
                    menuParent = FXMLLoader.load(MainLauncher.class.getResource("resources/enterName.fxml"));
                } catch (IOException ioe) {
                    throw new RuntimeException(ioe);
                }
                Scene menuScene = new Scene(menuParent);
                Stage mainStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                mainStage.setScene(menuScene);
                mainStage.show();
                stopFlag = true;
                SQLConnection.insertPlayer(application.singlePlayer.getName(), new Timestamp(System.currentTimeMillis()) ,(int) application.singlePlayer.getPlayerChips());
            }
        });

        quitGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Parent menuParent = null;
                try {
                    menuParent = FXMLLoader.load(MainLauncher.class.getResource("resources/enterName.fxml"));
                } catch (IOException ioe) {
                    throw new RuntimeException(ioe);
                }
                Scene menuScene = new Scene(menuParent);
                Stage mainStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                mainStage.setScene(menuScene);
                mainStage.show();
                stopFlag = true;
                SQLConnection.insertPlayer(application.singlePlayer.getName(), new Timestamp(System.currentTimeMillis()) ,(int) application.singlePlayer.getPlayerChips());
            }
        });
    }

    // обнуление рук победителей в ХАЙ и ЛОУ
    private void resetWinners() {
        application.setHighHandWinner(null);
        application.setLowHandWinner(null);
    }

    // визуальная очистка карт с доски и Текста игроков
    private void clearTable() {
        for (HBox cardList : playerCardsList) {
            cardList.getChildren().clear();
        }
        tableCards.getChildren().clear();

        singlePlayerMessageText.setText("");
        player1MessageText.setText("");
        player2MessageText.setText("");
        player3MessageText.setText("");
        player4MessageText.setText("");
    }

    // обновление дилера, новая колода, обнуление раунда
    private void adjustDealer() {
        dealer.incrementDealingPlayerOrder();
        CardDeck newDeck = new CardDeck();
        newDeck.shuffle();
        dealer.setDeck(newDeck);
        dealer.setRound(Round.PRE_FLOP);
        dealer.setRoundCount(0);
        dealer.clear();
    }

    // обнуление карт и повышение блайндов
    public void adjustTable() {
        table.clear();
        table.incrementBlinds();
        table.setHighestPotValue(0);
    }

    private void adjustPlayersNewGame() {
        for (Player player : table.getPlayers()) {
            player.setFolded(false);
            player.setPlayerChips(PLAYER_INITIAL_CHIPS);
            player.getPlayerHand().clear();
            player.clearWinningHands();
            player.setWinningHandType("");
            player.setAllIn(false);
            player.setCalledForAllIn(false);
        }
    }

    private boolean adjustPlayers() {
        int foldCount = 0;
        for (Player player : table.getPlayers()) {
            player.setFolded(false);
            if (player.getPlayerChips() < table.getBigBlind()) {
                if (player instanceof HumanPlayer) {
                    return false;
                } else {
                    double awardingChips = table.getBigBlind() * PLAYER_REAWARD_FACTOR;
                    player.awardChips(awardingChips);

                    foldCount++;
                }
            }
            player.getPlayerHand().clear();
            player.clearWinningHands();
            player.setWinningHandType("");
            player.setAllIn(false);
            player.setCalledForAllIn(false);
        }
        if (foldCount > 4) {
            return false;
        }

        return true;
    }

    public void changeChipsPositionsAnimations() {
        int[] xCordinates = {106, 106, 345, 486, 492};
        int[] yCordinates = {253, -40, -70, -40, 253};

        int dealingPlayerOrder = dealer.getDealingPlayerOrder();

        int dealingChipPosition = (dealingPlayerOrder + 4) % GAME_PLAYERS;
        int smallBlindChipPosition = dealingPlayerOrder;
        int bigBlindChipPosition = (dealingPlayerOrder + 1) % GAME_PLAYERS;

        KeyValue valueDealerX = new KeyValue(dealerChip.layoutXProperty(), xCordinates[dealingChipPosition], Interpolator.EASE_OUT);
        KeyValue valueDealerY = new KeyValue(dealerChip.layoutYProperty(), yCordinates[dealingChipPosition], Interpolator.EASE_OUT);
        KeyValue valueSmallBlindX = new KeyValue(smallBlindChip.layoutXProperty(), xCordinates[smallBlindChipPosition], Interpolator.EASE_OUT);
        KeyValue valueSmallBlindY = new KeyValue(smallBlindChip.layoutYProperty(), yCordinates[smallBlindChipPosition], Interpolator.EASE_OUT);
        KeyValue valueBigBlindX = new KeyValue(bigBlindChip.layoutXProperty(), xCordinates[bigBlindChipPosition], Interpolator.EASE_OUT);
        KeyValue valueBigBlindY = new KeyValue(bigBlindChip.layoutYProperty(), yCordinates[bigBlindChipPosition], Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), valueDealerX, valueDealerY, valueSmallBlindX, valueSmallBlindY, valueBigBlindX, valueBigBlindY);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(keyFrame);

        RotateTransition rotateDealer = new RotateTransition(Duration.millis(1000), dealerChip);
        rotateDealer.setByAngle(720f);

        RotateTransition rotateSmallBlind = new RotateTransition(Duration.millis(1000), smallBlindChip);
        rotateSmallBlind.setByAngle(720f);

        RotateTransition rotateBigBlind = new RotateTransition(Duration.millis(1000), bigBlindChip);
        rotateBigBlind.setByAngle(720f);

        ParallelTransition trans = new ParallelTransition();
        trans.getChildren().addAll(timeline, rotateDealer, rotateSmallBlind, rotateBigBlind);

        trans.play();
    }

    private void showGameOver() {
        gameOverAnchorPane.setVisible(true);
        gameOverAnchorPane.setScaleX(1);
        gameOverAnchorPane.setScaleY(1);
        gameOverAnchorPane.setLayoutY(winnersAnchorPane.getLayoutY() + 10);
        balanceText.setText("$ " + table.getPlayers().get(0).getPlayerChips());
    }

    private void hidePlayerControls() {
        playerActionsPane.setOpacity(0);
        playerTimeIndicatior.setVisible(false);
    }

    private void setPlayerActionText(PlayerAction playerSelectedAction) {
        singlePlayerMessageText.setText(playerSelectedAction.toString());
    }

    // Запуск игры
    public void startGame() {
        roundsCount++;
        bindPlayers(table.getPlayers());
        dealer.collectBlinds();
        showPreFlopRoundAnimation();
        int dealingOrder = dealer.getDealingPlayerOrder();
        dealer.dealToPlayers();
        showDealingToPlayerAnimation(dealingOrder);
    }

    // Анимация раздачи карт
    void showDealingToPlayerAnimation(int dealingOrder) {
        clearPlayerCards();
        final CardController showingCard = new CardController("DIAMONDS", Value.CardValue.TWO);
        final Pane card = showingCard.getCard();
        card.setLayoutX(325);
        card.setLayoutY(506);
        tableAnchor.getChildren().add(card);
        showingCard.faceDown();

        List<Transition> transitionSequence = new ArrayList<Transition>();

        for (int i = 0, j = dealingOrder; i < GAME_PLAYERS * PLAYER_HAND_SIZE; i++, j++) {
            transitionSequence.add(dealToPlayerAnimation(j % 5));
        }

        SequentialTransition transition = new SequentialTransition();
        transition.getChildren().addAll(transitionSequence);
        transition.play();

        if (!stopFlag) {
            transition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    adjustCardLists();
                    showStartBettingAnimation("Pre Flop Round");
                }
            });
        }
    }

    // Анимация карты от дилера к конкретному игроку
    private Transition dealToPlayerAnimation(final int i) {
        final CardController showingCard = new CardController("DIAMONDS", Value.CardValue.TWO);
        final Pane card = showingCard.getCard();
        card.setLayoutX(325);
        card.setLayoutY(506);

        tableAnchor.getChildren().add(card);
        showingCard.faceDown();
        double destinationX = 354, destinationY = 530;

        switch (i) {
            case 0:
                destinationX = singlePlayerCards.getLayoutX();
                destinationY = singlePlayerCards.getLayoutY();
                break;
            case 1:
                destinationX = playerCards1.getLayoutX();
                destinationY = playerCards1.getLayoutY();
                break;
            case 2:
                destinationX = playerCards2.getLayoutX();
                destinationY = playerCards2.getLayoutY();
                break;
            case 3:
                destinationX = playerCards3.getLayoutX();
                destinationY = playerCards3.getLayoutY();
                break;
            case 4:
                destinationX = playerCards4.getLayoutX();
                destinationY = playerCards4.getLayoutY();
                break;
            default:
                break;
        }

        KeyValue valueX = new KeyValue(card.layoutXProperty(), destinationX, Interpolator.EASE_OUT);
        KeyValue valueY = new KeyValue(card.layoutYProperty(), destinationY, Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), valueX, valueY);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(keyFrame);

        RotateTransition rotateTransition = new RotateTransition(Duration.millis(500), card);
        rotateTransition.setByAngle(720f);
        ParallelTransition parallel = new ParallelTransition();
        parallel.getChildren().addAll(timeline, rotateTransition);

        if (!stopFlag) {
            parallel.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    addCardToCardList(card, i);
                }
            });
        }
        return parallel;
    }

    synchronized private void addCardToCardList(Pane testCard, int i) {
        switch (i) {
            case 0:
                singlePlayerCards.getChildren().add(testCard);
                break;
            case 1:
                playerCards1.getChildren().add(testCard);
                break;
            case 2:
                playerCards2.getChildren().add(testCard);
                break;
            case 3:
                playerCards3.getChildren().add(testCard);
                break;
            case 4:
                playerCards4.getChildren().add(testCard);
                break;
            default:
                break;
        }
    }

    private void clearPlayerCards() {
        for (HBox playerCards : playerCardsList)
            playerCards.getChildren().clear();
    }

    // Анимация складывания карт в стопку, а наши карты показываються
    private void adjustCardLists() {
        List<Timeline> cardAjustTimeLines = new ArrayList<>();

        for (HBox playerCards : playerCardsList) {
            KeyValue valuePosAdjustment = null;
            if (playerCards == playerCards3 || playerCards == playerCards4) {
                valuePosAdjustment = new KeyValue(playerCards.layoutXProperty(), playerCards.getLayoutX() + 50);
            }
            KeyValue valueX = new KeyValue(playerCards.prefWidthProperty(), 50, Interpolator.EASE_OUT);
            KeyFrame keyFrame = null;
            if (valuePosAdjustment != null)
                keyFrame = new KeyFrame(Duration.millis(500), valueX, valuePosAdjustment);
            else
                keyFrame = new KeyFrame(Duration.millis(500), valueX);
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(keyFrame);
            cardAjustTimeLines.add(timeline);
        }

        ParallelTransition transition = new ParallelTransition();
        transition.getChildren().addAll(cardAjustTimeLines);
        if (!stopFlag) {
            transition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    showPlayerCards(0);
                }

            });
        }
        transition.play();
    }

    void bindPlayers(List<Player> allPlayers) {

        List<Text> playerNamesControlList = new ArrayList<>();
        playerNamesControlList.add(singlePlayerName);
        playerNamesControlList.add(player1Name);
        playerNamesControlList.add(player2Name);
        playerNamesControlList.add(player3Name);
        playerNamesControlList.add(player4Name);

        for (int i = 0; i < allPlayers.size(); i++) {
            playerNamesControlList.get(i).textProperty().bind(allPlayers.get(i).nameProperty());
        }

        bindPlayersChips(allPlayers);

        bindPlayerPots(allPlayers);
    }

    void showPreFlopRoundAnimation() {
        clearTableCards();
    }

    private void clearTableCards() {
        tableCards.getChildren().clear();
    }

    private void bindPlayersChips(List<Player> allPlayers) {
        List<Text> playerChipsControlList = new ArrayList<>();
        playerChipsControlList.add(singlePlayerChips);
        playerChipsControlList.add(player1Chips);
        playerChipsControlList.add(player2Chips);
        playerChipsControlList.add(player3Chips);
        playerChipsControlList.add(player4Chips);
        for (int i = 0; i < allPlayers.size(); i++) {
            playerChipsControlList.get(i).textProperty().bind(allPlayers.get(i).getPlayerChipsProperty().asString());
        }
    }

    private void bindPlayerPots(List<Player> allPlayers) {
        List<Text> playerPotControlList = new ArrayList<>(allPlayers.size());
        playerPotControlList.add(singlePlayerPot);
        playerPotControlList.add(player1Pot);
        playerPotControlList.add(player2Pot);
        playerPotControlList.add(player3Pot);
        playerPotControlList.add(player4Pot);

        for (int i = 0; i < allPlayers.size(); i++) {
            final int j = i;
            playerPotControlList.get(i).textProperty().bind(table.getTablePot().getPlayerPots().get(i).playerContributionProperty().asString("%.0f"));
            playerPotControlList.get(i).textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> ov, String oldVal, String newVal) {
                    Text playerPot = null;

                    switch (j) {
                        case 0:
                            playerPot = singlePlayerPot;
                            break;
                        case 1:
                            playerPot = player1Pot;
                            break;
                        case 2:
                            playerPot = player2Pot;
                            break;
                        case 3:
                            playerPot = player3Pot;
                            break;
                        case 4:
                            playerPot = player4Pot;
                            break;
                    }

                    KeyValue valueFill = new KeyValue(playerPot.fillProperty(), Paint.valueOf("ff0000"), Interpolator.EASE_IN);
                    KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), valueFill);
                    Timeline timeline = new Timeline();
                    timeline.getKeyFrames().add(keyFrame);
                    timeline.setCycleCount(6);
                    timeline.setAutoReverse(true);
                    timeline.play();
                }
            });
        }
    }

    // Анимация названия текущего раунда
    private void showStartBettingAnimation(String message) {
        roundMessageText.setText(message);
        roundMessageBox.setOpacity(0);
        KeyValue valueOpacity = new KeyValue(roundMessageText.opacityProperty(), 1, Interpolator.EASE_OUT);
        KeyValue valueBoxOpacity = new KeyValue(roundMessageBox.opacityProperty(), 1, Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(2000), valueOpacity, valueBoxOpacity);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(keyFrame);
        timeline.setAutoReverse(true);
        timeline.setCycleCount(2);
        timeline.play();
    }


    private HBox getPlayerCardsHBox(int player) {
        HBox cardsList = null;
        switch (player) {
            case 0:
                cardsList = singlePlayerCards;
                break;
            case 1:
                cardsList = playerCards1;
                break;
            case 2:
                cardsList = playerCards2;
                break;
            case 3:
                cardsList = playerCards3;
                break;
            case 4:
                cardsList = playerCards4;
                break;
        }

        return cardsList;
    }

    // Анимация переворота карт в руках игрока
    private void showPlayerCards(int player) {
        HBox playerCards = getPlayerCardsHBox(player);
        playerCards.getChildren().clear();

        List<Card> cards = table.getPlayers().get(player).getPlayerHand().getCards();

        List<Pane> playerCardPanes = new ArrayList<Pane>(cards.size());

        for (Card card : cards) {
            CardController CardController = new CardController(card.getSuit().getSuitType().toString(), card.getValue().getCardValue());

            playerCardPanes.add(CardController.getCard());
            CardController.getCard().setRotationAxis(Rotate.Y_AXIS);
            CardController.getCard().setRotate(180);

            RotateTransition rt = new RotateTransition(Duration.millis(1000), CardController.getCard());
            rt.setAxis(Rotate.Y_AXIS);
            rt.setByAngle(180);
            rt.play();
        }

        playerCards.getChildren().addAll(playerCardPanes);

        KeyValue valueSize = new KeyValue(playerCards.prefWidthProperty(), 180, Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), valueSize);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        if (!stopFlag) {
            timeline.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    startBettingRound();
                }
            });
        }

    }

    // Начало круга торгов и определение
    private void startBettingRound() {
        int betStartingPlayer = (dealer.getDealingPlayerOrder() + 2) % GAME_PLAYERS;
        dealer.incrementRoundCount();

        boolean playerPassed = false;

        final List<Transition> bettingTransitionsBeforePlayerChoice = new ArrayList<>();
        final List<Transition> bettingTransitionsAfterPlayerChoice = new ArrayList<>();
        System.out.println("HREHRE -- -- ");
        for (int i = betStartingPlayer; i < GAME_PLAYERS + betStartingPlayer; i++) {
            Player player = table.getPlayers().get(i % GAME_PLAYERS);
            if (player.isFolded() || player.isAllIn()) {
                continue;
            }
            if (player instanceof HumanPlayer) {
                playerPassed = true;
                continue;
            } else if (player instanceof ComputerPlayer) {
                //action = ((ComputerPlayer)player).getAction(dealer.getPlayerPossibleActions(i%GAME_PLAYERS),dealer.getRound());
            }
            if (!playerPassed)
                bettingTransitionsBeforePlayerChoice.add(getPlayerBettingTransition(i % GAME_PLAYERS));
            else
                bettingTransitionsAfterPlayerChoice.add(getPlayerBettingTransition(i % GAME_PLAYERS));

        }
        // выполнения действий игроков - ботов до человека
        if (bettingTransitionsBeforePlayerChoice.isEmpty()) {
            showPlayerTurnAnimation(bettingTransitionsAfterPlayerChoice);
        } else {
            SequentialTransition seqTransition = new SequentialTransition();
            seqTransition.getChildren().addAll(bettingTransitionsBeforePlayerChoice);
            if (!stopFlag){
                seqTransition.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        showPlayerTurnAnimation(bettingTransitionsAfterPlayerChoice);
                    }
                });
            }
            seqTransition.play();
        }
    }

    // Анимация хода человека (очередь)
    private void showPlayerTurnAnimation(final List<Transition> afterTransitions) {

        if (table.getPlayers().get(0).isFolded() || table.getPlayers().get(0).isAllIn()) {
            showAfterPlayerTransitions(afterTransitions);
        } else {
            roundMessageText.setText("Your Turn");
            roundMessageText.setOpacity(0);
            roundMessageBox.setOpacity(0);
            KeyValue valueOpacity = new KeyValue(roundMessageText.opacityProperty(), 1, Interpolator.LINEAR);
            KeyValue valueBoxOpacity = new KeyValue(roundMessageBox.opacityProperty(), 1, Interpolator.EASE_IN);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(500), valueOpacity, valueBoxOpacity);
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(keyFrame);
            timeline.setCycleCount(2);
            timeline.setAutoReverse(true);
            timeline.setDelay(Duration.millis(1500));
            if (!stopFlag) {
                timeline.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        showAndWaitPlayerControls(afterTransitions);
                    }
                });
            }
            timeline.play();
        }
    }

    // Анимация кнопок действия человека
    private void showAndWaitPlayerControls(final List<Transition> afterTransitions) {

        setPlayerControlsToDefault();
        List<PlayerAction> possibleActions = dealer.getPlayerPossibleActions(0);

        for (PlayerAction action : possibleActions) {
            if (action == PlayerAction.CALL)
                callButton.setDisable(false);
            else if (action == PlayerAction.RAISE)
                raiseButton.setDisable(false);
            else if (action == PlayerAction.CHECK)
                checkButton.setDisable(false);
            else if (action == PlayerAction.FOLD)
                foldButton.setDisable(false);
            else if (action == PlayerAction.ALL_IN)
                allInButton.setDisable(false);
        }
        playerActionsPane.setVisible(true);
        playerActionsPane.setOpacity(0);
        KeyValue valueOpacity = new KeyValue(playerActionsPane.opacityProperty(), 1, Interpolator.LINEAR);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), valueOpacity);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(keyFrame);
        timeline.setDelay(Duration.millis(500));
        if (!stopFlag) {
            timeline.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    showPlayerTimerAnimation(afterTransitions, false);
                }

            });
        }
        timeline.play();
    }

    // Анимация таймера на ход человека
    private void showPlayerTimerAnimation(final List<Transition> afterTransitions, boolean skip) {
        if (skip) {
            showAfterPlayerTransitions(afterTransitions);
            return;
        }
        playerTimeIndicatior.setVisible(true);
        playerTimeIndicatior.setProgress(0);
        playerSelectedAction = PlayerAction.FOLD;
        KeyValue valueProgress = new KeyValue(playerTimeIndicatior.progressProperty(), 1, Interpolator.LINEAR);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(PLAYER_TIME_OUT_SECONDS * 1000), valueProgress);
        timelinePlayerActionAnimation = new Timeline();
        timelinePlayerActionAnimation.getKeyFrames().add(keyFrame);
        timelinePlayerActionAnimation.setDelay(Duration.millis(500));
        if (!stopFlag) {
            timelinePlayerActionAnimation.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    hidePlayerControls();
                    setPlayerActionText(playerSelectedAction);
                    //handle single player default actions
                    if (playerSelectedAction == PlayerAction.FOLD)
                        dealer.setFlod(0);

                    showAfterPlayerTransitions(afterTransitions);
                }
            });
        }
        timelinePlayerActionAnimation.play();
    }

    // Анимация и действия ботов после хода игрока и потом новый крог взависимоти от раунда
    private void showAfterPlayerTransitions(List<Transition> afterTransitions) {

        if (afterTransitions.isEmpty()) {
            if (table.isSamePotValues() || dealer.allFoldedOrAllIn() || dealer.everyoneCalledAllInFoldedOrAllIn()) {
                if (dealer.getRound() == Round.PRE_FLOP)
                    startFlopRound();
                else if (dealer.getRound() == Round.FLOP)
                    startTurnRound();
                else if (dealer.getRound() == Round.TURN)
                    startRiverRound();
                else if (dealer.getRound() == Round.RIVER)
                    startShowDownRound();
            } else {
                startBettingRound();
            }
        } else {

            final SequentialTransition seqTransition = new SequentialTransition();
            seqTransition.getChildren().addAll(afterTransitions);

            seqTransition.play();
            if (!stopFlag) {
                seqTransition.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (table.isSamePotValues() || dealer.allFoldedOrAllIn() || dealer.everyoneCalledAllInFoldedOrAllIn()) {
                            if (dealer.getRound() == Round.PRE_FLOP) {
                                startFlopRound();
                            } else if (dealer.getRound() == Round.FLOP) {
                                startTurnRound();
                            } else if (dealer.getRound() == Round.TURN) {
                                startRiverRound();
                            } else if (dealer.getRound() == Round.RIVER) {
                                startShowDownRound();
                            }
                        } else {
                            startBettingRound();
                        }
                    }
                });
            }
        }
    }

    private void setPlayerControlsToDefault() {
        raiseButton.setDisable(true);
        callButton.setDisable(true);
        checkButton.setDisable(true);
        foldButton.setDisable(true);
        allInButton.setDisable(true);
    }

    private Transition getPlayerBettingTransition(final int playerPos) {
        Rectangle messageBox = null;
        Text messageText = null;
        switch (playerPos) {
            case 0:
                messageBox = singlePlayerMessageBox;
                messageText = singlePlayerMessageText;
                break;
            case 1:
                messageBox = player1MessageBox;
                messageText = player1MessageText;
                break;
            case 2:
                messageBox = player2MessageBox;
                messageText = player2MessageText;
                break;
            case 3:
                messageBox = player3MessageBox;
                messageText = player3MessageText;
                break;
            case 4:
                messageBox = player4MessageBox;
                messageText = player4MessageText;
                break;
        }

        final Text finalMessageText = messageText;
        final Rectangle finalMessageBox = messageBox;


        KeyValue valueBox = new KeyValue(messageBox.fillProperty(), Paint.valueOf("3b72ff"), Interpolator.EASE_IN);
        KeyValue valueTextThinking = new KeyValue(messageText.textProperty(), "Hmmm..");
        KeyFrame keyFrameBox = new KeyFrame(Duration.millis(300), valueBox, valueTextThinking);
        Timeline timelineBox = new Timeline();
        timelineBox.getKeyFrames().add(keyFrameBox);
        timelineBox.setCycleCount(5);
        timelineBox.setDelay(Duration.millis(700));
        timelineBox.setAutoReverse(true);


        SequentialTransition seqTrans = new SequentialTransition();
        seqTrans.getChildren().addAll(timelineBox);
        if (!stopFlag) {
            seqTrans.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    showAndDoPlayerAction(finalMessageText, playerPos);
                    finalMessageBox.setFill(Paint.valueOf("e4e4e4"));
                    finalMessageText.setText("");
                }
            });
        }
        return seqTrans;
    }

    // Анимация текста выбора действия игрока
    private void showAndDoPlayerAction(final Text finalMessageText, final int playerPos) {
        Player player = table.getPlayers().get(playerPos);
        PlayerAction action = ((ComputerPlayer) player).getAction(dealer.getPlayerPossibleActions(playerPos), table.getCommunityCards(), dealer.getRound(), dealer.getRoundCount()); //for now

        if (player.isCalledForAllIn() && dealer.allFoldedOrAllInExceptOne())
            return;

        if (action == PlayerAction.CALL) {
            dealer.getCallFrom(playerPos);
        } else if (action == PlayerAction.FOLD) {
            dealer.setFlod(playerPos);
        } else if (action == PlayerAction.RAISE) {
            dealer.getRaiseFrom(playerPos);
        } else if (action == PlayerAction.ALL_IN) {
            dealer.getAllInFrom(playerPos);
        } else if (action == PlayerAction.CHECK) {

        }
        if (action == PlayerAction.CALL && player.isAllIn())
            action = PlayerAction.ALL_IN;


        KeyValue valueText = new KeyValue(finalMessageText.textProperty(), action.toString());
        KeyFrame keyFrameText = new KeyFrame(Duration.millis(500), valueText);

        Timeline timelineText = new Timeline();
        timelineText.setDelay(Duration.millis(400));
        timelineText.getKeyFrames().add(keyFrameText);
        timelineText.play();
        if (!stopFlag) {
            timelineText.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                }
            });
        }
    }

    private void startFlopRound() {
        dealer.dealFlop();
        showFlopRoundMessage();
    }

    // Анимация названия ФЛОП раунда
    private void showFlopRoundMessage() {
        roundMessageText.setText("Flop Round");
        roundMessageBox.setOpacity(0);
        KeyValue valueOpacity = new KeyValue(roundMessageText.opacityProperty(), 1, Interpolator.EASE_OUT);
        KeyValue valueBoxOpacity = new KeyValue(roundMessageBox.opacityProperty(), 1, Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(2000), valueOpacity, valueBoxOpacity);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(keyFrame);
        timeline.setAutoReverse(true);
        timeline.setCycleCount(2);
        timeline.setDelay(Duration.millis(2000));
        if (!stopFlag) {
            timeline.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    showDealingFlopAnimations();
                }
            });
        }
        timeline.play();
    }

    // Анимация ФЛОПА раздачи карт от дилера
    private void showDealingFlopAnimations() {
        SequentialTransition seqTrans = getDealingTableCardsTransition(NUMBER_OF_FLOP_CARDS);
        seqTrans.play();
        if (!stopFlag) {
            seqTrans.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    showTableCards();
                }
            });
        }
    }

    private SequentialTransition getDealingTableCardsTransition(int numberOfCards) {
        List<Transition> transitions = new ArrayList<>();
        for (int i = 0; i < numberOfCards; i++)
            transitions.add(getDealingFlopAnimations());

        tableCards.setMinWidth(280);
        tableCards.setSpacing(10);
        SequentialTransition seqTrans = new SequentialTransition();
        seqTrans.getChildren().addAll(transitions);
        return seqTrans;
    }

    private Transition getDealingFlopAnimations() {
        final CardController showingCard = new CardController("DIAMONDS", Value.CardValue.TWO);
        final Pane card = showingCard.getCard();

        card.setLayoutX(325);
        card.setLayoutY(506);

        tableAnchor.getChildren().add(card);
        showingCard.faceDown();
        double destinationX = tableCards.getLayoutX(), destinationY = tableCards.getLayoutY();
        //double destinationX = 265, destinationY = 78;

        KeyValue valueX = new KeyValue(card.layoutXProperty(), destinationX, Interpolator.EASE_OUT);
        KeyValue valueY = new KeyValue(card.layoutYProperty(), destinationY, Interpolator.EASE_OUT);
        KeyValue valueScaleX = new KeyValue(card.scaleXProperty(), 1.2, Interpolator.EASE_OUT);
        KeyValue valueScaleY = new KeyValue(card.scaleYProperty(), 1.2, Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), valueX, valueY, valueScaleX, valueScaleY);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(keyFrame);


        RotateTransition rotateTransition = new RotateTransition(Duration.millis(500), card);
        rotateTransition.setByAngle(720f);
        ParallelTransition parallel = new ParallelTransition();
        parallel.getChildren().addAll(timeline, rotateTransition);

        if (!stopFlag) {
            parallel.setOnFinished(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    tableCards.getChildren().add(card);
                }
            });
        }
        return parallel;
    }

    // Анимация переворота карт на столе после флопа и начало следующего круга торгов или терна если все алл ин
    private void showTableCards() {

        tableCards.getChildren().clear();

        List<Card> cards = table.getCommunityCards();

        List<Pane> tableCardPanes = new ArrayList<Pane>(cards.size());

        for (Card card : cards) {
            CardController CardController = new CardController(card.getSuit().getSuitType().toString(), card.getValue().getCardValue());

            CardController.getCard().setScaleX(1.2);
            CardController.getCard().setScaleY(1.2);
            tableCardPanes.add(CardController.getCard());
            CardController.getCard().setRotationAxis(Rotate.Y_AXIS);
            CardController.getCard().setRotate(180);

            RotateTransition rt = new RotateTransition(Duration.millis(1000), CardController.getCard());
            rt.setAxis(Rotate.Y_AXIS);
            rt.setByAngle(180);
            rt.play();
        }

        tableCards.getChildren().addAll(tableCardPanes);

        KeyValue valueSize = new KeyValue(tableCards.prefWidthProperty(), 280, Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), valueSize);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        if (!stopFlag) {
            timeline.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    dealer.setRound(Round.FLOP);
                    dealer.setRoundCount(0);
                    if (dealer.allFoldedOrAllIn() || (dealer.allFoldedOrAllInExceptOne() && dealer.everyoneCalledAllInFoldedOrAllIn())) {
                        dealer.setRound(Round.TURN);
                        dealer.setRoundCount(0);
                        startTurnRound();
                    } else
                        startBettingRound();
                }
            });
        }
    }

    private void startTurnRound() {
        dealer.dealTurn();
        showTurnRoundMessage();
    }

    // Анимация названия раунда (ТЁРН)
    private void showTurnRoundMessage() {
        roundMessageText.setText("Turn Round");
        roundMessageBox.setOpacity(0);
        KeyValue valueOpacity = new KeyValue(roundMessageText.opacityProperty(), 1, Interpolator.EASE_OUT);
        KeyValue valueBoxOpacity = new KeyValue(roundMessageBox.opacityProperty(), 1, Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(2000), valueOpacity, valueBoxOpacity);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(keyFrame);
        timeline.setAutoReverse(true);
        timeline.setCycleCount(2);
        timeline.setDelay(Duration.millis(2000));
        if (!stopFlag) {
            timeline.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    showDealingTurnAnimations();
                }
            });
        }
        timeline.play();
    }

    // Анимация Терна раздачи карт от дилера на стол
    private void showDealingTurnAnimations() {
        SequentialTransition seqTrans = getDealingTableCardsTransition(NUMBER_OF_TURN_CARDS);

        seqTrans.play();
        if (!stopFlag) {
            seqTrans.setOnFinished(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    showTurnCard();
                }
            });
        }
    }

    // Переворот карты Терна и начало нового круга торгов или Ривер если все аллин
    private void showTurnCard() {
        tableCards.getChildren().remove(TURN_CARD_POS);


        Card turnCard = table.getCommunityCards().get(TURN_CARD_POS);


        CardController CardController = new CardController(turnCard.getSuit().getSuitType().toString(), turnCard.getValue().getCardValue());

        CardController.getCard().setScaleX(1.2);
        CardController.getCard().setScaleY(1.2);
        CardController.getCard().setRotationAxis(Rotate.Y_AXIS);
        CardController.getCard().setRotate(180);

        RotateTransition rt = new RotateTransition(Duration.millis(1000), CardController.getCard());
        rt.setAxis(Rotate.Y_AXIS);
        rt.setByAngle(180);
        rt.play();

        tableCards.getChildren().add(CardController.getCard());

        KeyValue valueSize = new KeyValue(tableCards.prefWidthProperty(), 280, Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), valueSize);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        if (!stopFlag) {
            timeline.setOnFinished(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    dealer.setRound(Round.TURN);
                    dealer.setRoundCount(0);
                    if (dealer.allFoldedOrAllIn() || (dealer.allFoldedOrAllInExceptOne() && dealer.everyoneCalledAllInFoldedOrAllIn())) {
                        dealer.setRound(Round.RIVER);
                        dealer.setRoundCount(0);
                        startRiverRound();
                    } else
                        startBettingRound();
                }
            });
        }
    }

    private void startRiverRound() {
        dealer.dealRiver();
        showRiverRoundMessage();
    }

    // Анимация названия раунда РИВЕР
    private void showRiverRoundMessage() {
        roundMessageText.setText("River Round");
        roundMessageBox.setOpacity(0);
        KeyValue valueOpacity = new KeyValue(roundMessageText.opacityProperty(), 1, Interpolator.EASE_OUT);
        KeyValue valueBoxOpacity = new KeyValue(roundMessageBox.opacityProperty(), 1, Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(2000), valueOpacity, valueBoxOpacity);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(keyFrame);
        timeline.setAutoReverse(true);
        timeline.setCycleCount(2);
        timeline.setDelay(Duration.millis(2000));
        if (!stopFlag) {
            timeline.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    showDealingRiverAnimations();
                }
            });
        }
        timeline.play();
    }

    // Анимация раздачи карт от Дилера на стол РИВЕР
    private void showDealingRiverAnimations() {
        SequentialTransition seqTrans = getDealingTableCardsTransition(NUMBER_OF_RIVER_CARDS);

        seqTrans.play();
        if (!stopFlag) {
            seqTrans.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    showRiverCard();
                }
            });
        }
    }

    // Анимация переворота карты РИВЕРА и начало нового круга торгов или ШОУДАУН если все алл ин
    private void showRiverCard() {
        tableCards.getChildren().remove(RIVER_CARD_POS);


        Card turnCard = table.getCommunityCards().get(RIVER_CARD_POS);


        CardController CardController = new CardController(turnCard.getSuit().getSuitType().toString(), turnCard.getValue().getCardValue());

        CardController.getCard().setScaleX(1.2);
        CardController.getCard().setScaleY(1.2);
        CardController.getCard().setRotationAxis(Rotate.Y_AXIS);
        CardController.getCard().setRotate(180);

        RotateTransition rt = new RotateTransition(Duration.millis(1000), CardController.getCard());
        rt.setAxis(Rotate.Y_AXIS);
        rt.setByAngle(180);
        rt.play();

        tableCards.getChildren().add(CardController.getCard());

        KeyValue valueSize = new KeyValue(tableCards.prefWidthProperty(), 280, Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), valueSize);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        if (!stopFlag) {
            timeline.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    dealer.setRound(Round.RIVER);
                    dealer.setRoundCount(0);
                    if (dealer.allFoldedOrAllIn() || (dealer.allFoldedOrAllInExceptOne() && dealer.everyoneCalledAllInFoldedOrAllIn())) {
                        startShowDownRound();
                    } else
                        startBettingRound();
                }
            });
        }
    }

    // Анимация названия раунда ШОУДАУН
    private void startShowDownRound() {
        roundMessageText.setText("Show Down");
        roundMessageBox.setOpacity(0);
        KeyValue valueOpacity = new KeyValue(roundMessageText.opacityProperty(), 1, Interpolator.EASE_OUT);
        KeyValue valueBoxOpacity = new KeyValue(roundMessageBox.opacityProperty(), 1, Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(2000), valueOpacity, valueBoxOpacity);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(keyFrame);
        timeline.setAutoReverse(true);
        timeline.setCycleCount(2);
        timeline.setDelay(Duration.millis(2000));
        if (!stopFlag) {
            timeline.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    adjustShowDownCards();
                }
            });
        }
        timeline.play();
    }

    // Анимация раздвижения карт всех игроков (раньше карты были друг на друге)
    private void adjustShowDownCards() {

        List<Timeline> cardAjustTimeLines = new ArrayList<>();

        for (HBox playerCards : playerCardsList) {
            KeyValue valuePosAdjustment = null;
            if (playerCards == playerCards3 || playerCards == playerCards4) {
                valuePosAdjustment = new KeyValue(playerCards.layoutXProperty(), playerCards.getLayoutX() - 50);
            }
            KeyValue valueX = new KeyValue(playerCards.prefWidthProperty(), 177, Interpolator.EASE_OUT);
            KeyFrame keyFrame = null;
            if (valuePosAdjustment != null)
                keyFrame = new KeyFrame(Duration.millis(500), valueX, valuePosAdjustment);
            else
                keyFrame = new KeyFrame(Duration.millis(500), valueX);
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(keyFrame);
            cardAjustTimeLines.add(timeline);
        }

        ParallelTransition transition = new ParallelTransition();
        transition.getChildren().addAll(cardAjustTimeLines);
        if (!stopFlag) {
            transition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    showPlayerCardsTransitions();
                }

            });
        }
        transition.play();
    }

    // Анимация переворота карт всех игроков во время шоудауна
    private void showPlayerCardsTransitions() {
        PlayerBestHighHand bestHand = dealer.getHighHandWinner();
        application.setHighHandWinner(table.getPlayers().get(bestHand.getPlayerPosition()));

        singlePlayerMessageText.setText(table.getPlayers().get(0).getWinningHandType());

        int showDownStartingPlayer = dealer.getDealingPlayerOrder() + 2;


        List<Transition> showDownTransitions = new ArrayList<>();

        for (int i = showDownStartingPlayer; i < GAME_PLAYERS + showDownStartingPlayer; i++) {
            Player player = table.getPlayers().get(i % GAME_PLAYERS);

            if (player instanceof HumanPlayer) {
                continue;
            } else if (player instanceof ComputerPlayer) {

            }
            showDownTransitions.add(getPlayerShowDownTransition(i % GAME_PLAYERS));

        }
        SequentialTransition seqShowCards = new SequentialTransition();
        seqShowCards.getChildren().addAll(showDownTransitions);
        if (!stopFlag) {
            seqShowCards.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    showHighHandWinner();
                }
            });
        }
        seqShowCards.play();
    }

    // Анимация какая комбинация у игрока
    private Transition getPlayerShowDownTransition(final int player) {

        ParallelTransition cardsRotating = new ParallelTransition();

        final String playerWinningHand = table.getPlayers().get(player).getWinningHandType();

        Rectangle messageBox = null;
        Text messageText = null;
        switch (player) {
            case 0:
                messageBox = singlePlayerMessageBox;
                messageText = singlePlayerMessageText;
                break;
            case 1:
                messageBox = player1MessageBox;
                messageText = player1MessageText;
                break;
            case 2:
                messageBox = player2MessageBox;
                messageText = player2MessageText;
                break;
            case 3:
                messageBox = player3MessageBox;
                messageText = player3MessageText;
                break;
            case 4:
                messageBox = player4MessageBox;
                messageText = player4MessageText;
                break;
        }

        final Text finalMessageText = messageText;

        final String[] handText = {"", "HIGH CARD", "PAIR", "TWO PAIRS", "THREE OF KIND", "STRAIGHT", "FLUSH", "FULL HOUSE",
                "FOUR OF KIND", "STRAIGHT FLUSH", "ROYAL FLUSH"};

        KeyValue valueBox = new KeyValue(messageBox.fillProperty(), Paint.valueOf("3ba40e"), Interpolator.EASE_IN);
        KeyValue valueTextThinking = new KeyValue(messageText.textProperty(), "");
        KeyFrame keyFrameBox = new KeyFrame(Duration.millis(300), valueBox, valueTextThinking);
        Timeline timelineBox = new Timeline();
        timelineBox.getKeyFrames().add(keyFrameBox);
        timelineBox.setCycleCount(5);
        timelineBox.setDelay(Duration.millis(2000));
        timelineBox.setAutoReverse(true);
        if (!stopFlag) {
            timelineBox.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    finalMessageText.setText(playerWinningHand);
                    if (table.getPlayers().get(player).isFolded())
                        finalMessageText.setText("FOLDED");
                    final HBox playerCards = getPlayerCardsHBox(player);
                    playerCards.getChildren().clear();

                    final List<Card> cards = table.getPlayers().get(player).getPlayerHand().getCards();
                    final List<Pane> playerCardPanes = new ArrayList<Pane>(cards.size());
                    for (Card card : cards) {
                        CardController CardController = new CardController(card.getSuit().getSuitType().toString(), card.getValue().getCardValue());

                        playerCardPanes.add(CardController.getCard());
                        CardController.getCard().setRotationAxis(Rotate.Y_AXIS);
                        CardController.getCard().setRotate(180);
                        RotateTransition rt = new RotateTransition(Duration.millis(1000), CardController.getCard());
                        rt.setAxis(Rotate.Y_AXIS);
                        rt.setByAngle(180);
                        rt.play();

                    }
                    playerCards.getChildren().addAll(playerCardPanes);
                }
            });
        }

        cardsRotating.getChildren().add(timelineBox);

        return cardsRotating;
    }


    // Анимация какой игрок победил в Хай комбинации
    private void showHighHandWinner() {

        roundMessageText.setText(application.getHighHandWinner().getName() + " Wins HighHand. " + application.getHighHandWinner().getWinningHandType().toUpperCase());
        roundMessageText.setStyle("-fx-font-size:32;");
        roundMessageBox.setOpacity(0);

        //System.out.println("WINNING HAND: " + application.getHighHandWinner().getWinningCards());

        KeyValue valueOpacity = new KeyValue(roundMessageText.opacityProperty(), 1, Interpolator.EASE_OUT);
        KeyValue valueBoxOpacity = new KeyValue(roundMessageBox.opacityProperty(), 1, Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(2000), valueOpacity, valueBoxOpacity);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(keyFrame);
        timeline.setAutoReverse(true);
        timeline.setCycleCount(2);
        timeline.setDelay(Duration.millis(2500));
        if (!stopFlag) {
            timeline.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    showLowHandWinner();
                }
            });
        }
        timeline.play();
    }

    // Анимация какой игрок выйграл с ЛОУ комбинаций
    private void showLowHandWinner() {
        final PlayerBestLowHand lowHand = dealer.getLowHandWinner();
        if (lowHand == null)
            roundMessageText.setText("Nobody wins Low Hand");
        else {
            application.setLowHandWinner(table.getPlayers().get(lowHand.getPlayerPosition()));
            roundMessageText.setText(table.getPlayers().get(lowHand.getPlayerPosition()).getName() + " Wins Low Hand. ");
            //System.out.println("WINNING LOW HAND: " + table.getPlayers().get(lowHand.getPlayerPosition()).getWinningLowCards());
        }
        roundMessageText.setStyle("-fx-font-size:32;");
        roundMessageBox.setOpacity(0);

        KeyValue valueOpacity = new KeyValue(roundMessageText.opacityProperty(), 1, Interpolator.EASE_OUT);
        KeyValue valueBoxOpacity = new KeyValue(roundMessageBox.opacityProperty(), 1, Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(2000), valueOpacity, valueBoxOpacity);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(keyFrame);
        timeline.setAutoReverse(true);
        timeline.setCycleCount(2);
        timeline.setDelay(Duration.millis(2500));
        if (!stopFlag) {
            timeline.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //if (lowHand != null)
                    showWinnersDetails();
                }
            });
        }
        timeline.play();
    }

    // Отображения панели с победителями их комбинации карт и их выйгрыш
    private void showWinnersDetails() {

        List<Player> winners = new ArrayList<>(2);
        winnersAnchorPane.setVisible(true);
        winnersAnchorPane.setScaleX(1);
        winnersAnchorPane.setScaleY(1);
        winnersAnchorPane.setOpacity(0);
        Player highWinner = application.getHighHandWinner();
        winners.add(highWinner);
        highWinnerName.setText(highWinner.getName());
        highHandType.setText(highWinner.getWinningHandType());

        highWinningCards.getChildren().clear();
        List<Card> cards = highWinner.getWinningCards();
        Collections.sort(cards);
        List<Pane> playerCardPanes = new ArrayList<Pane>(cards.size());

        for (Card card : cards) {
            CardController CardController = new CardController(card.getSuit().getSuitType().toString(), card.getValue().getCardValue());
            playerCardPanes.add(CardController.getCard());
        }

        highWinningCards.getChildren().addAll(playerCardPanes);
        highWinningCards.setScaleX(1.5);
        highWinningCards.setScaleY(1.5);

        lowWinnngCards.getChildren().clear();
        if (application.getLowHandWinner() == null) {
            lowWinnerName.setText("NO WINNER :(");
            lowWinnings.setText("$ 0");
        } else {
            Player lowWinner = application.getLowHandWinner();
            winners.add(lowWinner);
            lowWinnerName.setText(lowWinner.getName());
            lowWinnngCards.setScaleX(1.5);
            lowWinnngCards.setScaleY(1.5);
            lowWinnings.setText("$ " + table.getTablePot().getTablePotChips() / winners.size());
            List<Card> lowCards = lowWinner.getWinningLowCards();
            Collections.sort(lowCards);
            List<Pane> lowHandPanes = new ArrayList<Pane>(lowCards.size());

            for (Card card : lowCards) {
                CardController CardController = new CardController(card.getSuit().getSuitType().toString(), card.getValue().getCardValue());
                lowHandPanes.add(CardController.getCard());
            }
            lowWinnngCards.getChildren().addAll(lowHandPanes);

        }
        highWinnings.setText("$ " + table.getTablePot().getTablePotChips() / winners.size());

        dealer.awardWinners(winners);

        KeyValue valueOpacity = new KeyValue(winnersAnchorPane.opacityProperty(), 1, Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), valueOpacity);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(keyFrame);
        timeline.setDelay(Duration.millis(3000));
        timeline.play();
    }
}