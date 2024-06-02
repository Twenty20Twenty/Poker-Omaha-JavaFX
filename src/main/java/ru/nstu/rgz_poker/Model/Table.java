package ru.nstu.rgz_poker.Model;

import java.util.ArrayList;
import java.util.List;

public class Table implements ITableRules {

    private TablePot tablePot;

    private List<Player> playerList;

    private int bigBlind;
    private int smallBlind;

    private double highestPotValue;

    private List<Card> communityCards;


    public Table(List<Player> players) {
        playerList = players;
        tablePot = new TablePot(playerList);
        bigBlind = INITIAL_BIG_BLIND;
        smallBlind = INITIAL_SMALL_BLIND;
        communityCards = new ArrayList<>(5);
    }

    public void clear() {
        communityCards.clear();
        communityCards = new ArrayList<>();
        tablePot = null;
        tablePot = new TablePot(playerList);
    }

    public double getHighestPotValue() {
        return highestPotValue;
    }

    public void setHighestPotValue(double value) {
        highestPotValue = value;
    }

    public List<Card> getCommunityCards() {
        return communityCards;
    }

    public double getRaiseAmount() {
        return highestPotValue + bigBlind;
    }

    public TablePot getTablePot() {
        return tablePot;
    }

    private double getTableChips() {
        return tablePot.getTablePotChips();
    }

    public List<Player> getPlayers() {
        return playerList;
    }

    public int numberOfPlayers() {
        return (playerList == null) ? 0 : playerList.size();
    }

    public void takeBigBlindFromPlayer(int i) {

        playerList.get(i % numberOfPlayers()).takeChips(bigBlind);
        getTablePot().getPlayerPots().get(i % numberOfPlayers()).addToPot(bigBlind);

        highestPotValue = bigBlind;
    }

    public void takeSmallBlindFromPlayer(int i) {
        playerList.get(i % numberOfPlayers()).takeChips(smallBlind);
        getTablePot().getPlayerPots().get(i % numberOfPlayers()).addToPot(smallBlind);
    }

    public boolean isSamePotValues() {
        List<PlayerPot> pots = tablePot.getPlayerPots();

        int startPosition = 0;

        for (int i = 0; i < pots.size(); i++) {
            Player player = getPlayers().get(i);
            if (player.isFolded())
                continue;
            startPosition = i;
            break;
        }

        double potCheckValue = tablePot.getPlayerPots().get(startPosition).getPlayerContribution();

        for (int i = startPosition + 1; i < startPosition + pots.size(); i++) {
            if (potCheckValue != tablePot.getPlayerPots().get(i % pots.size()).getPlayerContribution()
                    && !getPlayers().get(i % pots.size()).isFolded())
                return false;
        }
        return true;
    }

    public void clearTablePot() {
        tablePot.clear();
    }

    public void setBligBlind(int value) {
        bigBlind = value;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public void setSmallBlind(int value) {
        smallBlind = value;
    }

    public void incrementBlinds() {
        bigBlind += BIG_BLIND_INCREMENT;
        smallBlind = bigBlind / 2;
    }

    public boolean isEveryoneAllIn() {

        for (Player player : playerList) {
            if (player.isAllIn())
                continue;
            else
                return false;
        }

        return true;
    }

    public boolean ifAnyoneAllIn() {
        for (Player player : getPlayers()) {
            if (player.isAllIn())
                return true;
        }

        return false;
    }

}
