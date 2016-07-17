package com.cardgamedemo.entity;

import java.util.List;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */
public class Hand {
    private List<Card> cards;

    public Hand(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
