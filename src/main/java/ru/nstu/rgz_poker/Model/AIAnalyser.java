/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.nstu.rgz_poker.Model;

import ru.nstu.rgz_poker.Model.Player.PlayerAction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
class AIAnalyser {

    final static int LOW_HAND_MAX_VALUE = 30;

    PlayerAction getPreFlopAction(PlayerHand playerHand, List<PlayerAction> possibleActions, int roundCount) {

        int highValue = getHighHandValue(playerHand);

        int roundCountAdjustment = (roundCount - 1) * 2 + 1;

        PlayerAction action = PlayerAction.FOLD;

        int rankHandScore = 0;

        if (HighRankedHand.hasPair(playerHand.getCards())) {
            rankHandScore += 10;
        }

        if ((highValue + rankHandScore) <= (20 + roundCountAdjustment) && possibleActions.contains(PlayerAction.CHECK)) {
            action = PlayerAction.CHECK;
        } else if (highValue + rankHandScore <= (20 + roundCountAdjustment)) {
            action = PlayerAction.FOLD;
        } else if (highValue + rankHandScore < 59 + (roundCountAdjustment * (roundCount - 1) * 10) && possibleActions.contains(PlayerAction.CALL)) {
            action = PlayerAction.CALL;
        } else if (highValue + rankHandScore < 59 + (roundCountAdjustment * (roundCount - 1) * 10) && possibleActions.contains(PlayerAction.CHECK)) {
            action = PlayerAction.CHECK;
        } else if (highValue + rankHandScore < 59 + (roundCountAdjustment * (roundCount - 1) * 10) && possibleActions.contains(PlayerAction.ALL_IN)) {
            action = PlayerAction.ALL_IN;
        } else if (highValue + rankHandScore >= 59 && possibleActions.contains(PlayerAction.RAISE)) {
            action = PlayerAction.RAISE;
        } else if (highValue + rankHandScore >= 59 && possibleActions.contains(PlayerAction.CALL)) {
            action = PlayerAction.CALL;
        } else if (highValue + rankHandScore >= 59 && possibleActions.contains(PlayerAction.CHECK)) {
            action = PlayerAction.CHECK;
        } else if (highValue + rankHandScore >= 59 && possibleActions.contains(PlayerAction.ALL_IN)) {
            action = PlayerAction.ALL_IN;
        } else if (possibleActions.contains(PlayerAction.CHECK)) {
            action = PlayerAction.CHECK;
        }

        return action;
    }

    public int getHighHandValue(PlayerHand playerHand) {
        int value = 0;

        for (Card card : playerHand.getCards()) {
            value += card.getValue().getCardValue();
        }
        return value;
    }

    PlayerAction getFlopAction(PlayerHand playerHand, List<Card> communityCards, List<PlayerAction> possibleActions, int roundCount) {


        int[] scores = getScores(playerHand, communityCards);

        int highHandScore = scores[0];

        int lowHandScore = scores[1];

        int roundCountAdjustment = (roundCount - 1) * 3 + 1;
        //System.out.println("FLOP :" + highHandScore);

        PlayerAction action = PlayerAction.FOLD;
        if (highHandScore <= 0 + roundCountAdjustment && possibleActions.contains(PlayerAction.CHECK))
            action = PlayerAction.CHECK;
        else if (highHandScore <= 0 + roundCountAdjustment)
            action = PlayerAction.FOLD;
        else if (highHandScore < (9 + (roundCountAdjustment + ((roundCount - 1)) * 10 * roundCount)) && possibleActions.contains(PlayerAction.CHECK)) {
            action = PlayerAction.CHECK;
        } else if (highHandScore < (30 + (roundCountAdjustment + ((roundCount - 1)) * 30 * roundCount)) && possibleActions.contains(PlayerAction.CALL)) {
            action = PlayerAction.CALL;
        } else if (highHandScore < (30 + (roundCountAdjustment + ((roundCount - 1)) * 30 * roundCount)) && possibleActions.contains(PlayerAction.CHECK)) {
            action = PlayerAction.CHECK;
        } else if (highHandScore < (30 + (roundCountAdjustment + ((roundCount - 1)) * 30 * roundCount)) && possibleActions.contains(PlayerAction.ALL_IN)) {
            action = PlayerAction.ALL_IN;
        } else if (highHandScore < (60 + roundCountAdjustment) && possibleActions.contains(PlayerAction.RAISE)) {
            action = PlayerAction.RAISE;
        } else if (highHandScore < (60 + roundCountAdjustment) && possibleActions.contains(PlayerAction.CALL)) {
            action = PlayerAction.CALL;
        } else if (highHandScore < (60 + roundCountAdjustment) && possibleActions.contains(PlayerAction.CHECK)) {
            action = PlayerAction.CHECK;
        } else if (highHandScore < (60 + roundCountAdjustment) && possibleActions.contains(PlayerAction.ALL_IN)) {
            action = PlayerAction.ALL_IN;
        } else if (highHandScore > 99 && possibleActions.contains(PlayerAction.ALL_IN)) {
            action = PlayerAction.ALL_IN;
        }

        int lowHandAdjusted = LOW_HAND_MAX_VALUE - lowHandScore;
        int lowRoundCountAdjustment = (roundCount - 1) * 2 + 1;
        if (action == PlayerAction.FOLD || action == PlayerAction.CHECK) {
            if ((lowHandAdjusted > 9 + lowRoundCountAdjustment) && possibleActions.contains(PlayerAction.RAISE))
                action = PlayerAction.RAISE;
            else if ((lowHandAdjusted > 4 + lowRoundCountAdjustment) && possibleActions.contains(PlayerAction.CALL))
                action = PlayerAction.CALL;
            else if ((lowHandAdjusted >= 0) && possibleActions.contains(PlayerAction.CHECK))
                action = PlayerAction.CHECK;
        }

        if (action == PlayerAction.FOLD) {
            if (possibleActions.contains(PlayerAction.CHECK))
                action = PlayerAction.CHECK;
        }

        //System.out.println("FLOP LOW" + lowHandAdjusted);
        return action;
    }

    PlayerAction getTurnAction(PlayerHand playerHand, List<Card> communityCards, List<PlayerAction> possibleActions, int roundCount) {
        int roundCountAdjustment = (roundCount - 1) * 5 + 1;

        int[] scores = getScores(playerHand, communityCards);
        int highHandScore = scores[0];
        int lowHandScore = scores[1];
        //System.out.println("TURN HIGH" + highHandScore);
        PlayerAction action = PlayerAction.FOLD;
        if (highHandScore <= 10 + roundCountAdjustment && possibleActions.contains(PlayerAction.CHECK))
            action = PlayerAction.CHECK;
        else if (highHandScore <= 10 + roundCountAdjustment)
            action = PlayerAction.FOLD;
        else if (highHandScore < (25 + (roundCountAdjustment + ((roundCount - 1)) * 15 * roundCount)) && possibleActions.contains(PlayerAction.CHECK)) {
            action = PlayerAction.CHECK;
        } else if (highHandScore < (45 + (roundCountAdjustment + ((roundCount - 1)) * 35 * roundCount)) && possibleActions.contains(PlayerAction.CALL)) {
            action = PlayerAction.CALL;
        } else if (highHandScore < (45 + (roundCountAdjustment + ((roundCount - 1)) * 35 * roundCount)) && possibleActions.contains(PlayerAction.CHECK)) {
            action = PlayerAction.CHECK;
        } else if (highHandScore < (45 + (roundCountAdjustment + ((roundCount - 1)) * 35 * roundCount)) && possibleActions.contains(PlayerAction.ALL_IN)) {
            action = PlayerAction.ALL_IN;
        } else if (highHandScore < (130 + roundCountAdjustment) && possibleActions.contains(PlayerAction.RAISE)) {
            action = PlayerAction.RAISE;
        } else if (highHandScore < (130 + roundCountAdjustment) && possibleActions.contains(PlayerAction.CALL)) {
            action = PlayerAction.CALL;
        } else if (highHandScore < (130 + roundCountAdjustment) && possibleActions.contains(PlayerAction.CHECK)) {
            action = PlayerAction.CHECK;
        } else if (highHandScore < (130 + roundCountAdjustment) && possibleActions.contains(PlayerAction.ALL_IN)) {
            action = PlayerAction.ALL_IN;
        } else if (highHandScore > 130 && possibleActions.contains(PlayerAction.ALL_IN)) {
            action = PlayerAction.ALL_IN;
        }

        int lowHandAdjusted = LOW_HAND_MAX_VALUE - lowHandScore;
        int lowRoundCountAdjustment = (roundCount - 1) * 2 + 1;
        if (action == PlayerAction.FOLD || action == PlayerAction.CHECK) {
            if ((lowHandAdjusted > 9 + lowRoundCountAdjustment) && possibleActions.contains(PlayerAction.RAISE))
                action = PlayerAction.RAISE;
            else if ((lowHandAdjusted > 4 + lowRoundCountAdjustment) && possibleActions.contains(PlayerAction.CALL))
                action = PlayerAction.CALL;
            else if ((lowHandAdjusted >= 0) && possibleActions.contains(PlayerAction.CHECK))
                action = PlayerAction.CHECK;
        }

        if (action == PlayerAction.FOLD) {
            if (possibleActions.contains(PlayerAction.CHECK))
                action = PlayerAction.CHECK;
        }

        //System.out.println("TURN LOW" + lowHandAdjusted);
        return action;
    }

    PlayerAction getRiverAction(PlayerHand playerHand, List<Card> communityCards, List<PlayerAction> possibleActions, int roundCount) {
        int roundCountAdjustment = (roundCount - 1) * 5 + 15;

        int[] scores = getScores(playerHand, communityCards);
        int highHandScore = scores[0];
        int lowHandScore = scores[1];
        //System.out.println("RIVER HIGH" + highHandScore);
        PlayerAction action = PlayerAction.FOLD;
        if (highHandScore <= 15 + roundCountAdjustment && possibleActions.contains(PlayerAction.CHECK))
            action = PlayerAction.CHECK;
        else if (highHandScore <= 25 + roundCountAdjustment)
            action = PlayerAction.FOLD;
        else if (highHandScore < (35 + (roundCountAdjustment + ((roundCount - 1)) * 20 * roundCount)) && possibleActions.contains(PlayerAction.CHECK)) {
            action = PlayerAction.CHECK;
        } else if (highHandScore < (50 + (roundCountAdjustment + ((roundCount - 1)) * 40 * roundCount)) && possibleActions.contains(PlayerAction.CALL)) {
            action = PlayerAction.CALL;
        } else if (highHandScore < (50 + (roundCountAdjustment + ((roundCount - 1)) * 40 * roundCount)) && possibleActions.contains(PlayerAction.CHECK)) {
            action = PlayerAction.CHECK;
        } else if (highHandScore < (50 + (roundCountAdjustment + ((roundCount - 1)) * 40 * roundCount)) && possibleActions.contains(PlayerAction.ALL_IN)) {
            action = PlayerAction.ALL_IN;
        } else if (highHandScore < (150 + roundCountAdjustment) && possibleActions.contains(PlayerAction.RAISE)) {
            action = PlayerAction.RAISE;
        } else if (highHandScore < (150 + roundCountAdjustment) && possibleActions.contains(PlayerAction.CALL)) {
            action = PlayerAction.CALL;
        } else if (highHandScore < (150 + roundCountAdjustment) && possibleActions.contains(PlayerAction.CHECK)) {
            action = PlayerAction.CHECK;
        } else if (highHandScore < (150 + roundCountAdjustment) && possibleActions.contains(PlayerAction.ALL_IN)) {
            action = PlayerAction.ALL_IN;
        } else if (highHandScore > 150 && possibleActions.contains(PlayerAction.ALL_IN)) {
            action = PlayerAction.ALL_IN;
        }

        int lowHandAdjusted = LOW_HAND_MAX_VALUE - lowHandScore;
        int lowRoundCountAdjustment = (roundCount - 1) * 2 + 1;
        if (action == PlayerAction.FOLD || action == PlayerAction.CHECK) {
            if ((lowHandAdjusted > 9 + lowRoundCountAdjustment) && possibleActions.contains(PlayerAction.RAISE))
                action = PlayerAction.RAISE;
            else if ((lowHandAdjusted > 4 + lowRoundCountAdjustment) && possibleActions.contains(PlayerAction.CALL))
                action = PlayerAction.CALL;
            else if ((lowHandAdjusted >= 0) && possibleActions.contains(PlayerAction.CHECK))
                action = PlayerAction.CHECK;
        }

        if (action == PlayerAction.FOLD) {
            if (possibleActions.contains(PlayerAction.CHECK))
                action = PlayerAction.CHECK;
        }

        //System.out.println("RIVER LOW" + lowHandAdjusted);
        return action;
    }

    private int[] getScores(PlayerHand playerHand, List<Card> communityCards) {
        int[] scores = new int[2];
        int highHandScore = 0;
        int lowHandScore = Integer.MAX_VALUE;
        List<Card> playerCards = playerHand.getCards();
        List<Card> analyseCards = new ArrayList<>();


        //PAIR SCORE
        if (HighRankedHand.hasPair(playerCards)) {
            highHandScore += 5;
        }

        // TWO PAIRS SCORE
        for (int i = 0; i < playerCards.size(); i++) {
            for (int j = i + 1; j < playerCards.size(); j++) {
                for (int p = 0; p < communityCards.size(); p++) {

                    analyseCards.clear();
                    analyseCards.add(playerCards.get(i));
                    analyseCards.add(communityCards.get(p));

                    if (HighRankedHand.hasPair(analyseCards)) {
                        highHandScore += 5;
                    }

                    for (int q = p + 1; q < communityCards.size(); q++) {
                        for (int r = q + 1; r < communityCards.size(); r++) {

                            analyseCards.clear();
                            analyseCards.add(playerCards.get(i));
                            analyseCards.add(playerCards.get(j));
                            analyseCards.add(communityCards.get(p));
                            analyseCards.add(communityCards.get(q));
                            analyseCards.add(communityCards.get(r));

                            if (HighRankedHand.straight(analyseCards) != null) {
                                highHandScore += 20;
                            }

                            if (HighRankedHand.flush(analyseCards) != null) {
                                highHandScore += 25;
                            }

                            List<Card> fullHouse = HighRankedHand.fullHouse(analyseCards);

                            if (fullHouse != null && fullHouse.size() == 2) {
                                highHandScore += 30;
                                //System.out.println("FULL HOUSE");
                            }

                            if (HighRankedHand.straightFlush(analyseCards) != null) {
                                highHandScore += 40;
                                //System.out.println("STRAIGHT FLUSH");
                            }
                            if (HighRankedHand.isRoyalFlush(analyseCards)) {
                                highHandScore += 50;
                                //System.out.println("ROYAL FLUSH");
                            }

                            int handLowScore = LowRankedHand.getLowHandScore(analyseCards);

                            if (handLowScore < lowHandScore)
                                lowHandScore = handLowScore;

                        }
                        analyseCards.clear();
                        analyseCards.add(playerCards.get(i));
                        analyseCards.add(playerCards.get(j));
                        analyseCards.add(communityCards.get(p));
                        analyseCards.add(communityCards.get(q));

                        List<Card> twoPairs = HighRankedHand.twoPairs(analyseCards);
                        if (twoPairs != null && !twoPairs.isEmpty()) {
                            highHandScore += 10;
                        }

                        List<Card> fourOfKind = HighRankedHand.fourOfaKind(analyseCards);

                        if (fourOfKind != null && !fourOfKind.isEmpty()) {
                            highHandScore += 35;
                            //System.out.println("FOUR OF KIND");
                        }

                        if (HighRankedHand.straight(analyseCards) != null) {
                            highHandScore += 8;
                        }

                        if (HighRankedHand.flush(analyseCards) != null) {
                            highHandScore += 9;
                        }
                    }
                    analyseCards.clear();
                    analyseCards.add(playerCards.get(i));
                    analyseCards.add(playerCards.get(j));
                    analyseCards.add(communityCards.get(p));

                    List<Card> threeOfKind = HighRankedHand.threeOfKind(analyseCards);

                    if (threeOfKind != null && !threeOfKind.isEmpty()) {
                        highHandScore += 15;
                    }

                    if (HighRankedHand.straight(analyseCards) != null) {
                        highHandScore += 5;
                    }

                    if (HighRankedHand.flush(analyseCards) != null) {
                        highHandScore += 6;
                    }

                }
            }
        }
        scores[0] = highHandScore;
        scores[1] = lowHandScore;
        return scores;
    }
}
