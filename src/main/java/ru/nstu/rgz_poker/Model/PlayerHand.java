package ru.nstu.rgz_poker.Model;

import java.util.ArrayList;
import java.util.List;

public class PlayerHand {
    List<Card> cards;

    public PlayerHand() {
        cards = new ArrayList<Card>();
    }

    public void add(Card card) {
        cards.add(card);
    }

    public int size() {
        return cards.size();
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void clear() {
        cards.clear();
        cards = new ArrayList<>();
    }
}