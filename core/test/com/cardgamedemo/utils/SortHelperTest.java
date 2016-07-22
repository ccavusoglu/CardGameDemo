package com.cardgamedemo.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.cardgamedemo.entity.Card;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Created by Çağatay Çavuşoğlu on 20.07.2016.
 */
public class SortHelperTest {
    static HashMap<Integer, List<Card>> sortCases;
    static HashMap<Integer, List<Card>> sortCasesResults;
    static HashMap<Integer, List<Card>> sortCasesExpectedResults;
    SortHelper sut;

    @Before
    public void setUp() throws Exception {
        sut = new SortHelper();
    }

    @BeforeClass
    public static void mockGdx() {
        Gdx.app = mock(Application.class);
    }

    @BeforeClass
    public static void setUpCases() throws Exception {
        sortCases = new HashMap<Integer, List<Card>>();
        sortCasesExpectedResults = new HashMap<Integer, List<Card>>();
        sortCasesResults = new HashMap<Integer, List<Card>>();

        // insertion sort with lists
        Integer i = 0;
        sortCases.put(i, mapStrToCards(new String[]{"H1", "S2", "D5", "H4", "S1", "D3", "C4", "S4", "D1", "S3", "D4"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"H1", "S1", "D1", "S2", "D3", "S3", "H4", "C4", "S4", "D4", "D5"}));

        sortCases.put(i, mapStrToCards(new String[]{"C9", "S8", "C10", "C13", "D1", "H10", "C10", "C8", "D12", "S6", "S2"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"D1", "S2", "S6", "S8", "C8", "C9", "C10", "H10", "C10", "D12", "C13"}));

        sortCases.put(i, mapStrToCards(new String[]{"D7", "C13", "H5", "C12", "H10", "S1", "S4", "C1", "C9", "D5", "C8"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"S1", "C1", "S4", "H5", "D5", "D7", "C8", "C9", "H10", "C12", "C13"}));

        // insertion sort with buckets
        sortCases.put(i, mapStrToCards(new String[]{"H5", "S7", "S5", "D5", "H9", "S6", "H6", "D4", "H4", "H13", "H3"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"H3", "D4", "H4", "H5", "S5", "D5", "S6", "H6", "S7", "H9", "H13"}));

        sortCases.put(i, mapStrToCards(new String[]{"S7", "S11", "C7", "S10", "S1", "H11", "D5", "D4", "S3", "D1", "C12"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"S1", "D1", "S3", "D4", "D5", "S7", "C7", "S10", "S11", "H11", "C12"}));

        sortCases.put(i, mapStrToCards(new String[]{"C2", "S5", "C4", "C7", "C13", "S3", "S9", "H6", "S8", "H11", "D4"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"C2", "S3", "C4", "D4", "S5", "H6", "C7", "S8", "S9", "H11", "C13"}));

        // sorts in group
        sortCases.put(i, mapStrToCards(new String[]{"C12", "C10", "D3", "H4", "H1", "H3", "C3", "H5", "S7", "H7", "S9"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"D3", "H3", "C3", "H1", "H4", "H5", "S7", "H7", "S9", "C10", "C12"}));

        // sorts sequential
        sortCases.put(i, mapStrToCards(new String[]{"H1", "S2", "D5", "H4", "S1", "D3", "C4", "S4", "D1", "S3", "D4"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"S1", "S2", "S3", "S4", "D3", "D4", "D5", "D1", "H1", "H4", "C4"}));

        // sorts smart
        sortCases.put(i, mapStrToCards(new String[]{"H1", "S2", "D5", "H4", "S1", "D3", "C4", "S4", "D1", "S3", "D4"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"S1", "S2", "S3", "H4", "C4", "S4", "D3", "D4", "D5", "H1", "D1"}));

        sortCases.put(i, mapStrToCards(new String[]{"C12", "C10", "D3", "H4", "H1", "H3", "C3", "H5", "S7", "H7", "S9"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"H3", "H4", "H5", "D3", "C3", "H1", "S7", "H7", "S9", "C10", "C12"}));

        sortCases.put(i, mapStrToCards(new String[]{"C12", "C10", "D3", "H4", "H1", "H3", "C3", "H5", "S7", "H7", "S3"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"D3", "C3", "S3", "H3", "H4", "H5", "H1", "S7", "H7", "C10", "C12"}));

        //11
        sortCases.put(i, mapStrToCards(new String[]{"S2", "H3", "S3", "S4", "D4", "H5", "S8", "C9", "H9", "S11", "H12"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"S2", "S3", "S4", "S8", "S11", "D4", "H3", "H5", "H9", "H12", "C9"}));

        sortCases.put(i, mapStrToCards(new String[]{"S2", "S1", "S3", "S4", "S5", "H4", "D5", "C4", "H9", "S11", "H12"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"S1", "S2", "S3", "S4", "H4", "C4", "S5", "D5", "H9", "S11", "H12"}));

        sortCases.put(i, mapStrToCards(new String[]{"S2", "S1", "S3", "S4", "S5", "H4", "D5", "C4", "H9", "S6", "H12"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"S1", "S2", "S3", "S4", "S5", "S6", "H4", "C4", "D5", "H9", "H12"}));

        sortCases.put(i, mapStrToCards(new String[]{"S2", "S1", "S3", "S4", "S5", "H4", "D5", "C4", "H9", "S6", "S7"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"S1", "S2", "S3", "S4", "H4", "C4", "S5", "S6", "S7", "D5", "H9"}));

        // 15
        sortCases.put(i, mapStrToCards(new String[]{"S2", "S1", "S3", "S4", "S5", "H4", "D5", "C4", "S8", "S6", "S7"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"S1", "S2", "S3", "S4", "H4", "C4", "S5", "S6", "S7", "S8", "D5"}));

        sortCases.put(i, mapStrToCards(new String[]{"S2", "S1", "S3", "S4", "S5", "H3", "D5", "C3", "S8", "S6", "S7"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"S3", "H3", "C3", "S4", "S5", "S6", "S7", "S8", "S1", "S2", "D5"}));

        sortCases.put(i, mapStrToCards(new String[]{"S2", "S1", "S3", "S4", "S5", "H7", "D5", "C7", "S8", "S6", "S7"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"S1", "S2", "S3", "S4", "S5", "S6", "H7", "C7", "S7", "D5", "S8"}));

        sortCases.put(i, mapStrToCards(new String[]{"S13", "S12", "S11", "S10", "S9", "S8", "S7", "S6", "S5", "H11", "C11"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"S5", "S6", "S7", "S8", "S9", "S10", "S11", "S12", "S13", "H11", "C11"}));

        sortCases.put(i, mapStrToCards(new String[]{"S2", "S1", "S3", "C1", "C2", "C3", "H3", "C5", "S4", "H4", "S5"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"C1", "C2", "C3", "S1", "S2", "S3", "S4", "S5", "H3", "H4", "C5"}));

        // 20
        sortCases.put(i, mapStrToCards(new String[]{"S13", "C8", "S7", "H6", "H2", "H3", "D3", "S3", "H12", "H11", "H10"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"H3", "D3", "S3", "H10", "H11", "H12", "H2", "H6", "S7", "C8", "S13"}));

        sortCases.put(i, mapStrToCards(new String[]{"S2", "S1", "S3", "C1", "C2", "C3", "H3", "C4", "S4", "H4", "S5"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"C1", "C2", "C3", "C4", "S1", "S2", "S3", "S4", "S5", "H3", "H4"}));

        //22
        sortCases.put(i, mapStrToCards(new String[]{"S2", "S1", "S3", "C1", "C2", "C3", "H3", "C4", "S4", "H4", "S7"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"S1", "S2", "S3", "C1", "C2", "C3", "C4", "S4", "H4", "H3", "S7"}));

        sortCases.put(i, mapStrToCards(new String[]{"D7", "S6", "S2", "S3", "D3", "H3", "S4", "C3", "H4", "C4"}));
        sortCasesExpectedResults.put(i++, mapStrToCards(new String[]{"S3", "D3", "H3", "C3", "S4", "H4", "C4", "S2", "S6", "D7"}));

    }

    @Test
    public void insertionSort_GivenAListInput_SortsBasedOnCardOrders() throws Exception {
        // arrange

        // act
        for (int i = 0; i < 3; i++)
            sut.insertionSort(sortCases.get(i));

        // assert
        for (int i = 0; i < 3; i++)
            Assert.assertArrayEquals(sortCases.get(i).toArray(), sortCasesExpectedResults.get(i).toArray());
    }

    @Test
    public void sortInGroups_GivenAListInput_GroupSortCards() throws Exception {
        // arrange

        // act
        for (int i = 6; i < 7; i++)
            sortCasesResults.put(i, sut.sortInGroups(sortCases.get(i)));

        // assert
        for (int i = 6; i < 7; i++)
            Assert.assertArrayEquals(sortCasesResults.get(i).toArray(), sortCasesExpectedResults.get(i).toArray());
    }

    @Test
    public void sortSequential_GivenAListInput_SortCardsSequentially() throws Exception {
        // arrange

        // act
        for (int i = 7; i < 8; i++)
            sortCasesResults.put(i, sut.sortSequential(sortCases.get(i)));

        // assert
        for (int i = 7; i < 8; i++)
            Assert.assertArrayEquals(sortCasesResults.get(i).toArray(), sortCasesExpectedResults.get(i).toArray());
    }

    @Test
    public void sortSmart2_GivenAListInput_SortByGroupsAndSequences() throws Exception {
        // arrange

        // act
        for (int i = 8; i < 24; i++)
            sortCasesResults.put(i, sut.sortSmart2(sortCases.get(i)));

        // assert
        for (int i = 8; i < 24; i++)
            Assert.assertArrayEquals("Failed: " + i, sortCasesResults.get(i).toArray(), sortCasesExpectedResults.get(i).toArray());
    }

    @Test
    public void sortSmart_GivenAListInput_SortByGroupsAndSequences() throws Exception {
        // arrange

        // act
        for (int i = 8; i < 21; i++)
            sortCasesResults.put(i, sut.sortSmart(sortCases.get(i)));

        // assert
        for (int i = 8; i < 21; i++)
            Assert.assertArrayEquals("Failed: " + i, sortCasesResults.get(i).toArray(), sortCasesExpectedResults.get(i).toArray());
    }

    private static List<Card> mapStrToCards(String[] cards) {
        List<Card> cardsList = new ArrayList<Card>(cards.length);

        for (String c : cards) {
            Card card = new Card();
            int order = Integer.parseInt(c.substring(1, c.length()));
            if (c.startsWith("S")) {
                card.setSuitAndOrder(Enums.SuitType.SPADES, order);
            } else if (c.startsWith("D")) {
                card.setSuitAndOrder(Enums.SuitType.DIAMONDS, order);
            } else if (c.startsWith("H")) {
                card.setSuitAndOrder(Enums.SuitType.HEARTS, order);
            } else {
                card.setSuitAndOrder(Enums.SuitType.CLUBS, order);
            }

            cardsList.add(card);
        }

        return cardsList;
    }
}
