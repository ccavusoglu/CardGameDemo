package com.cardgamedemo.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.cardgamedemo.CardGame;
import com.cardgamedemo.CardGameDemo;
import com.cardgamedemo.entity.Card;
import com.cardgamedemo.entity.Deck;
import com.cardgamedemo.entity.Hand;
import com.cardgamedemo.utils.AssetHelper;
import com.cardgamedemo.utils.SortHelper;
import com.cardgamedemo.view.IHandLayout;
import com.cardgamedemo.view.actor.CardActor;
import com.cardgamedemo.view.actor.DeckActor;
import com.cardgamedemo.view.actor.HandActor;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */
public class MainController {
    private IHandLayout handLayout;
    private CardGameDemo    cardGameDemo;
    private SortHelper sortHelper;

    public MainController(IHandLayout handLayout, CardGameDemo cardGameDemo, SortHelper sortHelper) {
        this.handLayout = handLayout;
        this.cardGameDemo = cardGameDemo;
        this.sortHelper = sortHelper;
    }

    public void prepareGameScreen(Stage stage, AssetHelper assetHelper) {
        List<Vector3> positions = handLayout.prepareLayout(cardGameDemo.getGame().getHandSize());

        Hand hand = sortHelper.sortInOrder(cardGameDemo.getGame().draw());

        for (int i = 0; i < cardGameDemo.getGame().getHandSize(); i++) {
            CardActor cardActor = new CardActor(positions.get(i), handLayout.getCardWidth(), hand.getCards().get(i), assetHelper);

            stage.addActor(cardActor);
        }

        DeckActor deckActor = new DeckActor();
        HandActor handActor = new HandActor();
    }
}
