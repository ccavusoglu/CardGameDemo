package com.cardgamedemo.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import com.cardgamedemo.CardGame;
import com.cardgamedemo.entity.Card;

import java.util.*;

/**
 * Created by Çağatay Çavuşoğlu on 15.07.2016.
 */

/**
 * Perform sorting operations.
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

    public List<Card> sortInGroups(List<Card> cards) {
        long beginTime = TimeUtils.millis();

        List<Card> sortedCards = getSortByGroup(cards, null, null, null);

        Gdx.app.log("Total Sort in: ", TimeUtils.timeSinceMillis(beginTime) + " ms");

        return sortedCards;
    }

    public List<Card> sortSequential(List<Card> cards) {
        // bucket sort first
        long beginTimeBucket = TimeUtils.millis();

        List<Card> sortedCards = getSortSequential(getSuitBuckets(cards), null, null, null);

        Gdx.app.log("Total Sort in: ", TimeUtils.timeSinceMillis(beginTimeBucket) + " ms");

        return sortedCards;
    }

    // maximum set cover to minimize spare cards total cost
    // cross checks sets (groups & sequences) and finds the maximum coverage by a greedy approach
    // OBSOLETE
    public List<Card> sortSmart(List<Card> cards) {
        long beginTime = TimeUtils.millis();

        // sort first for faster execution
        insertionSort(cards);

        ArrayList<Integer> sequentialSetCosts = new ArrayList<Integer>();
        ArrayList<Integer> sequentialSetCount = new ArrayList<Integer>();
        HashMap<Integer, List<Card>> sequentialSets = new HashMap<Integer, List<Card>>();
        List<Integer> groupSetCount = new ArrayList<Integer>();
        HashMap<Integer, List<Card>> groupSets = new HashMap<Integer, List<Card>>();
        List<Integer> groupSetCosts = new ArrayList<Integer>();

        // first sort by groups
        List<Card> groupSorted = getSortByGroup(cards, groupSets, groupSetCount, groupSetCosts);
        // sort by sequences
        List<Card> orderSorted = getSortSequential(getSuitBuckets(cards), sequentialSets, sequentialSetCount, sequentialSetCosts);

        List<Card> sortedCards = new ArrayList<Card>();
        HashMap<Integer, List<Card>> setBuckets = new HashMap<Integer, List<Card>>(CardGame.CARD_TYPE_COUNT);
        int key = 0, removeIndex = 0;
        // keep track of added sets to prevent duplication
        Map<Integer, Boolean> addedGroupSets = new HashMap<Integer, Boolean>();
        Map<Integer, Boolean> addedSequenceSets = new HashMap<Integer, Boolean>();
        Map<String, Integer> mapSetsToBuckets = new HashMap<String, Integer>();
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
            // compare sets. starts with the highest cost
            List<Card> firstSet;
            List<Card> secondSet;
            List<Integer> firstSetCost;
            List<Integer> secondSetCost;
            List<Integer> firstSetCount = new ArrayList<Integer>();
            List<Integer> secondSetCount = new ArrayList<Integer>();
            Map<Integer, Boolean> firstAddedSets;
            Map<Integer, Boolean> secondAddedSets;
            boolean firstGroup = false;
            int lastGroupSet = groupSetCount.size() - 1;
            int lastSeqSet = sequentialSetCount.size() - 1;
            String first;
            String second;

            if (groupSetCosts.get(lastGroupSet) >= sequentialSetCosts.get(lastSeqSet)) {
                firstSet = groupSets.get(lastGroupSet);
                firstGroup = true;
                secondSet = sequentialSets.get(lastSeqSet);
                firstSetCost = groupSetCosts;
                secondSetCost = sequentialSetCosts;
                firstSetCount = groupSetCount;
                secondSetCount = sequentialSetCount;
                firstAddedSets = addedGroupSets;
                secondAddedSets = addedSequenceSets;
                first = "G";
                second = "S";
            } else {
                firstSet = sequentialSets.get(lastSeqSet);
                secondSet = groupSets.get(lastGroupSet);
                firstSetCost = sequentialSetCosts;
                secondSetCost = groupSetCosts;
                firstSetCount = sequentialSetCount;
                secondSetCount = groupSetCount;
                firstAddedSets = addedSequenceSets;
                secondAddedSets = addedGroupSets;
                first = "S";
                second = "G";
            }
            for (Integer j = firstSetCount.size() - 1; j >= 0; j--) {
                if (firstGroup) {
                    firstSet = groupSets.get(j);
                } else {
                    firstSet = sequentialSets.get(j);
                }
                for (Integer k = secondSetCount.size() - 1; k >= 0; k--) {
                    if (firstGroup) {
                        secondSet = sequentialSets.get(k);
                    } else {
                        secondSet = groupSets.get(k);
                    }
                    boolean conflict = false;
                    int firstSetIndex = 0;
                    for (Card firstSetCard : firstSet) {
                        firstSetIndex++;
                        int secondSetIndex = 0;
                        for (Card secondSetCard : secondSet) {
                            secondSetIndex++;
                            if (firstSetCard.equals(secondSetCard)) {
                                // conflict! should pick one or exchange a card
                                conflict = true;
                                // there may be only 1 conflict. break after this.
                                // check if without this card first set can exist
                                if (firstSet.size() > 3) {
                                    // middle card 1 2 3 '4' 5 6 7
                                    if ((firstSet.size() > 6 && firstSetIndex > 3 && firstSetIndex + 3 <= firstSet.size())) {
                                        // remove this card from this set
                                        firstSet.remove(firstSetCard);

                                        // add divided sets and group
                                        // in this case there can't be more than 2 groups. therefore no need to calculate new sets cost etc.
                                        // loop will just end after this.
                                        if (!firstAddedSets.get(j)) {
                                            mapSetsToBuckets.put(first + j, key);
                                            setBuckets.put(key++, secondSet);
                                            firstAddedSets.put(j, true);
                                        }
                                        if (!secondAddedSets.get(k)) {
                                            mapSetsToBuckets.put(second + k, key);
                                            setBuckets.put(key++, firstSet.subList(0, firstSetIndex - 1));
                                            secondAddedSets.put(k, true);
                                        }
                                        if (!secondAddedSets.get(k + 1)) {
                                            mapSetsToBuckets.put(second + (k + 1), key);
                                            setBuckets.put(key++, firstSet.subList(firstSetIndex - 1, firstSet.size()));
                                            secondAddedSets.put(k + 1, true);
                                        }
                                        break;
                                    }
                                    // first or last card. just remove '1' 2 3 3 || 1 2 3 '4'
                                    else if (firstGroup || firstSetIndex == 1 || firstSetIndex == firstSet.size()) {
                                        // remove this card from this set
                                        firstSet.remove(firstSetCard);
                                        firstSetCost.set(j, firstSetCost.get(j) - firstSetCard.getPoint());
                                        // add both sets to last list
                                        if (!firstAddedSets.get(j)) {
                                            mapSetsToBuckets.put(first + j, key);
                                            setBuckets.put(key++, firstSet);
                                            firstAddedSets.put(j, true);
                                        }
                                        if (!secondAddedSets.get(k)) {
                                            mapSetsToBuckets.put(second + k, key);
                                            setBuckets.put(key++, secondSet);
                                            secondAddedSets.put(k, true);
                                        }
                                        break;
                                    } else {
                                        // this means there are 4-5-6-7-8 cards on the set
                                        // should compare with and without cost to other set cost with or without this card
                                        // then select one of them or both if sequence haven't broken
                                        int cost = 0;
                                        int m = 0;
                                        int u = firstSetIndex - 1;
                                        int n = firstSetIndex - 1;
                                        int z = firstSet.size() - 1;

                                        // may be middle 1 2 '3' 4 5 should calc. upper side
                                        if (firstSetIndex > 3 || (firstSet.size() == 5 && firstSetIndex == 3)) {
                                            m = firstSetIndex;
                                            u = firstSet.size();
                                            n = 0;
                                            z = firstSetIndex - 1;
                                        }

                                        for (; m < u; m++) {
                                            cost += firstSet.get(m).getPoint();
                                        }

                                        if (cost >= secondSetCost.get(k) - secondSetCard.getPoint()) {
                                            // select first set
                                            // remove this card from this set
                                            secondSet.remove(secondSetCard);
                                            secondSetCost.set(k, secondSetCost.get(k) - secondSetCard.getPoint());
                                            // add first set
                                            if (!secondAddedSets.get(k)) {
                                                mapSetsToBuckets.put(second + k, key);
                                                setBuckets.put(key++, firstSet);
                                                secondAddedSets.put(k, true);
                                            }
                                            break;
                                        } else {
                                            // select second set
                                            // remove this card from this set
                                            int temp = firstSetIndex;
                                            int diff = 0;

                                            // right side
                                            if (firstSetIndex > firstSet.size() / 2) {
                                                firstSetIndex--;
                                                diff = firstSet.size() - firstSetIndex;
                                                firstSetCost.set(j, firstSetCost.get(j) - cost - firstSetCard.getPoint());
                                                while (diff-- > 0) firstSet.remove(firstSet.size() - 1);
                                            } else {
                                                diff = temp;
                                                firstSetCost.set(j, firstSetCost.get(j) - cost - firstSetCard.getPoint());
                                                while (diff-- > 0) firstSet.remove(0);
                                            }
                                            firstSetIndex = temp;
                                            // add first set
                                            if (!firstAddedSets.get(j)) {
                                                mapSetsToBuckets.put(first + j, key);
                                                setBuckets.put(key++, firstSet);
                                                firstAddedSets.put(j, true);
                                            }
                                            if (!secondAddedSets.get(k)) {
                                                mapSetsToBuckets.put(second + k, key);
                                                setBuckets.put(key++, secondSet);
                                                secondAddedSets.put(k, true);
                                            }
                                            break;
                                        }
                                    }
                                } else {
                                    if (secondSet.size() == 3) {
                                        // 1 set should be picked
                                        if (firstSetCost.get(j) >= secondSetCost.get(k)) {
                                            if (secondAddedSets.get(k)) {
                                                setBuckets.remove(mapSetsToBuckets.get(second + k));
                                                removeIndex++;
                                            }

                                            if (!firstAddedSets.get(j)) {
                                                mapSetsToBuckets.put(first + j, key);
                                                setBuckets.put(key++, firstSet);
                                                firstAddedSets.put(j, true);
                                            }
                                        } else {
                                            if (firstAddedSets.get(j)) {
                                                setBuckets.remove(mapSetsToBuckets.get(first + j));
                                                removeIndex++;
                                            }

                                            if (!secondAddedSets.get(k)) {
                                                mapSetsToBuckets.put(second + k, key);
                                                setBuckets.put(key++, secondSet);
                                                secondAddedSets.put(k, true);
                                            }
                                        }
                                    } else {
                                        // check if sequential set can exist without this card
                                        // if its first or last we can remove and pick both sets
                                        if (secondSetIndex == 1 || secondSetIndex == secondSet.size()) {
                                            secondSet.remove(secondSetIndex - 1);
                                            // re calc. cost.
                                            secondSetCost.set(k, secondSetCost.get(k) - secondSetCard.getPoint());
                                            if (!firstAddedSets.get(j)) {
                                                mapSetsToBuckets.put(first + j, key);
                                                setBuckets.put(key++, firstSet);
                                                firstAddedSets.put(j, true);
                                            }
                                            if (!secondAddedSets.get(k)) {
                                                mapSetsToBuckets.put(second + k, key);
                                                setBuckets.put(key++, secondSet);
                                                secondAddedSets.put(k, true);
                                            }
                                            break;
                                        }
                                        // if its at 4th or after add cards up to sort index. e.g: 1 2 3 '4' 5 add 1 2 3
                                        else if (secondSetIndex > 3) {
                                            if (!firstAddedSets.get(j)) {
                                                mapSetsToBuckets.put(first + j, key);
                                                setBuckets.put(key++, firstSet);
                                                firstAddedSets.put(j, true);
                                            }
                                            if (!secondAddedSets.get(k)) {
                                                mapSetsToBuckets.put(second + k, key);
                                                setBuckets.put(key++, secondSet.subList(0, secondSetIndex - 1));
                                                secondAddedSets.put(k, true);
                                                // re calc. cost.
                                                secondSetIndex--;
                                                int diff = secondSet.size() - secondSetIndex;
                                                while (diff-- > 0) secondSetCost.set(k, secondSetCost.get(k) - secondSet.get(secondSetIndex++).getPoint());
                                            }
                                        } else {
                                            // 1 set should be picked
                                            if (firstSetCost.get(j) >= secondSetCost.get(k) && !firstAddedSets.get(j)) {
                                                mapSetsToBuckets.put(first + j, key);
                                                setBuckets.put(key++, firstSet);
                                                firstAddedSets.put(j, true);
                                            } else if (!secondAddedSets.get(k)) {
                                                mapSetsToBuckets.put(second + k, key);
                                                setBuckets.put(key++, secondSet);
                                                secondAddedSets.put(k, true);
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        if (conflict) break;
                    }
                    // no conflict. add both
                    if (!conflict) {
                        if (firstSet.size() == 3 && secondSet.size() == 3) {
                            if (!firstAddedSets.get(j)) {
                                mapSetsToBuckets.put(first + j, key);
                                setBuckets.put(key++, firstSet);
                                firstAddedSets.put(j, true);
                            }

                            if (!secondAddedSets.get(k)) {
                                mapSetsToBuckets.put(second + k, key);
                                setBuckets.put(key++, secondSet);
                                secondAddedSets.put(k, true);
                            }
                        }
                    }
                }
            }

            // order sets in between
            Integer i = 0;
            HashMap<Integer, List<Card>> tempBucket = new HashMap<Integer, List<Card>>();
            for (Map.Entry<Integer, List<Card>> entry : setBuckets.entrySet()) {
                tempBucket.put(i++, entry.getValue());
            }

            ArrayList<Integer> bucketCosts = new ArrayList<Integer>();
            for (i = 0; i < key - removeIndex; i++) {
                bucketCosts.add(getTotalPoint(tempBucket.get(i)));
            }

            // sort buckets
            HashMap<Integer, List<Card>> sortedBuckets = sortByArray(tempBucket, bucketCosts);

            for (i = 0; i < bucketCosts.size(); i++) {
                sortedCards.addAll(sortedBuckets.get(i));
            }

            // add spare cards
            for (Card card : groupSorted) {
                if (!sortedCards.contains(card)) sortedCards.add(card);
            }

        }

        Gdx.app.log("Smart Sort in: ", TimeUtils.timeSinceMillis(beginTime) + " ms");

        return sortedCards;
    }

    public List<Card> sortSmart2(List<Card> cards) {
        ArrayList<Integer> sequentialSetCosts = new ArrayList<Integer>();
        ArrayList<Integer> sequentialSetCount = new ArrayList<Integer>();
        HashMap<Integer, List<Card>> sequentialSets = new HashMap<Integer, List<Card>>();
        List<Integer> groupSetCount = new ArrayList<Integer>();
        HashMap<Integer, List<Card>> groupSets = new HashMap<Integer, List<Card>>();
        List<Integer> groupSetCosts = new ArrayList<Integer>();
        List<Card> sortedCards = new ArrayList<Card>();

        // first sort by groups
        List<Card> groupSorted = getSortByGroup(cards, groupSets, groupSetCount, groupSetCosts);
        // sort by sequences
        List<Card> orderSorted = getSortSequential(getSuitBuckets(cards), sequentialSets, sequentialSetCount, sequentialSetCosts);

        // no groups. pick all sequences
        if (groupSetCount.size() == 0) {
            return orderSorted;
        }
        // no sequences pick all groups
        else if (sequentialSetCount.size() == 0) {
            return groupSorted;
        } else if (groupSetCount.size() == 0 && sequentialSetCount.size() == 0) {
            return orderSorted;
        }

        List<CardSet> cardSets = new ArrayList<CardSet>();

        // get all combination of sets (min 3 card)
        // create Set objects
        int id = 0;
        for (int i = 0; i < sequentialSetCount.size(); i++) {
            int size = sequentialSets.get(i).size() + 1;
            while (size-- != 3) {
                for (int j = 0; j < sequentialSets.get(i).size() - size + 1; j++) {
                    cardSets.add(new CardSet(id++, sequentialSets.get(i).subList(j, j + size)));
                }
            }
        }

        for (int i = 0; i < groupSetCount.size(); i++) {
            cardSets.add(new CardSet(id++, groupSets.get(i)));

            if (groupSets.get(i).size() > 3) {
                id = groupCombinations(id, cardSets, groupSets.get(i));
            }
        }

        List<CardSet> setLeft = new ArrayList<CardSet>();
        setLeft.addAll(cardSets);

        List<Integer> finalValues = new ArrayList<Integer>();
        List<List<Integer>> finalSets = new ArrayList<List<Integer>>();

        // generate index list
        // sort!
        for (CardSet cardSet : cardSets) {
            ArrayList<Integer> arr = new ArrayList<Integer>();
            arr.add(cardSet.id);
            finalSets.add(arr);

            finalValues.add(recursiveSort(cardSet, setLeft, arr));
            setLeft.remove(cardSet);
        }

        int maxValueIndex = 0;
        int maxValue = 0;

        for (int i = 0; i < finalValues.size(); i++) {
            if (finalValues.get(i) > maxValue) {
                maxValue = finalValues.get(i);
                maxValueIndex = i;
            }
        }

        List<CardSet> maxValuedSets = new ArrayList<CardSet>();

        for (Integer setId : finalSets.get(maxValueIndex)) {
            maxValuedSets.add(cardSets.get(setId));
        }

        // sort sets based on their values
        bubbleSort(maxValuedSets);

        // add sorted cards
        for (CardSet cardSet : maxValuedSets)
            sortedCards.addAll(cardSet.cards);

        // add spare cards
        for (Card card : groupSorted)
            if (!sortedCards.contains(card)) sortedCards.add(card);

        return sortedCards;
    }

    // ascending bubble sort
    private void bubbleSort(List<CardSet> list) {
        boolean end = false;
        while (!end) {
            end = true;
            for (int i = 0; i < list.size() - 1; i++) {
                if (list.get(i).value > list.get(i + 1).value) {
                    Collections.swap(list, i, i + 1);
                    end = false;
                }
            }
        }
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

    private List<Card> getSortSequential(HashMap<Integer, List<Card>> buckets, HashMap<Integer, List<Card>> sequentialSets, ArrayList<Integer> sequentialSetCount,
                                         ArrayList<Integer> sequentialSetCosts) {
        // begin index to get spare cards for each bucket
        int[] groupIndexStart = {0, 0, 0, 0};
        int[] groupIndexEnd = {1, 1, 1, 1};
        int index = CardGame.CARD_TYPE_COUNT;

        //
        if (sequentialSetCosts == null) sequentialSetCosts = new ArrayList<Integer>();
        if (sequentialSetCount == null) sequentialSetCount = new ArrayList<Integer>();
        if (sequentialSets == null) sequentialSets = new HashMap<Integer, List<Card>>();
        HashMap<Integer, List<Card>> sortedBuckets = new HashMap<Integer, List<Card>>();

        List<Card> sortedCards = new ArrayList<Card>();
        List<Card> spareCards = new ArrayList<Card>();
        ArrayList<Integer> addedBucketIndex = new ArrayList<Integer>();
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
                        sortedBuckets.put(i, buckets.get(i).subList(groupIndexStart[i], groupIndexEnd[i]));
                        addedBucketIndex.add(i);
                        //
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
                        sortedBuckets.put(i, buckets.get(i).subList(groupIndexStart[i], groupIndexEnd[i]));
                        addedBucketIndex.add(i);
                        //
                        groupIndexStart[i] = j;
                        groupIndexEnd[i] = j + 1;
                    } else {
                        spareCards.addAll(buckets.get(i).subList(groupIndexStart[i], groupIndexEnd[i]));
                        groupIndexStart[i] = j;
                        groupIndexEnd[i] = j + 1;
                    }

                    curOrder = 0;
                }
            }
        }

        HashMap<Integer, Integer> setCompare = new HashMap<Integer, Integer>();

        int k = 0;
        for (Integer i : addedBucketIndex) {
            sequentialSetCosts.add(getTotalPoint(sortedBuckets.get(i)));

            setCompare.put(i, sequentialSetCosts.get(k++));
            sequentialSetCount.add(k);
        }

        insertionSort(setCompare, addedBucketIndex);

        sequentialSetCosts.clear();
        k = 0;
        for (Integer i : addedBucketIndex) {
            sortedCards.addAll(sortedBuckets.get(i));
            sequentialSets.put(k++, sortedBuckets.get(i));
            sequentialSetCosts.add(getTotalPoint(sortedBuckets.get(i)));
        }

        // add spare cards to the list
        sortedCards.addAll(spareCards);

        return new ArrayList<Card>(sortedCards);
    }

    private HashMap<Integer, List<Card>> getSuitBuckets(List<Card> cards) {
        // create buckets for 4 types of suites
        HashMap<Integer, List<Card>> buckets = new HashMap<Integer, List<Card>>(CardGame.CARD_TYPE_COUNT);

        for (int i = 0; i < CardGame.CARD_TYPE_COUNT; i++)
            buckets.put(i, new ArrayList<Card>());

        for (Card card : cards) {
            if (card.getSuitType().equals(Enums.SuitType.SPADES)) buckets.get(0).add(card);
            else if (card.getSuitType().equals(Enums.SuitType.DIAMONDS)) buckets.get(1).add(card);
            else if (card.getSuitType().equals(Enums.SuitType.HEARTS)) buckets.get(2).add(card);
            else buckets.get(3).add(card);
        }

        // sort each bucket
        for (int i = 0; i < CardGame.CARD_TYPE_COUNT; i++)
            insertionSort(buckets.get(i));

        return buckets;
    }

    private Integer getTotalPoint(List<Card> cards) {
        Integer total = 0;

        for (Card card : cards)
            total += card.getPoint();

        return total;
    }

    private int groupCombinations(int i, List<CardSet> cardSets, List<Card> cards) {
        List<Card> cards1 = new ArrayList<Card>(3);
        cards1.add(cards.get(0));
        cards1.add(cards.get(1));
        cards1.add(cards.get(2));
        cardSets.add(new CardSet(i++, cards1));
        List<Card> cards2 = new ArrayList<Card>(3);
        cards2.add(cards.get(0));
        cards2.add(cards.get(1));
        cards2.add(cards.get(3));
        cardSets.add(new CardSet(i++, cards2));
        List<Card> cards3 = new ArrayList<Card>(3);
        cards3.add(cards.get(0));
        cards3.add(cards.get(2));
        cards3.add(cards.get(3));
        cardSets.add(new CardSet(i++, cards3));
        List<Card> cards4 = new ArrayList<Card>(3);
        cards4.add(cards.get(1));
        cards4.add(cards.get(2));
        cards4.add(cards.get(3));
        cardSets.add(new CardSet(i++, cards4));

        return i;
    }

    private int recursiveSort(CardSet cardSet, List<CardSet> setsLeft, ArrayList<Integer> arr) {
        int val = cardSet.value;
        int index = setsLeft.size() - 1;
        List<CardSet> noConflict = new ArrayList<CardSet>();

        for (CardSet targetSet : setsLeft) {
            if (targetSet.equals(cardSet)) {
                continue;
            }

            if (!cardSet.conflict(targetSet)) {
                noConflict.add(targetSet);
            } else {
                if (index-- == 0) break;
            }
        }

        // recurse for a set in no conflict list
        if (noConflict.size() > 0) {
            ArrayList<CardSet> tempConf = new ArrayList<CardSet>(noConflict);
            List<Integer> values = new ArrayList<Integer>();
            List<Integer> ids = new ArrayList<Integer>();

            for (CardSet set : noConflict) {
                tempConf.remove(set);
                ids.add(set.id);
                values.add(recursiveSort(set, tempConf, arr));
            }

            int max = values.get(0);
            int maxId = ids.get(0);
            for (int i = 0; i < values.size(); i++) {
                if (values.get(i) > max) {
                    maxId = ids.get(i);
                    max = values.get(i);
                }
            }

            arr.add(maxId);
            val += max;
        }

        return val;
    }
}
