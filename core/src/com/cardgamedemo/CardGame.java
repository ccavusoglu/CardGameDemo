package com.cardgamedemo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.cardgamedemo.entity.Card;
import com.cardgamedemo.entity.Deck;
import com.cardgamedemo.utils.Enums;
import com.cardgamedemo.view.screen.AbstractScreen;
import com.cardgamedemo.view.screen.SplashScreen;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */
public class CardGame extends Game {
    private static final int DECK_SIZE = 52;
    private SplashScreen splashScreen;

    @Inject
    public CardGame(SplashScreen splashScreen) {
        this.splashScreen = splashScreen;
    }

    @Override
    public void create() {
        setScreen(splashScreen);
    }

    public Deck generateDeck() {
        List<Card> cards = new ArrayList<Card>(DECK_SIZE);

        for (int i = 0; i < DECK_SIZE; i++) {
            if (i < 13) {
                if (i < 10) cards.add(new Card(Enums.SuitType.SPADES, i, Enums.CardType.BASE));
                else if (i == 10) cards.add(new Card(Enums.SuitType.SPADES, i, Enums.CardType.JACK));
                else if (i == 11) cards.add(new Card(Enums.SuitType.SPADES, i, Enums.CardType.QUEEN));
                else cards.add(new Card(Enums.SuitType.SPADES, i, Enums.CardType.KING));
            } else if (i < 26) {
                if (i < 23) cards.add(new Card(Enums.SuitType.DIAMONDS, i, Enums.CardType.BASE));
                else if (i == 24) cards.add(new Card(Enums.SuitType.DIAMONDS , i, Enums.CardType.JACK));
                else if (i == 25) cards.add(new Card(Enums.SuitType.DIAMONDS, i, Enums.CardType.QUEEN));
                else cards.add(new Card(Enums.SuitType.DIAMONDS, i, Enums.CardType.KING));
            } else if (i < 39) {
                if (i < 36) cards.add(new Card(Enums.SuitType.HEARTS, i, Enums.CardType.BASE));
                else if (i == 37) cards.add(new Card(Enums.SuitType.HEARTS, i, Enums.CardType.JACK));
                else if (i == 38) cards.add(new Card(Enums.SuitType.HEARTS, i, Enums.CardType.QUEEN));
                else cards.add(new Card(Enums.SuitType.HEARTS, i, Enums.CardType.KING));
            } else {
                if (i < 49) cards.add(new Card(Enums.SuitType.CLUBS, i, Enums.CardType.BASE));
                else if (i == 50) cards.add(new Card(Enums.SuitType.CLUBS, i, Enums.CardType.JACK));
                else if (i == 51) cards.add(new Card(Enums.SuitType.CLUBS, i, Enums.CardType.QUEEN));
                else cards.add(new Card(Enums.SuitType.CLUBS, i, Enums.CardType.KING));
            }
        }

        return new Deck(cards);
    }

    @Override
    public void setScreen(Screen screen) {
        ((AbstractScreen) screen).init();
        super.setScreen(screen);
    }
}
