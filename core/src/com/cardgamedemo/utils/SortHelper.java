package com.cardgamedemo.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import com.cardgamedemo.CardGameDemo;
import com.cardgamedemo.entity.Card;
import com.cardgamedemo.entity.Hand;

import java.util.*;

/**
 * Created by Çağatay Çavuşoğlu on 15.07.2016.
 */
public class SortHelper {
    private static final int MIN_GROUP_SIZE = 3;

    public static void insertionSort(List<Card> list) {
        for (int i = 1; i < list.size(); ++i) {
            Card card = list.get(i);
            int j = i - 1;

            while (j >= 0 && list.get(j).getOrder() > card.getOrder()) {
                Collections.swap(list, j + 1, j);
                j = j - 1;
            }

            list.remove(j + 1);
            list.add(j + 1, card);
        }
    }

    public static void insertionSort(HashMap<Integer, Integer> bucketPoints, ArrayList<Integer> bucketsToCompare) {
        for (Integer i = 1; i < bucketsToCompare.size(); ++i) {
            Integer k = bucketsToCompare.get(i);
            Integer j = bucketsToCompare.get(i - 1);
            Integer m = i - 1;

            while (m >= 0 && bucketPoints.get(j) > bucketPoints.get(k)) {
                Collections.swap(bucketsToCompare, m, m + 1);
                m = m - 1;
            }

            bucketsToCompare.remove(m + 1);
            bucketsToCompare.add(m + 1, k);
        }
    }

    public static HashMap<Integer, List<Card>> sortByArray(HashMap<Integer, List<Card>> setBuckets, List<Integer> bucketCosts) {
        HashMap<Integer, List<Card>> temp = new HashMap<Integer, List<Card>>();
        List<Integer> templ = new ArrayList<Integer>();

        Integer index = 0;
        for (Integer integer : bucketCosts)
            templ.add(index++);

        for (Integer i = 1; i < bucketCosts.size(); ++i) {
            Integer k = bucketCosts.get(i);
            Integer j = bucketCosts.get(i - 1);
            Integer m = i - 1;

            while (m >= 0 && j > k) {
                Collections.swap(bucketCosts, m, m + 1);
                Collections.swap(templ, m, m + 1);

                m = m - 1;
            }

            templ.remove(m + 1);
            templ.add(m + 1, i);

            bucketCosts.remove(m + 1);
            bucketCosts.add(m + 1, k);
        }

        index = 0;
        // sort buckets
        for (Integer integer : templ) {
            temp.put(index++, setBuckets.get(integer));
        }

        return temp;
    }

    public Hand sortInGroups(List<Card> cards) {
        long beginTime = TimeUtils.millis();

        for (Card card : cards) {
            Gdx.app.log("Cards G: ", card.getSuitType() + ":" + card.getPoint() + " " + card.getPointString());
        }

        List<Card> sortedCards = getSortByGroup(cards, null, null, null);

        for (Card card : sortedCards) {
            Gdx.app.log("Ordered G: ", card.getSuitType() + ":" + card.getPoint());
        }

        Gdx.app.log("Total Sort in: ", TimeUtils.timeSinceMillis(beginTime) + " ms");

        return new Hand(sortedCards);
    }

    public Hand sortSequential(List<Card> cards) {
        // bucket sort first
        long beginTimeBucket = TimeUtils.millis();

        List<Card> sortedCards = getSortSequential(getBuckets(cards), null, null, null);

        for (Card card : sortedCards) {
            Gdx.app.log("Ordered: ", card.getSuitType() + ":" + card.getPoint());
        }

        Gdx.app.log("Total Sort in: ", TimeUtils.timeSinceMillis(beginTimeBucket) + " ms");

        return new Hand(sortedCards);
    }

    public Hand sortSmart(List<Card> cards) {
        long beginTime = TimeUtils.millis();

        // sort first for faster execution
        insertionSort(cards);

        for (Card card : cards) {
            Gdx.app.log("Cards G: ", card.getSuitType() + ":" + card.getPoint() + " " + card.getPointString());
        }

        List<Integer> sequentialSetCosts = new ArrayList<Integer>();
        List<Integer> sequentialSetCount = new ArrayList<Integer>();
        HashMap<Integer, List<Card>> sequentialSets = new HashMap<Integer, List<Card>>();
        List<Integer> groupSetCount = new ArrayList<Integer>();
        HashMap<Integer, List<Card>> groupSets = new HashMap<Integer, List<Card>>();
        List<Integer> groupSetCosts = new ArrayList<Integer>();

        // first sort by groups
        List<Card> groupSorted = getSortByGroup(cards, groupSets, groupSetCount, groupSetCosts);
        // sort by sequences
        List<Card> orderSorted = getSortSequential(getBuckets(cards), sequentialSets, sequentialSetCount, sequentialSetCosts);

        List<Card> sortedCards = new ArrayList<Card>();
        HashMap<Integer, List<Card>> setBuckets = new HashMap<Integer, List<Card>>(CardGameDemo.CARD_TYPE_COUNT);
        int key = 0;
        // keep track of added sets to prevent duplication
        Map<Integer, Boolean> addedGroupSets = new HashMap<Integer, Boolean>();
        Map<Integer, Boolean> addedSequenceSets = new HashMap<Integer, Boolean>();
        addedGroupSets.put(0, false);
        addedGroupSets.put(1, false);
        addedGroupSets.put(2, false);
        addedSequenceSets.put(0, false);
        addedSequenceSets.put(1, false);
        addedSequenceSets.put(2, false);

        // no groups. pick all sequences
        if (groupSetCount.size() == 0) {
            sortedCards = orderSorted;
        }
        // no sequences pick all groups
        else if (sequentialSetCount.size() == 0) {
            sortedCards = groupSorted;
        } else {
            // compare sets
            for (Integer j = groupSetCount.size() - 1; j >= 0; j--) {
                for (Integer k = sequentialSetCount.size() - 1; k >= 0; k--) {
                    // cross check sets for conflicts
                    if (groupSets.get(j).size() == 3 && sequentialSets.get(k).size() == 3) {
                        // can't give a card
                        if (groupSetCosts.get(j) >= sequentialSetCosts.get(k)) {
                            if (!addedGroupSets.get(j)) {
                                setBuckets.put(key++, groupSets.get(j));
                                addedGroupSets.put(j, true);
                            }
                        } else if (!addedSequenceSets.get(k)) {
                            setBuckets.put(key++, sequentialSets.get(k));
                            addedSequenceSets.put(k, true);
                        }
                    } else {
                        boolean conflict = false;
                        // compare sets
                        for (Card card : groupSets.get(j)) {
                            int sortIndex = 0;
                            for (Card card1 : sequentialSets.get(k)) {
                                sortIndex++;
                                if (card.equals(card1)) {
                                    // conflict! should pick one or exchange a card
                                    conflict = true;
                                    // there may be only 1 conflict. break after this.
                                    // check if without this card first set can exist
                                    if (groupSets.get(j).size() == 4) {
                                        // remove this card from this set
                                        groupSets.get(j).remove(card);
                                        groupSetCosts.set(j, groupSetCosts.get(j) - card.getPoint());
                                        // add both set to last list
                                        // they have the highest total cost
                                        if (!addedGroupSets.get(j)) {
                                            setBuckets.put(key++, groupSets.get(j));
                                            addedGroupSets.put(j, true);
                                        }
                                        if (!addedSequenceSets.get(k)) {
                                            setBuckets.put(key++, sequentialSets.get(k));
                                            addedSequenceSets.put(k, true);
                                        }
                                        break;
                                    } else {
                                        if (sequentialSets.get(k).size() == 3) {
                                            // 1 set should be picked
                                            if (groupSetCosts.get(j) >= sequentialSetCosts.get(k) && !addedGroupSets.get(j)) {
                                                setBuckets.put(key++, groupSets.get(j));
                                                addedGroupSets.put(j, true);
                                            } else if (!addedSequenceSets.get(k)) {
                                                setBuckets.put(key++, sequentialSets.get(k));
                                                addedSequenceSets.put(k, true);
                                            }
                                        } else {
                                            // check if sequential set can exist without this card
                                            // if its first or last we can remove and pick both sets
                                            if (sortIndex == 1 || sortIndex == sequentialSets.get(k).size()) {
                                                sequentialSets.get(k).remove(sortIndex - 1);
                                                // re calc. cost.
                                                sequentialSetCosts.set(k, sequentialSetCosts.get(k) - card1.getPoint());
                                                if (!addedGroupSets.get(j)) {
                                                    setBuckets.put(key++, groupSets.get(j));
                                                    addedGroupSets.put(j, true);
                                                }
                                                if (!addedSequenceSets.get(k)) {
                                                    setBuckets.put(key++, sequentialSets.get(k));
                                                    addedSequenceSets.put(k, true);
                                                }
                                                break;
                                            }
                                            // if its at 4th or after add cards up to sort index. e.g: 1 2 3 '4' 5 add 1 2 3
                                            else if (sortIndex > 3) {
                                                if (!addedGroupSets.get(j)) {
                                                    setBuckets.put(key++, groupSets.get(j));
                                                    addedGroupSets.put(j, true);
                                                }
                                                if (!addedSequenceSets.get(k)) {
                                                    setBuckets.put(key++, sequentialSets.get(k).subList(0, sortIndex));
                                                    addedSequenceSets.put(k, true);
                                                    // re calc. cost.
                                                    sortIndex--;
                                                    int diff = sequentialSets.get(k).size() - sortIndex;
                                                    while (diff-- > 0)
                                                        sequentialSetCosts.set(k, sequentialSetCosts.get(k) - sequentialSets.get(k).get(sortIndex++).getPoint());
                                                }
                                            } else {
                                                // 1 set should be picked
                                                if (groupSetCosts.get(j) >= sequentialSetCosts.get(k) && !addedGroupSets.get(j)) {
                                                    setBuckets.put(key++, groupSets.get(j));
                                                    addedGroupSets.put(j, true);
                                                } else if (!addedSequenceSets.get(k)) {
                                                    setBuckets.put(key++, sequentialSets.get(k));
                                                    addedSequenceSets.put(k, true);
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            if (conflict) break;
                        }
                    }
                }
            }

            // order sets in between
            ArrayList<Integer> bucketCosts = new ArrayList<Integer>();
            for (int i = 0; i < key; i++) {
                bucketCosts.add(getTotalPoint(setBuckets.get(i)));
            }

            // sort buckets
            HashMap<Integer, List<Card>> sortedBuckets = sortByArray(setBuckets, bucketCosts);

            for (int i = 0; i < bucketCosts.size(); i++) {
                sortedCards.addAll(sortedBuckets.get(i));
            }

            // add spare cards
            for (Card card : groupSorted) {
                if (!sortedCards.contains(card)) sortedCards.add(card);
            }

        }

        for (Card card : sortedCards) {
            Gdx.app.log("Ordered: ", card.getSuitType() + ":" + card.getPoint());
        }

        Gdx.app.log("Smart Sort in: ", TimeUtils.timeSinceMillis(beginTime) + " ms");

        return new Hand(sortedCards);
    }

    private HashMap<Integer, List<Card>> getBuckets(List<Card> cards) {
        // create buckets for 4 types of suites
        HashMap<Integer, List<Card>> buckets = new HashMap<Integer, List<Card>>(CardGameDemo.CARD_TYPE_COUNT);
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
        insertionSort(buckets.get(0));
        insertionSort(buckets.get(1));
        insertionSort(buckets.get(2));
        insertionSort(buckets.get(3));

        return buckets;
    }

    private List<Card> getSortByGroup(List<Card> cards, HashMap<Integer, List<Card>> groupSets, List<Integer> groupSetCount, List<Integer> groupSetCosts) {
        // create buckets and add cards
        HashMap<Integer, List<Card>> buckets = new HashMap<Integer, List<Card>>();
        HashSet<Integer> spareBuckets = new HashSet<Integer>(cards.size());
        HashMap<Integer, Integer> bucketPoints = new HashMap<Integer, Integer>(cards.size());
        ArrayList<Integer> bucketsToCompare = new ArrayList<Integer>(cards.size() / MIN_GROUP_SIZE);

        for (Card card : cards) {
            int order = card.getOrder();

            if (buckets.get(order) == null) {
                buckets.put(order, new ArrayList<Card>());
                bucketPoints.put(order, 0);
            }

            bucketPoints.put(order, bucketPoints.get(order) + card.getPoint());
            buckets.get(order).add(card);

            // there's a group in this bucket.
            // if size > 4 prevent adding twice
            if (buckets.get(order).size() > 2 && !bucketsToCompare.contains(order)) {
                bucketsToCompare.add(order);
            } else spareBuckets.add(order);
        }

        // sort buckets with groups
        insertionSort(bucketPoints, bucketsToCompare);

        // compare buckets and create sorted list
        List<Card> sortedCards = new ArrayList<Card>();
        List<Card> spareCards = new ArrayList<Card>();

        // there's no 3 or 4 groups. just add all to final list
        if (bucketsToCompare.size() == 0) {
            spareCards.addAll(cards);
        } else {
            int i = 0;
            for (Integer index : bucketsToCompare) {
                sortedCards.addAll(buckets.get(index));
                spareBuckets.remove(index);
                //
                if (groupSetCount != null) groupSetCount.add(i);
                if (groupSets != null) groupSets.put(i++, buckets.get(index));
                if (groupSetCosts != null) groupSetCosts.add(getTotalPoint(buckets.get(index)));
            }

            for (Integer index : spareBuckets) {
                spareCards.addAll(buckets.get(index));
            }
        }

        // sort spare cards
        insertionSort(spareCards);

        // add spare cards at the end
        sortedCards.addAll(spareCards);

        return sortedCards;
    }

    private List<Card> getSortSequential(HashMap<Integer, List<Card>> buckets, HashMap<Integer, List<Card>> sequentialSets, List<Integer> sequentialSetCount,
                                         List<Integer> sequentialSetCosts) {
        // begin index to get spare cards for each bucket
        int[] groupIndexStart = {0, 0, 0, 0};
        int[] groupIndexEnd = {1, 1, 1, 1};
        int index = CardGameDemo.CARD_TYPE_COUNT;

        List<Card> sortedCards = new ArrayList<Card>();
        List<Card> spareCards = new ArrayList<Card>();
        int setIndex = 0;

        // loop for each bucket
        for (int i = 0; i < index; i++) {
            // loop for each card on buckets
            int curOrder = 0;
            for (int j = 1; j <= buckets.get(i).size(); j++) {
                // 3 and more cards will be ordered only
                if (buckets.get(i).size() < 3) {
                    spareCards.addAll(buckets.get(i));
                    break;
                }
                // last element
                else if (j == buckets.get(i).size()) {
                    // 3 or more ordered left
                    if (groupIndexEnd[i] == buckets.get(i).size() && curOrder > 1) {
                        sortedCards.addAll(buckets.get(i).subList(groupIndexStart[i], groupIndexEnd[i]));
                        //
                        if (sequentialSetCount != null) sequentialSetCount.add(setIndex);
                        if (sequentialSets != null) sequentialSets.put(setIndex++, buckets.get(i).subList(groupIndexStart[i], groupIndexEnd[i]));
                        if (sequentialSetCosts != null) sequentialSetCosts.add(getTotalPoint(buckets.get(i).subList(groupIndexStart[i], groupIndexEnd[i])));
                    }
                    // 1 card left
                    else if (curOrder == 0) spareCards.add(buckets.get(i).get(j - 1));
                        // 2 card ordered left
                    else spareCards.addAll(buckets.get(i).subList(j - 2, j));
                }
                // order check for j and j+1 Card
                else if (buckets.get(i).get(j - 1).getOrder() + 1 == buckets.get(i).get(j).getOrder()) {
                    curOrder++;
                    groupIndexEnd[i] = j + 1;
                } else {
                    if (curOrder > 1) {
                        sortedCards.addAll(buckets.get(i).subList(groupIndexStart[i], groupIndexEnd[i]));
                        groupIndexStart[i] = j;
                        groupIndexEnd[i] = j + 1;
                        //
                        if (sequentialSetCount != null) sequentialSetCount.add(setIndex);
                        if (sequentialSets != null) sequentialSets.put(setIndex++, buckets.get(i).subList(groupIndexStart[i], groupIndexEnd[i]));
                        if (sequentialSetCosts != null) sequentialSetCosts.add(getTotalPoint(buckets.get(i).subList(groupIndexStart[i], groupIndexEnd[i])));
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

    private Integer getTotalPoint(List<Card> cards) {
        Integer total = 0;

        for (Card card : cards)
            total += card.getPoint();

        return total;
    }
}
