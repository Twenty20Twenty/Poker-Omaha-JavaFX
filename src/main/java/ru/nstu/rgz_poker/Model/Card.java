package ru.nstu.rgz_poker.Model;

/**
 * Represents a Card with its Suit and Value
 * <p>
 * Bugs: none
 *
 * @author Dilan
 * @version 1.0
 * @since 2014-06-15
 */
public class Card implements Comparable<Card> {

    /**
     * Suit of the Card. Heart,Diamond,Spades,Clubs
     */
    private Suit suit;

    /**
     * Value of the Card. 2,3,4..... 10, Jack,Queen,King,Ace
     */
    private Value value;

    public Card(Suit suit, Value value) {
        this.suit = suit;
        this.value = value;
    }

    public Suit getSuit() {
        return suit;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return suit.getSuitType().name() + ":" + value.getCardValue();
    }

    @Override
    public int compareTo(Card card) {
        int compareValue = this.getValue().getCardValue() - card.getValue().getCardValue();
        return compareValue;
    }

}
