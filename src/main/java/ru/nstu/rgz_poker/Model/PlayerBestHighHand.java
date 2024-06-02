/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.nstu.rgz_poker.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class PlayerBestHighHand implements IRankHandsRules {

    private int playerPosition;

    private List<Card> winningCards;

    private int playerBestHand;

    public PlayerBestHighHand(int pos) {
        playerPosition = pos;
        winningCards = new ArrayList<>();
        playerBestHand = 0;
    }

    public int getPlayerBestHand() {
        return playerBestHand;
    }

    public void setPlayerBestHand(int hand) {
        playerBestHand = hand;
    }

    public List<Card> getWinningCards() {
        return winningCards;
    }

    public void setWinningCards(List<Card> cards) {
        winningCards = cards;
    }

    public int getPlayerPosition() {
        return playerPosition;
    }

}
