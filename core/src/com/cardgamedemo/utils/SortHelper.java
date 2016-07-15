package com.cardgamedemo.utils;

import com.cardgamedemo.entity.Card;
import com.cardgamedemo.entity.Deck;
import com.cardgamedemo.entity.Hand;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Çağatay Çavuşoğlu on 15.07.2016.
 */
public class SortHelper {
    public Hand sortInOrder(Deck deck) {
        // dummy
        HashMap<Integer, List<Card>> dummy = new HashMap<Integer, List<Card>>();
        dummy.put(0, deck.getCards().subList(0, 2));
        dummy.put(1, deck.getCards().subList(3, 5));
        dummy.put(2, deck.getCards().subList(6, 8));
        dummy.put(3, deck.getCards().subList(9, 10));

        return new Hand(dummy, 4);
    }

    public Hand sortInGroups(Deck deck) {
        return null;
    }
}
