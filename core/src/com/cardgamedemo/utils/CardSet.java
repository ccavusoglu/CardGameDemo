package com.cardgamedemo.utils;

/**
 * Created by Çağatay Çavuşoğlu on 22.07.2016.
 */

import com.cardgamedemo.entity.Card;

import java.util.List;

/**
 * Data Object for storing sets
 */
public class CardSet {
    public final int        id;
    public       List<Card> cards;
    public       int        value;

    public CardSet(int i, List<Card> cards) {
        this.id = i;
        this.cards = cards;
        for (Card card : cards)
            value += card.getPoint();
    }

    public boolean conflict(CardSet set) {
        boolean conflict = false;
        for (Card first : cards) {
            for (Card second : set.cards)
                if (first.equals(second)) conflict = true;
        }
        return conflict;
    }

    public String toString() {
        String s = "[" + id + ": ";
        for (Card card : cards)
            s += card.getSuitType() + "" + card.getOrder() + ",";

        s += " :" + value + "]";
        return s;
    }
}
