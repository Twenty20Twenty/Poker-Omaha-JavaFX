/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.nstu.rgz_poker.Model;

import ru.nstu.rgz_poker.Model.Dealer.Round;

import java.util.List;

/**
 * @author Administrator
 */
public class ComputerPlayer extends Player {
    AIAnalyser aiAnalyser;

    public ComputerPlayer(String name, int order) {
        super(name, order);
        aiAnalyser = new AIAnalyser();
    }

    public PlayerAction getAction(List<PlayerAction> possibleActions, List<Card> communityCards, Round currentRound, final int roundCount) {

        PlayerAction action = PlayerAction.FOLD;

        if (currentRound == Round.PRE_FLOP) {
            action = aiAnalyser.getPreFlopAction(playerHand, possibleActions, roundCount);
        } else if (currentRound == Round.FLOP) {
            action = aiAnalyser.getFlopAction(playerHand, communityCards, possibleActions, roundCount);
        } else if (currentRound == Round.TURN) {
            action = aiAnalyser.getTurnAction(playerHand, communityCards, possibleActions, roundCount);
        } else if (currentRound == Round.RIVER) {
            action = aiAnalyser.getRiverAction(playerHand, communityCards, possibleActions, roundCount);
        }
        return action;
    }
}
