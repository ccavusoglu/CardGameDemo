package com.cardgamedemo.entity;

import java.util.List;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */
public class Hand {
    private List<Card> cardsOrdered;
    private int        groupCount;

    public Hand(List<Card> cardsOrdered, int groupCount) {
        this.groupCount = groupCount;
        this.cardsOrdered = cardsOrdered;
    }

    public List<Card> getCards() {
        return cardsOrdered;
    }

    public int getGroupCount() {
        return groupCount;
    }
}
