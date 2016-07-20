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
    private int            order;
    private int            id;

    // only for tests
    public Card() {
    }

    public Card(Enums.SuitType suitType, int order, Enums.CardType cardType, String pointString) {
        this.suitType = suitType;
        this.order = order;
        this.point = order > 10 ? 10 : order;
        this.cardType = cardType;
        this.pointString = pointString;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Card && suitType == ((Card) obj).getSuitType() && order == ((Card) obj).getOrder();
    }

    public Enums.CardType getCardType() {
        return cardType;
    }

    public int getId() {
        return id;
    }

    public int getOrder() {
        return order;
    }

    public int getPoint() {
        return point;
    }

    public String getPointString() {
        return pointString;
    }

    public Enums.SuitType getSuitType() {
        return suitType;
    }

    public void setSuitAndOrder(Enums.SuitType suitType,  int order) {
        this.suitType = suitType;
        this.order = order;
        this.point = order > 10 ? 10 : order;
    }

    public String toString() {
        return "[" + suitType.toString() + ":" + order + "]";
    }
}
