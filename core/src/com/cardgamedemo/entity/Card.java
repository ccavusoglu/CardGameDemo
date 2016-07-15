package com.cardgamedemo.entity;

import com.cardgamedemo.utils.Enums;

/**
 * Created by Çağatay Çavuşoğlu on 15.07.2016.
 */
public class Card {
    private Enums.SuitType suitType;
    private int            point;
    private Enums.CardType cardType;
    private int sortIndex = 0;

    public Card(Enums.SuitType suitType, int point, Enums.CardType cardType) {
        this.suitType = suitType;
        this.point = point;
        this.cardType = cardType;
    }

    public Enums.CardType getCardType() {
        return cardType;
    }

    public int getPoint() {
        return point;
    }

    public Enums.SuitType getSuitType() {
        return suitType;
    }

    public void setSortIndex() {

    }

    public int getSortIndex() {
        return sortIndex;
    }
}
