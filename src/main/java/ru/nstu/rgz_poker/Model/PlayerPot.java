package ru.nstu.rgz_poker.Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class PlayerPot {

    DoubleProperty playerContribution;
    private Player player;

    public PlayerPot(Player player) {
        this.player = player;
        playerContribution = new SimpleDoubleProperty(0);
    }

    public void addToPot(double chips) {
        playerContribution.set(playerContribution.get() + chips);
    }

    public DoubleProperty playerContributionProperty() {
        return playerContribution;
    }

    public void clearPot() {
        playerContribution.set(0);
    }

    public Player getPlayer() {
        return player;
    }

    public double getPlayerContribution() {
        return playerContribution.get();
    }

}
