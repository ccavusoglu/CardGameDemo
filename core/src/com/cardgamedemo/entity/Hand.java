package com.cardgamedemo.entity;

import java.util.List;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */
public class Hand {
    private List<Card> cardsOrdered;

    public Hand(List<Card> cardsOrdered) {
        this.cardsOrdered = cardsOrdered;
    }

    public List<Card> getCards() {
        return cardsOrdered;
    }

}
