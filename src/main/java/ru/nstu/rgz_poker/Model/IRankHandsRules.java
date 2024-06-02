/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.nstu.rgz_poker.Model;

/**
 * @author Administrator
 */
public interface IRankHandsRules {
    public static int HIGH_CARD = 1;
    public static int PAIR = 2;
    public static int TWO_PAIRS = 3;
    public static int THREE_OF_KIND = 4;
    public static int STRAIGHT = 5;
    public static int FLUSH = 6;
    public static int FULL_HOUSE = 7;
    public static int FOUR_OF_KIND = 8;
    public static int STRAIGHT_FLUSH = 9;
    public static int ROYAL_FLUSH = 10;
}
