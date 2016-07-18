package com.cardgamedemo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.cardgamedemo.entity.Card;
import com.cardgamedemo.entity.Deck;
import com.cardgamedemo.utils.Enums;
import com.cardgamedemo.view.screen.AbstractScreen;
import com.cardgamedemo.view.screen.SplashScreen;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */

/**
 * Game class. Should hold game specific parameters. Generate deck etc.
 */
public class CardGame extends Game {
    private static final int DECK_SIZE = 52;
    private static final int HAND_SIZE = 11;
    private SplashScreen splashScreen;
    private Deck         deck;

    @Inject
    public CardGame(SplashScreen splashScreen) {
        this.splashScreen = splashScreen;
    }

    @Override
    public void create() {
        setScreen(splashScreen);
        deck = generateDeck();
    }

    public List<Card> draw() {
        Collections.shuffle(deck.getCards());
        List<Card> cards = deck.getCards().subList(0, HAND_SIZE);

        //
        //        return cards;
        //
        // TEST deck
//                                List<Card> cards = new ArrayList<Card>();
//
//                                cards.add(deck.getCards().get(4));
//                                cards.add(deck.getCards().get(48));
//                                cards.add(deck.getCards().get(29));
//                                cards.add(deck.getCards().get(14));
//                                cards.add(deck.getCards().get(32));
//                                cards.add(deck.getCards().get(33));
//                                cards.add(deck.getCards().get(10));
//                                cards.add(deck.getCards().get(7));
//                                cards.add(deck.getCards().get(11));
//                                cards.add(deck.getCards().get(43));
//                                cards.add(deck.getCards().get(36));
        //
        Gdx.app.log("------", "----------------------------");
        for (Card card : cards) {
            Gdx.app.log("Draw: ", card.getSuitType() + ":" + card.getOrder() + ":" + card.getPointString() + ":" + card.getPoint());
        }

        return cards;
    }

    public Deck generateDeck() {
        List<Card> cards = new ArrayList<Card>(DECK_SIZE);

        for (int i = 1; i < DECK_SIZE + 1; i++) {
            if (i <= 13) {
                if (i < 11) cards.add(new Card(Enums.SuitType.SPADES, i % 11, Enums.CardType.BASE, getOrderString(i)));
                else if (i == 11) cards.add(new Card(Enums.SuitType.SPADES, 11, Enums.CardType.JACK, getOrderString(i)));
                else if (i == 12) cards.add(new Card(Enums.SuitType.SPADES, 12, Enums.CardType.QUEEN, getOrderString(i)));
                else cards.add(new Card(Enums.SuitType.SPADES, 13, Enums.CardType.KING, getOrderString(i)));
            } else if (i <= 26) {
                if (i < 24) cards.add(new Card(Enums.SuitType.DIAMONDS, i % 13, Enums.CardType.BASE, getOrderString(i)));
                else if (i == 24) cards.add(new Card(Enums.SuitType.DIAMONDS, 11, Enums.CardType.JACK, getOrderString(i)));
                else if (i == 25) cards.add(new Card(Enums.SuitType.DIAMONDS, 12, Enums.CardType.QUEEN, getOrderString(i)));
                else cards.add(new Card(Enums.SuitType.DIAMONDS, 13, Enums.CardType.KING, getOrderString(i)));
            } else if (i <= 39) {
                if (i < 37) cards.add(new Card(Enums.SuitType.HEARTS, i % 13, Enums.CardType.BASE, getOrderString(i)));
                else if (i == 37) cards.add(new Card(Enums.SuitType.HEARTS, 11, Enums.CardType.JACK, getOrderString(i)));
                else if (i == 38) cards.add(new Card(Enums.SuitType.HEARTS, 12, Enums.CardType.QUEEN, getOrderString(i)));
                else cards.add(new Card(Enums.SuitType.HEARTS, 13, Enums.CardType.KING, getOrderString(i)));
            } else {
                if (i < 50) cards.add(new Card(Enums.SuitType.CLUBS, i % 13, Enums.CardType.BASE, getOrderString(i)));
                else if (i == 50) cards.add(new Card(Enums.SuitType.CLUBS, 11, Enums.CardType.JACK, getOrderString(i)));
                else if (i == 51) cards.add(new Card(Enums.SuitType.CLUBS, 12, Enums.CardType.QUEEN, getOrderString(i)));
                else cards.add(new Card(Enums.SuitType.CLUBS, 13, Enums.CardType.KING, getOrderString(i)));
            }
        }

        return new Deck(cards);
    }

    public int getHandSize() {
        return HAND_SIZE;
    }

    @Override
    public void setScreen(Screen screen) {
        ((AbstractScreen) screen).init();
        super.setScreen(screen);
    }

    private String getOrderString(int i) {
        if (i % 13 == 1) return "A";
        if (i % 13 == 11) return "J";
        if (i % 13 == 12) return "Q";
        if (i % 13 == 0) return "K";

        return String.valueOf(i % 13);
    }
}
