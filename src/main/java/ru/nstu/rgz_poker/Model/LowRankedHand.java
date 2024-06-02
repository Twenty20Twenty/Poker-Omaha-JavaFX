package ru.nstu.rgz_poker.Model;

import ru.nstu.rgz_poker.Model.Value.CardValue;

import java.util.Arrays;
import java.util.List;

public class LowRankedHand {

    final static int LOW_HAND_MAX_VALUE = 30;

    public static int getLowHandScore(List<Card> cards) {
        if (!isLowRankHand(cards)) {
            return LOW_HAND_MAX_VALUE + 5;
        }

        int score = 0;

        for (Card card : cards) {
            int cardValue = card.getValue().getCardValue();
            if (cardValue == CardValue.ACE)
                cardValue = 1;
            score += cardValue;
        }

        return score;
    }

    private static boolean isLowRankHand(List<Card> cards) {

        for (Card card : cards) {
            int cardValue = card.getValue().getCardValue();
            if (cardValue > CardValue.EIGHT && cardValue != CardValue.ACE)
                return false;
        }

        int[] cardsCount = getCardsCounts(cards);
        Arrays.sort(cardsCount);

        for (int i = 0; i < cardsCount.length; i++) {
            if (cardsCount[i] > 1)
                return false;
        }

        return true;
    }

    private static int[] getCardsCounts(List<Card> cardList) {

        int[] returnCards = new int[Value.CardValue.EIGHT + 1];
        for (Card card : cardList) {
            int cardValue = card.getValue().getCardValue();

            if (cardValue == CardValue.ACE)
                cardValue = 1;

            returnCards[cardValue]++;
        }
        return returnCards;
    }

}
