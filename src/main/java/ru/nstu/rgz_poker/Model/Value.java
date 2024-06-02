package ru.nstu.rgz_poker.Model;

/**
 * Represents the Value of a card.
 * <p>
 * Bugs: none
 *
 * @author Dilan
 * @version 1.0
 * @since 2014-06-15
 */

public class Value {


    private int cardValue;

    public Value(int value) {
        this.setCardValue(value);
    }

    public static Value getValue(int value) {
        return new Value(value);
    }

    public int getCardValue() {
        return cardValue;
    }

    public void setCardValue(int cardValue) {
        this.cardValue = cardValue;
    }

    public class CardValue {
        public static final short TWO = 2;
        public static final short THREE = 3;
        public static final short FOUR = 4;
        public static final short FIVE = 5;
        public static final short SIX = 6;
        public static final short SEVEN = 7;
        public static final short EIGHT = 8;
        public static final short NINE = 9;
        public static final short TEN = 10;
        public static final short JACK = 11;
        public static final short QUEEN = 12;
        public static final short KING = 13;
        public static final short ACE = 14;

    }
}
