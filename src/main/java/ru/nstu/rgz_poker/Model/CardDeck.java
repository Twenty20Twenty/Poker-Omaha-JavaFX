package ru.nstu.rgz_poker.Model;

import java.util.Collections;
import java.util.Stack;

/**
 * Represents the deck of cards with all available Cards.
 * <p>
 * Bugs: none
 *
 * @author Dilan
 * @version 1.0
 * @since 2014-06-15
 */
public class CardDeck {
    private Stack<Card> cardsList;

    public CardDeck() {
        cardsList = new Stack<Card>();
        initialize(cardsList);
    }

    private void initialize(Stack<Card> cardsList) {

        for (int i = Value.CardValue.TWO; i <= Value.CardValue.ACE; i++) {

            Card clubsCard = new Card(Suit.getSuit("CLUBS"), Value.getValue(i));
            Card diamondCard = new Card(Suit.getSuit("DIAMONDS"), Value.getValue(i));
            Card heartsCard = new Card(Suit.getSuit("HEARTS"), Value.getValue(i));
            Card spadesCard = new Card(Suit.getSuit("SPADES"), Value.getValue(i));

            cardsList.add(clubsCard);
            cardsList.add(diamondCard);
            cardsList.add(heartsCard);
            cardsList.add(spadesCard);
        }

    }

    public void shuffle() {
        Collections.shuffle(cardsList);
    }

    public int getSize() {
        return cardsList.size();
    }

    public Card getTopCard() {
        return cardsList.pop();
    }
}
