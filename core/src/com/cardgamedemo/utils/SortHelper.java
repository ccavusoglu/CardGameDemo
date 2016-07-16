package com.cardgamedemo.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import com.cardgamedemo.entity.Card;
import com.cardgamedemo.entity.Deck;
import com.cardgamedemo.entity.Hand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Çağatay Çavuşoğlu on 15.07.2016.
 */
public class SortHelper {
    public static void insertionSort(List<Card> list) {
        for (int i = 1; i < list.size(); ++i) {
            Card card = list.get(i);
            int j = i - 1;

            while (j >= 0 && list.get(j).getPoint() > card.getPoint()) {
                Collections.swap(list, j + 1, j);
                j = j - 1;
            }

            list.remove(j + 1);
            list.add(j + 1, card);
        }
    }

    public Hand sortSmart(List<Card> cards) {
        return null;
    }

    public Hand sortInGroups(List<Card> cards) {
        return null;
    }

    public Hand sortInOrder(List<Card> cards) {
        // bucket sort first
        long beginTimeBucket = TimeUtils.nanoTime();

        // create buckets for 4 types of suites
        // TODO: '4' should be obtained from Game instead of hardcoding
        HashMap<Integer, List<Card>> buckets = new HashMap<Integer, List<Card>>(4);
        buckets.put(0, new ArrayList<Card>());
        buckets.put(1, new ArrayList<Card>());
        buckets.put(2, new ArrayList<Card>());
        buckets.put(3, new ArrayList<Card>());

        for (Card card : cards) {
            if (card.getSuitType().equals(Enums.SuitType.SPADES)) buckets.get(0).add(card);
            else if (card.getSuitType().equals(Enums.SuitType.DIAMONDS)) buckets.get(1).add(card);
            else if (card.getSuitType().equals(Enums.SuitType.HEARTS)) buckets.get(2).add(card);
            else buckets.get(3).add(card);
        }

        // sort each bucket
        long beginTime = TimeUtils.nanoTime();
        insertionSort(buckets.get(0));
        insertionSort(buckets.get(1));
        insertionSort(buckets.get(2));
        insertionSort(buckets.get(3));

        for (List<Card> c : buckets.values()) {
            for (Card card : c) {
                Gdx.app.log("Sorted: ", card.getSuitType() + ":" + card.getPoint());
            }
        }

        // seperate groups and order
        // TODO: '4' to Game parameter
        List<Card> sortedCards = getSortedCards(buckets, 4);

        for (Card card : sortedCards) {
            Gdx.app.log("Ordered: ", card.getSuitType() + ":" + card.getPoint());
        }

        Gdx.app.log("Insertion Sort in: ", TimeUtils.timeSinceNanos(beginTime) + " ns");
        Gdx.app.log("Total Sort in: ", TimeUtils.timeSinceNanos(beginTimeBucket) + " ns");

        return new Hand(sortedCards, 4);
    }

    private List<Card> getSortedCards(HashMap<Integer, List<Card>> buckets, int index) {
        // begin index to get spare cards for each bucket
        int[] groupIndexStart = {0, 0, 0, 0};
        int[] groupIndexEnd = {1, 1, 1, 1};

        List<Card> sortedCards = new ArrayList<Card>();
        List<Card> spareCards = new ArrayList<Card>();

        // loop for each bucket
        for (int i = 0; i < index; i++) {
            // loop for each card on buckets
            int curOrder = 0;
            for (int j = 1; j <= buckets.get(i).size(); j++) {
                // 3 and more cards ordered only
                if (buckets.get(i).size() < 3) {
                    spareCards.addAll(buckets.get(i));
                    break;
                }
                // last element
                else if (j == buckets.get(i).size()) {
                    // 3 or more ordered left
                    if (groupIndexEnd[i] == buckets.get(i).size() && curOrder > 1) {
                        sortedCards.addAll(buckets.get(i).subList(groupIndexStart[i], groupIndexEnd[i]));
                    }
                    // 1 card left
                    else if (curOrder == 0) spareCards.add(buckets.get(i).get(j - 1));
                    // 2 card ordered left
                    else spareCards.addAll(buckets.get(i).subList(j - 2, j));
                }
                // order check for j and j+1 Card
                else if (buckets.get(i).get(j - 1).getPoint() + 1 == buckets.get(i).get(j).getPoint()) {
                    curOrder++;
                    groupIndexEnd[i] = j + 1;
                } else {
                    if (curOrder > 1) {
                        sortedCards.addAll(buckets.get(i).subList(groupIndexStart[i], groupIndexEnd[i]));
                    } else {
                        spareCards.addAll(buckets.get(i).subList(groupIndexStart[i], groupIndexEnd[i]));
                        groupIndexStart[i] = j;
                        groupIndexEnd[i] = j + 1;
                    }

                    curOrder = 0;
                }
            }
        }

        // add spare cards to the list
        sortedCards.addAll(spareCards);

        return new ArrayList<Card>(sortedCards);
    }
}
