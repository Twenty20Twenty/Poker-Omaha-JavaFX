package ru.nstu.rgz_poker.Model;

import ru.nstu.rgz_poker.Model.Player.PlayerAction;

import java.util.ArrayList;
import java.util.List;

import static ru.nstu.rgz_poker.Model.ITableRules.NUMBER_OF_FLOP_CARDS;

public class Dealer implements IDealerRules, IGameRules {

    CardAnalyser cardAnalyser;
    private CardDeck deck;
    private Table table;
    private Round currentRound;

    private int roundCount;
    private int dealingPlayerOrder;


    public Dealer() {
        deck = new CardDeck();
        dealingPlayerOrder = 1;
        currentRound = Round.PRE_FLOP;
        roundCount = 0;
        cardAnalyser = new CardAnalyser();
    }

    public void clear() {
        cardAnalyser = new CardAnalyser();
    }

    public void incrementDealingPlayerOrder() {
        dealingPlayerOrder = (++dealingPlayerOrder) % GAME_PLAYERS;
    }

    public int incrementRoundCount() {
        roundCount++;
        return roundCount;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public void setRoundCount(int count) {
        roundCount = count;
    }

    public Round getRound() {
        return currentRound;
    }

    public void setRound(Round round) {
        this.currentRound = round;
    }

    public void shuffleDeck() {
        deck.shuffle();
    }

    public int getDealingPlayerOrder() {
        return dealingPlayerOrder;
    }

    public void setDealingPlayerOrder(int pos) {
        dealingPlayerOrder = pos;
    }

    public void collectBlinds() {
        getBigBlind();
        getSmallBlind();
    }

    private void getSmallBlind() {
        table.takeSmallBlindFromPlayer(dealingPlayerOrder);
    }

    private void getBigBlind() {
        table.takeBigBlindFromPlayer(dealingPlayerOrder + 1);
    }

    public void collectPlayerPots() {

    }

    private Player findHighHandWinner() {
        return null;
    }

    private Player findLowHandWinner() {
        return null;
    }

    public void dealFlop() {
        for (int i = 0; i < NUMBER_OF_FLOP_CARDS; i++)
            table.getCommunityCards().add(deck.getTopCard());
    }

    public void dealTurn() {
        table.getCommunityCards().add(deck.getTopCard());
    }

    public void dealRiver() {
        table.getCommunityCards().add(deck.getTopCard());
    }

    public void dealToPlayers() {
        List<Player> players = table.getPlayers();
        dealToPlayers(players);
    }

    public void dealToPlayers(List<Player> players) {

        for (int i = 0, j = dealingPlayerOrder; i < players.size() * PLAYER_HAND_SIZE; i++, j++) {
            players.get(j % players.size()).giveCard(deck.getTopCard());
        }
    }

    public CardDeck getDeck() {
        return deck;
    }

    public void setDeck(CardDeck deck) {
        this.deck = deck;
    }

    public PlayerBestHighHand getHighHandWinner() {
        return cardAnalyser.getHighHandWinner(table.getPlayers(), table.getCommunityCards());
    }

    public PlayerBestLowHand getLowHandWinner() {
        return cardAnalyser.getLowHandWinner();
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public List<PlayerAction> getPlayerPossibleActions(int i) {
        List<PlayerAction> possibleActions = new ArrayList<>();

        final double HIGHEST_POT_VALUE = table.getHighestPotValue();

        final double RAISE_VALUE = table.getRaiseAmount();

        //FOLD
        possibleActions.add(PlayerAction.FOLD);

        //ALLIN
        if (table.getPlayers().get(i).getPlayerChips() > 0) {
            possibleActions.add(PlayerAction.ALL_IN);
        }

        //CALL
        if (table.getPlayers().get(i).getPlayerChips() >= HIGHEST_POT_VALUE - (table.getTablePot().getPlayerPots().get(i).getPlayerContribution())
                && table.getTablePot().getPlayerPots().get(i).getPlayerContribution() < HIGHEST_POT_VALUE) {
            possibleActions.add(PlayerAction.CALL);
        }

        //CHECK
        if (table.getTablePot().getPlayerPots().get(i).getPlayerContribution() == HIGHEST_POT_VALUE
                && table.getPlayers().get(i).getPlayerChips() > 0) {
            possibleActions.add(PlayerAction.CHECK);
        }

        //RAISE
        if (table.getPlayers().get(i).getPlayerChips() >= RAISE_VALUE) {
            possibleActions.add(PlayerAction.RAISE);
        }

        return possibleActions;
    }

    public void getCallFrom(int playerPosition) {
        Player player = table.getPlayers().get(playerPosition);
        PlayerPot playerPot = table.getTablePot().getPlayerPots().get(playerPosition);
        double playerPotAmount = playerPot.getPlayerContribution();
        double amount = player.takeChips(table.getHighestPotValue() - playerPotAmount);
        table.getTablePot().getPlayerPots().get(playerPosition).addToPot(amount);

        if (table.ifAnyoneAllIn()) {
            player.setCalledForAllIn(true);
        }
        if (player.getPlayerChips() == 0) {
            player.setAllIn(true);
        }
    }

    public void setFlod(int position) {
        Player player = table.getPlayers().get(position);
        player.setFolded(true);

    }

    public void getRaiseFrom(int playerPosition) {
        Player player = table.getPlayers().get(playerPosition);
        PlayerPot playerPot = table.getTablePot().getPlayerPots().get(playerPosition);
        double playerPotAmount = playerPot.getPlayerContribution();
        double amount = player.takeChips(table.getRaiseAmount() - playerPotAmount);
        playerPot.addToPot(amount);
        table.setHighestPotValue(playerPot.getPlayerContribution());
        if (player.getPlayerChips() == 0) {
            player.setAllIn(true);
        }
    }

    public void getAllInFrom(int playerPosition) {
        Player player = table.getPlayers().get(playerPosition);
        PlayerPot playerPot = table.getTablePot().getPlayerPots().get(playerPosition);
        double amount = player.takeChips(player.getPlayerChips());
        playerPot.addToPot(amount);
        if (playerPot.getPlayerContribution() > table.getHighestPotValue())
            table.setHighestPotValue(playerPot.getPlayerContribution());
        player.setAllIn(true);
    }

    public boolean allOtherFoldedOrAllIn(int playerPos) {

        for (int i = playerPos + 1; i < playerPos + GAME_PLAYERS; i++) {
            if (table.getPlayers().get(i % GAME_PLAYERS).isFolded()
                    || table.getPlayers().get(i % GAME_PLAYERS).isAllIn())
                continue;
            else
                return false;
        }

        return true;
    }

    public boolean allFoldedOrAllIn() {
        for (int i = 0; i < GAME_PLAYERS; i++) {
            if (table.getPlayers().get(i).isFolded()
                    || table.getPlayers().get(i).isAllIn())
                continue;
            else
                return false;
        }
        return true;
    }

    public boolean allFoldedOrAllInExceptOne() {
        int count = 0;
        for (int i = 0; i < GAME_PLAYERS; i++) {
            if (table.getPlayers().get(i).isFolded()
                    || table.getPlayers().get(i).isAllIn())
                continue;
            else {
                count++;
            }
        }
        return count == 1;
    }

    public void awardWinners(List<Player> winners) {
        if (winners == null || winners.isEmpty())
            return;

        double tablePotValue = table.getTablePot().getTablePotChips();

        for (Player winner : winners) {
            double givingAmount = tablePotValue / winners.size();
            winner.awardChips(givingAmount);
        }
        table.clearTablePot();
    }

    public boolean everyoneCalledAllInFoldedOrAllIn() {
        for (Player player : table.getPlayers()) {
            if (player.isAllIn() || player.isFolded() || player.isCalledForAllIn()) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    public enum Round {
        PRE_FLOP, FLOP, TURN, RIVER
    }
}
