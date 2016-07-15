package com.cardgamedemo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.cardgamedemo.entity.Card;
import com.cardgamedemo.entity.Deck;
import com.cardgamedemo.utils.Enums;
import com.cardgamedemo.view.screen.AbstractScreen;
import com.cardgamedemo.view.screen.SplashScreen;

import javax.inject.Inject;
import java.util.*;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */
public class CardGame extends Game {
    private static final int DECK_SIZE = 52;
    private static final int HAND_SIZE = 11;
    private SplashScreen splashScreen;
    private Deck deck;

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
//        Collections.shuffle(deck.getCards());
//        Collections.binarySearch()
//        return deck.getCards().subList(0, HAND_SIZE);

        // TEST
        List<Card> cards = new ArrayList<Card>();

        cards.add(deck.getCards().get(26));
        cards.add(deck.getCards().get(1));
        cards.add(deck.getCards().get(17));
        cards.add(deck.getCards().get(29));
        cards.add(deck.getCards().get(0));
        cards.add(deck.getCards().get(15));
        cards.add(deck.getCards().get(42));
        cards.add(deck.getCards().get(3));
        cards.add(deck.getCards().get(13));
        cards.add(deck.getCards().get(2));
        cards.add(deck.getCards().get(16));

        return cards;
    }

    public Deck generateDeck() {
        List<Card> cards = new ArrayList<Card>(DECK_SIZE);

        for (int i = 1; i < DECK_SIZE + 1; i++) {
            if (i <= 13) {
                if (i < 11) cards.add(new Card(Enums.SuitType.SPADES, i, Enums.CardType.BASE));
                else if (i == 11) cards.add(new Card(Enums.SuitType.SPADES, i, Enums.CardType.JACK));
                else if (i == 12) cards.add(new Card(Enums.SuitType.SPADES, i, Enums.CardType.QUEEN));
                else cards.add(new Card(Enums.SuitType.SPADES, i, Enums.CardType.KING));
            } else if (i <= 26) {
                if (i < 24) cards.add(new Card(Enums.SuitType.DIAMONDS, i, Enums.CardType.BASE));
                else if (i == 24) cards.add(new Card(Enums.SuitType.DIAMONDS , i, Enums.CardType.JACK));
                else if (i == 25) cards.add(new Card(Enums.SuitType.DIAMONDS, i, Enums.CardType.QUEEN));
                else cards.add(new Card(Enums.SuitType.DIAMONDS, i, Enums.CardType.KING));
            } else if (i <= 39) {
                if (i < 37) cards.add(new Card(Enums.SuitType.HEARTS, i, Enums.CardType.BASE));
                else if (i == 37) cards.add(new Card(Enums.SuitType.HEARTS, i, Enums.CardType.JACK));
                else if (i == 38) cards.add(new Card(Enums.SuitType.HEARTS, i, Enums.CardType.QUEEN));
                else cards.add(new Card(Enums.SuitType.HEARTS, i, Enums.CardType.KING));
            } else {
                if (i < 50) cards.add(new Card(Enums.SuitType.CLUBS, i, Enums.CardType.BASE));
                else if (i == 50) cards.add(new Card(Enums.SuitType.CLUBS, i, Enums.CardType.JACK));
                else if (i == 51) cards.add(new Card(Enums.SuitType.CLUBS, i, Enums.CardType.QUEEN));
                else cards.add(new Card(Enums.SuitType.CLUBS, i, Enums.CardType.KING));
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
}
