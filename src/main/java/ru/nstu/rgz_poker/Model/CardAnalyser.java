package ru.nstu.rgz_poker.Model;

import java.util.ArrayList;
import java.util.List;

import static ru.nstu.rgz_poker.Model.IDealerRules.PLAYER_HAND_SIZE;


public class CardAnalyser implements IRankHandsRules {

    final static int LOW_HAND_MAX_VALUE = 30;

    PlayerBestLowHand playerBestLowHand = null;
    PlayerBestHighHand playerBestHighHand = null;

    public PlayerBestHighHand getHighHandWinner(List<Player> players, List<Card> communityCards) {

        List<PlayerBestHighHand> playersBestHands = new ArrayList<>();

        List<PlayerBestLowHand> playerBestLowHands = new ArrayList<>();

        for (int player = 0; player < players.size(); player++) {

            Player currentPlayer = players.get(player);
            if (currentPlayer.isFolded()) {
                playersBestHands.add(new PlayerBestHighHand(player));
                playerBestLowHands.add(new PlayerBestLowHand(player));
                continue;
            }

            PlayerBestHighHand bestHand = new PlayerBestHighHand(player);

            PlayerBestLowHand lowHand = new PlayerBestLowHand(player);

            List<Card> playerCards = players.get(player).getPlayerHand().getCards();

            for (int i = 0; i < PLAYER_HAND_SIZE; i++) {
                for (int j = i + 1; j < PLAYER_HAND_SIZE; j++) {
                    for (int p = 0; p < communityCards.size(); p++) {
                        for (int q = p + 1; q < communityCards.size(); q++) {
                            for (int r = q + 1; r < communityCards.size(); r++) {
                                List<Card> analyseCards = new ArrayList<>();
                                analyseCards.add(playerCards.get(i));
                                analyseCards.add(playerCards.get(j));
                                analyseCards.add(communityCards.get(p));
                                analyseCards.add(communityCards.get(q));
                                analyseCards.add(communityCards.get(r));

                                List<Card> fourOfKind = HighRankedHand.fourOfaKind(analyseCards);
                                List<Card> fullHouse = HighRankedHand.fullHouse(analyseCards);
                                List<Card> threeOfKind = HighRankedHand.threeOfKind(analyseCards);
                                List<Card> twoPairs = HighRankedHand.twoPairs(analyseCards);
                                Card straightFlush = HighRankedHand.straightFlush(analyseCards);
                                Card flush = HighRankedHand.flush(analyseCards);
                                Card straight = HighRankedHand.straight(analyseCards);
                                Card pair = HighRankedHand.getPair(analyseCards);

                                if (HighRankedHand.isRoyalFlush(analyseCards)) {
                                    bestHand.setPlayerBestHand(ROYAL_FLUSH);
                                    currentPlayer.setWinningCards(analyseCards);
                                    currentPlayer.setWinningHandType("ROYAL FLUSH");
                                } else if (straightFlush != null) {
                                    List<Card> winningCards = new ArrayList<>();
                                    winningCards.add(straightFlush);
                                    currentPlayer.setWinningCards(analyseCards);
                                    if (bestHand.getPlayerBestHand() < STRAIGHT_FLUSH) {
                                        bestHand.setPlayerBestHand(STRAIGHT_FLUSH);
                                        bestHand.setWinningCards(winningCards);
                                        currentPlayer.setWinningCards(analyseCards);
                                        currentPlayer.setWinningHandType("STRAIGHT FLUSH");
                                    } else if (bestHand.getPlayerBestHand() == STRAIGHT_FLUSH) {
                                        Card tieCard = bestHand.getWinningCards().get(0);
                                        if (tieCard.getValue().getCardValue() < straightFlush.getValue().getCardValue()) {
                                            bestHand.setWinningCards(winningCards);
                                            currentPlayer.setWinningCards(analyseCards);
                                        }
                                    }
                                } else if (fourOfKind != null && !fourOfKind.isEmpty()) {
                                    if (bestHand.getPlayerBestHand() < FOUR_OF_KIND) {
                                        bestHand.setPlayerBestHand(FOUR_OF_KIND);
                                        bestHand.setWinningCards(fourOfKind);
                                        currentPlayer.setWinningCards(analyseCards);
                                        currentPlayer.setWinningHandType("FOUR OF KIND");
                                    } else if (bestHand.getPlayerBestHand() == FOUR_OF_KIND) {
                                        Card tieCard = bestHand.getWinningCards().get(0);
                                        if (tieCard.getValue().getCardValue() < fourOfKind.get(0).getValue().getCardValue()) {
                                            bestHand.setWinningCards(fourOfKind);
                                            currentPlayer.setWinningCards(analyseCards);
                                        }
                                    }
                                } else if (fullHouse != null && fullHouse.size() == 2) {
                                    if (bestHand.getPlayerBestHand() < FULL_HOUSE) {
                                        bestHand.setPlayerBestHand(FULL_HOUSE);
                                        bestHand.setWinningCards(fullHouse);
                                        currentPlayer.setWinningCards(analyseCards);
                                        currentPlayer.setWinningHandType("FULL HOUSE");
                                    } else if (bestHand.getPlayerBestHand() == FULL_HOUSE) {
                                        Card tieCard = bestHand.getWinningCards().get(0);
                                        if (tieCard.getValue().getCardValue() < fullHouse.get(0).getValue().getCardValue()) {
                                            bestHand.setWinningCards(fullHouse);
                                            currentPlayer.setWinningCards(analyseCards);
                                        }
                                    }
                                } else if (flush != null) {
                                    List<Card> winningCards = new ArrayList<>();
                                    winningCards.add(flush);
                                    if (bestHand.getPlayerBestHand() < FLUSH) {
                                        bestHand.setPlayerBestHand(FLUSH);
                                        bestHand.setWinningCards(winningCards);
                                        currentPlayer.setWinningCards(analyseCards);
                                        currentPlayer.setWinningHandType("FLUSH");
                                    } else if (bestHand.getPlayerBestHand() == FLUSH) {
                                        Card tieCard = bestHand.getWinningCards().get(0);
                                        if (tieCard.getValue().getCardValue() < flush.getValue().getCardValue()) {
                                            bestHand.setWinningCards(winningCards);
                                            currentPlayer.setWinningCards(analyseCards);
                                        }
                                    }
                                } else if (straight != null) {
                                    List<Card> winningCards = new ArrayList<>();
                                    winningCards.add(straight);
                                    if (bestHand.getPlayerBestHand() < STRAIGHT) {
                                        bestHand.setPlayerBestHand(STRAIGHT);
                                        bestHand.setWinningCards(winningCards);
                                        currentPlayer.setWinningCards(analyseCards);
                                        currentPlayer.setWinningHandType("STRAIGHT");
                                    } else if (bestHand.getPlayerBestHand() == STRAIGHT) {
                                        Card tieCard = bestHand.getWinningCards().get(0);
                                        if (tieCard.getValue().getCardValue() < straight.getValue().getCardValue()) {
                                            bestHand.setWinningCards(winningCards);
                                            currentPlayer.setWinningCards(analyseCards);
                                        }
                                    }
                                } else if (threeOfKind != null && !threeOfKind.isEmpty()) {
                                    if (bestHand.getPlayerBestHand() < THREE_OF_KIND) {
                                        bestHand.setPlayerBestHand(THREE_OF_KIND);
                                        bestHand.setWinningCards(threeOfKind);
                                        currentPlayer.setWinningCards(analyseCards);
                                        currentPlayer.setWinningHandType("THREE OF KIND");
                                    } else if (bestHand.getPlayerBestHand() == THREE_OF_KIND) {
                                        Card tieCard = bestHand.getWinningCards().get(0);
                                        if (tieCard.getValue().getCardValue() < threeOfKind.get(0).getValue().getCardValue()) {
                                            bestHand.setWinningCards(threeOfKind);
                                            currentPlayer.setWinningCards(analyseCards);
                                        }
                                    }
                                } else if (twoPairs != null && !twoPairs.isEmpty()) {
                                    if (bestHand.getPlayerBestHand() < TWO_PAIRS) {
                                        bestHand.setPlayerBestHand(TWO_PAIRS);
                                        bestHand.setWinningCards(twoPairs);
                                        currentPlayer.setWinningCards(analyseCards);
                                        currentPlayer.setWinningHandType("TWO PAIRS");
                                    } else if (bestHand.getPlayerBestHand() == TWO_PAIRS) {
                                        Card tieCard = bestHand.getWinningCards().get(0);
                                        if (tieCard.getValue().getCardValue() < twoPairs.get(0).getValue().getCardValue()) {
                                            bestHand.setWinningCards(twoPairs);
                                            currentPlayer.setWinningCards(analyseCards);
                                        } else if (tieCard.getValue().getCardValue() == twoPairs.get(0).getValue().getCardValue()) {
                                            tieCard = bestHand.getWinningCards().get(1);
                                            if (tieCard.getValue().getCardValue() < twoPairs.get(1).getValue().getCardValue()) {
                                                bestHand.setWinningCards(twoPairs);
                                                currentPlayer.setWinningCards(analyseCards);
                                            } else if (tieCard.getValue().getCardValue() == twoPairs.get(1).getValue().getCardValue()) {
                                                tieCard = bestHand.getWinningCards().get(2);
                                                if (tieCard.getValue().getCardValue() < twoPairs.get(2).getValue().getCardValue()) {
                                                    bestHand.setWinningCards(twoPairs);
                                                    currentPlayer.setWinningCards(analyseCards);
                                                }
                                            }
                                        }
                                    }
                                } else if (pair != null) {
                                    List<Card> winningCards = new ArrayList<>();
                                    winningCards.add(pair);
                                    if (bestHand.getPlayerBestHand() < PAIR) {
                                        bestHand.setPlayerBestHand(PAIR);
                                        bestHand.setWinningCards(winningCards);
                                        currentPlayer.setWinningCards(analyseCards);
                                        currentPlayer.setWinningHandType("PAIR");
                                    } else if (bestHand.getPlayerBestHand() == PAIR) {
                                        Card tieCard = bestHand.getWinningCards().get(0);
                                        if (tieCard.getValue().getCardValue() < pair.getValue().getCardValue()) {
                                            bestHand.setWinningCards(winningCards);
                                            currentPlayer.setWinningCards(analyseCards);
                                        }
                                    }
                                } else {
                                    if (bestHand.getPlayerBestHand() < HIGH_CARD) {
                                        Card highCard = HighRankedHand.getHighRankCard(analyseCards);
                                        bestHand.setPlayerBestHand(HIGH_CARD);
                                        List<Card> winningCards = new ArrayList<>();
                                        winningCards.add(highCard);
                                        bestHand.setWinningCards(winningCards);
                                        currentPlayer.setWinningCards(analyseCards);
                                        currentPlayer.setWinningHandType("HIGH CARD");
                                    } else if (bestHand.getPlayerBestHand() == HIGH_CARD) {
                                        Card highCard = HighRankedHand.getHighRankCard(analyseCards);
                                        if (bestHand.getWinningCards().get(0).getValue().getCardValue() < highCard.getValue().getCardValue()) {
                                            List<Card> winningCards = new ArrayList<>();
                                            winningCards.add(highCard);
                                            bestHand.setWinningCards(winningCards);
                                            currentPlayer.setWinningCards(analyseCards);
                                        }
                                    }
                                }
                                //LOW HAND ANALYSIS

                                int score = LowRankedHand.getLowHandScore(analyseCards);
                                int adjustedScore = LOW_HAND_MAX_VALUE - score;

                                if (lowHand.getPlayerBestLowHand() < adjustedScore && adjustedScore >= 0) {
                                    lowHand.setPlayerBestLowHand(adjustedScore);
                                    currentPlayer.setWinningLowCards(analyseCards);
                                }
                            }
                        }
                    }
                }
            }

            playersBestHands.add(bestHand);
            playerBestLowHands.add(lowHand);

        }
        int winnerPos = getWinnerPos(playersBestHands);
        int lowWinnerPos = getLowWinnerPos(playerBestLowHands);

        playerBestHighHand = playersBestHands.get(winnerPos);
        if (lowWinnerPos < 0)
            playerBestLowHands = null;
        else
            playerBestLowHand = playerBestLowHands.get(lowWinnerPos);

        return playerBestHighHand;
    }

    public PlayerBestLowHand getLowHandWinner() {
        return playerBestLowHand;
    }

    private int getLowWinnerPos(List<PlayerBestLowHand> playerLowHands) {
        int currentBest = -5;
        int currentBestPlayer = -1;

        for (int i = 0; i < playerLowHands.size(); i++) {
            if (currentBest < playerLowHands.get(i).getPlayerBestLowHand()) {
                currentBest = playerLowHands.get(i).getPlayerBestLowHand();
                currentBestPlayer = i;
            }
        }

        return currentBestPlayer;
    }

    private int getWinnerPos(List<PlayerBestHighHand> playersBestHands) {

        int currentBest = playersBestHands.get(0).getPlayerBestHand();

        int currentBestPlayer = 0;

        for (int i = 1; i < playersBestHands.size(); i++) {
            if (currentBest < playersBestHands.get(i).getPlayerBestHand()) {
                currentBest = playersBestHands.get(i).getPlayerBestHand();
                currentBestPlayer = i;
            } else if (currentBest == playersBestHands.get(i).getPlayerBestHand()) {
                if (currentBest == STRAIGHT_FLUSH) {
                    Card tieCard = playersBestHands.get(currentBestPlayer).getWinningCards().get(0);
                    if (tieCard.getValue().getCardValue() < playersBestHands.get(i).getWinningCards().get(0).getValue().getCardValue()) {
                        currentBestPlayer = i;
                    }
                } else if (currentBest == FOUR_OF_KIND) {
                    Card tieCard = playersBestHands.get(currentBestPlayer).getWinningCards().get(0);
                    if (tieCard.getValue().getCardValue() < playersBestHands.get(i).getWinningCards().get(0).getValue().getCardValue()) {
                        currentBestPlayer = i;
                    }
                } else if (currentBest == FULL_HOUSE) {
                    Card tieCard = playersBestHands.get(currentBestPlayer).getWinningCards().get(0);
                    if (tieCard.getValue().getCardValue() < playersBestHands.get(i).getWinningCards().get(0).getValue().getCardValue()) {
                        currentBestPlayer = i;
                    }
                } else if (currentBest == FLUSH) {
                    Card tieCard = playersBestHands.get(currentBestPlayer).getWinningCards().get(0);
                    if (tieCard.getValue().getCardValue() < playersBestHands.get(i).getWinningCards().get(0).getValue().getCardValue()) {
                        currentBestPlayer = i;
                    }
                } else if (currentBest == STRAIGHT) {
                    Card tieCard = playersBestHands.get(currentBestPlayer).getWinningCards().get(0);
                    if (tieCard.getValue().getCardValue() < playersBestHands.get(i).getWinningCards().get(0).getValue().getCardValue()) {
                        currentBestPlayer = i;
                    }
                } else if (currentBest == THREE_OF_KIND) {
                    Card tieCard = playersBestHands.get(currentBestPlayer).getWinningCards().get(0);
                    if (tieCard.getValue().getCardValue() < playersBestHands.get(i).getWinningCards().get(0).getValue().getCardValue()) {
                        currentBestPlayer = i;
                    }
                } else if (currentBest == TWO_PAIRS) {
                    Card tieCard = playersBestHands.get(currentBestPlayer).getWinningCards().get(0);
                    if (tieCard.getValue().getCardValue() < playersBestHands.get(i).getWinningCards().get(0).getValue().getCardValue()) {
                        currentBestPlayer = i;
                    } else if (tieCard.getValue().getCardValue() == playersBestHands.get(i).getWinningCards().get(0).getValue().getCardValue()) {
                        tieCard = playersBestHands.get(currentBestPlayer).getWinningCards().get(1);
                        if (tieCard.getValue().getCardValue() < playersBestHands.get(i).getWinningCards().get(1).getValue().getCardValue()) {
                            currentBestPlayer = i;
                        } else if (tieCard.getValue().getCardValue() == playersBestHands.get(i).getWinningCards().get(1).getValue().getCardValue()) {
                            tieCard = playersBestHands.get(currentBestPlayer).getWinningCards().get(2);
                            if (tieCard.getValue().getCardValue() < playersBestHands.get(i).getWinningCards().get(2).getValue().getCardValue()) {
                                currentBestPlayer = i;
                            }
                        }
                    }
                } else if (currentBest == PAIR) {
                    Card tieCard = playersBestHands.get(currentBestPlayer).getWinningCards().get(0);
                    if (tieCard.getValue().getCardValue() < playersBestHands.get(i).getWinningCards().get(0).getValue().getCardValue()) {
                        currentBestPlayer = i;
                    }
                } else if (currentBest == HIGH_CARD) {

                }
            }
        }

        return currentBestPlayer;
    }
}
