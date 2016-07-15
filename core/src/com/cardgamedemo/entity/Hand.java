package com.cardgamedemo.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */
public class Hand {
    private List<Card>                   cardsOrdered;
    private int                          groupCount;
    private HashMap<Integer, List<Card>> cardsInGroups;

    public Hand(HashMap<Integer, List<Card>> cardsInGroups, int groupCount) {
        this.cardsInGroups = cardsInGroups;
        this.groupCount = groupCount;
        cardsOrdered = new ArrayList<Card>();
    }

    public HashMap<Integer, List<Card>> getCardsInGroups() {
        return cardsInGroups;
    }

    public List<Card> getCardsOrdered() {
        // TODO: dummy
        cardsOrdered.addAll(cardsInGroups.get(0));
        cardsOrdered.addAll(cardsInGroups.get(1));
        cardsOrdered.addAll(cardsInGroups.get(2));
        cardsOrdered.addAll(cardsInGroups.get(3));
        return cardsOrdered;
    }

    public int getGroupCount() {
        return groupCount;
    }
}
