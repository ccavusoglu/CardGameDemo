package com.cardgamedemo.utils;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */
public class Enums {
    public enum SuitType {
        CLUBS, SPADES, HEARTS, DIAMONDS;

        public String fullName() {
            return super.toString();
        }

        @Override
        public String toString() {
            return super.toString().substring(0, 1);
        }
    }

    public enum CardType {
        BASE, JACK, QUEEN, KING
    }

    public enum ButtonType {
        DECK, DRAW_ORDER, DRAW_GROUP, DRAW_SMART
    }
}
