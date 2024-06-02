package ru.nstu.rgz_poker.Model;

public interface ITableRules {
    public static final int INITIAL_SMALL_BLIND = 10;
    public static final int INITIAL_BIG_BLIND = 20;
    public static final int NUMBER_OF_FLOP_CARDS = 3;
    public static final int NUMBER_OF_TURN_CARDS = 1;
    public static final int NUMBER_OF_RIVER_CARDS = 1;
    public static final int TURN_CARD_POS = 3;
    public static final int RIVER_CARD_POS = 4;

    public static final int BIG_BLIND_INCREMENT = 10;
}
