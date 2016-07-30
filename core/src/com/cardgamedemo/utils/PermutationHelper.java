package com.cardgamedemo.utils;

import com.cardgamedemo.entity.Card;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Çağatay Çavuşoğlu on 30.07.2016.
 */
public class PermutationHelper {
    private final int size;
    private final List<Card> cards;
    private final Card[] nextPermutation;
    private final int[] permutation;
    private boolean next = true;

    public PermutationHelper(List<Card> cards) {
        size = cards.size();
        this.cards = cards;
        nextPermutation = new Card[size];
        permutation = new int[size + 1];
        for (int i = 0; i < size + 1; i++) {
            permutation[i] = i;
        }
    }

    public boolean hasNext() {
        return next;
    }

    public List<Card> next() {
        formNextPermutation();
        int i = size - 1;
        while (permutation[i] > permutation[i + 1]) i--;

        if (i == 0) {
            next = false;
            for (int j = 0; j < size + 1; j++) {
                permutation[j] = j;
            }
            return Arrays.asList(nextPermutation);
        }

        int j = size;

        while (permutation[i] > permutation[j]) j--;
        swap(i, j);
        int r = size;
        int s = i + 1;
        while (r > s) {
            swap(r, s);
            r--;
            s++;
        }

        return Arrays.asList(nextPermutation);
    }

    private void formNextPermutation() {
        for (int i = 0; i < size; i++) {
            Array.set(nextPermutation, i, cards.get(permutation[i + 1] - 1));
        }
    }

    private void swap(final int i, final int j) {
        final int x = permutation[i];
        permutation[i] = permutation[j];
        permutation[j] = x;
    }
}
