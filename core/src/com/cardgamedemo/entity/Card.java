package com.cardgamedemo.entity;

import com.cardgamedemo.utils.Enums;

/**
 * Created by Çağatay Çavuşoğlu on 15.07.2016.
 */
public class Card {
    private Enums.SuitType suitType;
    private int            point;
    private String         pointString;
    private Enums.CardType cardType;
    private int sortIndex = 0;

    public Card(Enums.SuitType suitType, int point, Enums.CardType cardType, String pointString) {
        this.suitType = suitType;
        this.point = point;
        this.cardType = cardType;
        this.pointString = pointString;
    }

    public Enums.CardType getCardType() {
        return cardType;
    }

    public int getPoint() {
        return point;
    }

    public String getPointString() {
        return pointString;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public Enums.SuitType getSuitType() {
        return suitType;
    }

    public void setSortIndex() {

    }
}
