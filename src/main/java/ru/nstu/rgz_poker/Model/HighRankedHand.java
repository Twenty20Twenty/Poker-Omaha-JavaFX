package ru.nstu.rgz_poker.Model;

import ru.nstu.rgz_poker.Model.Suit.SuitType;
import ru.nstu.rgz_poker.Model.Value.CardValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighRankedHand {

    public static boolean isRoyalFlush(List<Card> cardlist) {

        if (!sameSuit(cardlist)) {
            return false;
        }

        for (Card card : cardlist) {
            int value = card.getValue().getCardValue();
            if (!(value == CardValue.ACE
                    || value == CardValue.KING
                    || value == CardValue.QUEEN
                    || value == CardValue.TEN
                    || value == CardValue.JACK))
                return false;
        }
        return true;

    }

    public static Card straightFlush(List<Card> cardsList) {

        if (cardsList == null || cardsList.isEmpty() || cardsList.size() != 5 || !sameSuit(cardsList))
            return null;
        Collections.sort(cardsList);
        int checkValue = cardsList.get(0).getValue().getCardValue();

        for (int i = 1; i < cardsList.size(); i++) {
            if (cardsList.get(i).getValue().getCardValue() - 1 != checkValue)
                return null;
            checkValue = cardsList.get(i).getValue().getCardValue();
        }

        return cardsList.get(cardsList.size() - 1);

    }

    public static Card straight(List<Card> cardsList) {
        if (cardsList == null || cardsList.isEmpty())
            return null;
        Collections.sort(cardsList);
        int checkValue = cardsList.get(0).getValue().getCardValue();

        for (int i = 1; i < cardsList.size(); i++) {
            if (cardsList.get(i).getValue().getCardValue() - 1 != checkValue)
                return null;
            checkValue = cardsList.get(i).getValue().getCardValue();
        }

        return cardsList.get(cardsList.size() - 1);
    }

    public static boolean sameSuit(List<Card> cardlist) {

        SuitType type = cardlist.get(0).getSuit().getSuitType();

        for (int i = 1; i < cardlist.size(); i++) {
            SuitType cardType = cardlist.get(i).getSuit().getSuitType();
            if (type != cardType) {
                return false;
            }

        }
        return true;
    }

    public static Card getHighRankCard(List<Card> cardList) {
        if (cardList == null || cardList.size() == 0) {
            return null;
        }
        Card highCard = cardList.get(0);

        for (int i = 1; i < cardList.size(); i++) {
            Card currentCard = cardList.get(i);
            if (highCard.getValue().getCardValue() < currentCard.getValue().getCardValue()) {
                highCard = currentCard;
            }
        }

        return highCard;
    }

    public static List<Card> twoPairs(List<Card> cardsList) {
        if (cardsList == null || cardsList.isEmpty()) {
            return null;
        }

        int[] cardsCount = getCardsCounts(cardsList);
        List<Card> twoPairs = new ArrayList<>();
        int maxValue = 0;
        boolean found = false;

        for (int i = Value.CardValue.ACE; i >= Value.CardValue.TWO; i--) {
            if (cardsCount[i] == 2) {
                twoPairs.add(new Card(null, Value.getValue(i)));
                found = true;
            }
            if (!found && cardsCount[i] > 0) {
                maxValue = i;
            }

            found = false;
        }

        if (twoPairs.size() != 2)
            return null;
        twoPairs.add(new Card(null, Value.getValue(maxValue)));
        return twoPairs;

    }

    public static Card flush(List<Card> cardList) {
        if (!sameSuit(cardList) || cardList.isEmpty()) {
            return null;
        }
        Card highCard = cardList.get(0);

        for (int i = 1; i < cardList.size(); i++) {
            Card currentCard = cardList.get(i);
            if (highCard.getValue().getCardValue() < currentCard.getValue().getCardValue()) {
                highCard = currentCard;
            }
        }
        return highCard;
    }

    public static List<Card> fullHouse(List<Card> cardList) {
        if (cardList == null || cardList.size() != Dealer.EVALUATE_HAND_SIZE) {
            return null;
        }
        List<Card> fullHouse = new ArrayList<Card>(2);
        Card temp = null;
        boolean foundThree = false;
        int[] cardsCount = getCardsCounts(cardList);
        int twosCount = 0;
        for (int i = Value.CardValue.TWO; i < cardsCount.length; i++) {
            if (cardsCount[i] == 3) {
                fullHouse.add(new Card(null, Value.getValue(i)));
                foundThree = true;
            } else if (cardsCount[i] == 2 && twosCount < 1) {
                twosCount++;
                temp = new Card(null, Value.getValue(i));
            }
        }
        if (foundThree && twosCount == 1) {
            fullHouse.add(temp);
        }

        return fullHouse;
    }

    public static List<Card> threeOfKind(List<Card> cardList) {
        if (cardList == null || cardList.size() < 3) {
            return null;
        }
        List<Card> threeOfKind = new ArrayList<Card>(2);
        int[] cardsCount = getCardsCounts(cardList);

        int maxValue = 0;
        boolean found = false;

        for (int i = Value.CardValue.TWO; i < cardsCount.length; i++) {

            if (cardsCount[i] == 3) {
                threeOfKind.add(new Card(null, Value.getValue(i)));
                found = true;
            }
            if (!found && cardsCount[i] > 0) {
                maxValue = i;
            }

            found = false;

        }

        if (threeOfKind.size() != 1) {
            return null;
        } else {
            threeOfKind.add(new Card(null, Value.getValue(maxValue)));
        }

        return threeOfKind;

    }

    private static int[] getCardsCounts(List<Card> cardList) {
        int[] returnCards = new int[Value.CardValue.ACE + 1];
        for (Card card : cardList) {
            returnCards[card.getValue().getCardValue()]++;
        }
        return returnCards;
    }

    public static List<Card> fourOfaKind(List<Card> cardList) {
        if (cardList == null || cardList.size() < 4) {
            return null;
        }
        List<Card> fourOfaKind = new ArrayList<Card>(2);
        int[] cardsCount = getCardsCounts(cardList);

        int maxValue = 0;
        boolean found = false;

        for (int i = Value.CardValue.TWO; i < cardsCount.length; i++) {

            if (cardsCount[i] == 4) {
                fourOfaKind.add(new Card(null, Value.getValue(i)));
                found = true;
            }
            if (!found && cardsCount[i] > 0) {
                maxValue = i;
            }

        }

        if (!found) {
            return null;
        } else {
            fourOfaKind.add(new Card(null, Value.getValue(maxValue)));
        }

        return fourOfaKind;

    }

    public static boolean hasPair(List<Card> cards) {
        int[] cardsCount = getCardsCounts(cards);

        for (int i = 0; i < cardsCount.length; i++) {
            if (cardsCount[i] == 2) {
                return true;
            }
        }

        return false;
    }

    public static Card getPair(List<Card> cards) {
        Card returnCard = null;
        int[] cardsCount = getCardsCounts(cards);

        for (int i = CardValue.ACE; i >= CardValue.TWO; i--) {
            if (cardsCount[i] == 2) {
                returnCard = new Card(null, Value.getValue(i));
                break;
            }
        }

        return returnCard;
    }

}
