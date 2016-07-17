package com.cardgamedemo.controller;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.cardgamedemo.CardGameDemo;
import com.cardgamedemo.entity.Card;
import com.cardgamedemo.entity.Hand;
import com.cardgamedemo.utils.AssetHelper;
import com.cardgamedemo.utils.Enums;
import com.cardgamedemo.utils.SortHelper;
import com.cardgamedemo.view.IHandLayout;
import com.cardgamedemo.view.actor.CardActor;
import com.cardgamedemo.view.actor.DeckActor;
import com.cardgamedemo.view.widget.DrawButton;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */
public class MainController {
    private static final float FOCUS_HEIGHT = 48;
    private IHandLayout           handLayout;
    private CardGameDemo          cardGameDemo;
    private SortHelper            sortHelper;
    private Stage                 stage;
    private AssetHelper           assetHelper;
    private List<Vector3>         layoutPositions;
    private Hand                  hand;
    private LinkedList<CardActor> cardActors;
    private Group                 group;
    private CardActor             focussedCard;

    public MainController(IHandLayout handLayout, CardGameDemo cardGameDemo, SortHelper sortHelper) {
        this.handLayout = handLayout;
        this.cardGameDemo = cardGameDemo;
        this.sortHelper = sortHelper;
        cardActors = new LinkedList<CardActor>();
        group = new Group();
    }

    public void drawPlayerCards(Enums.ButtonType buttonType) {
        switch (buttonType) {
            case DECK:
                hand = new Hand(cardGameDemo.getGame().draw());
                group.clear();
                group.remove();
                cardActors.clear();
                break;
            case DRAW_ORDER:
                if (hand == null) hand = new Hand(cardGameDemo.getGame().draw());
                hand.setCards(sortHelper.sortSequential(hand.getCards()));
                arrangeCards();
                break;
            case DRAW_GROUP:
                if (hand == null) hand = new Hand(cardGameDemo.getGame().draw());
                hand.setCards(sortHelper.sortInGroups(hand.getCards()));
                arrangeCards();
                break;
            case DRAW_SMART:
                if (hand == null) hand = new Hand(cardGameDemo.getGame().draw());
                hand.setCards(sortHelper.sortSmart(hand.getCards()));
                arrangeCards();
                break;
        }

        if (cardActors.size() == 0) createCardActors();
    }

    public void focusOff(CardActor cardActor) {
        group.swapActor(cardActor.getIndex() + 1, cardActor.getIndex());
        cardActor.addAction(Actions.moveBy(0, -FOCUS_HEIGHT, 0.05f, Interpolation.exp10Out));
        cardActor.setFocussed(false);
        focussedCard = null;
    }

    public void focusOn(CardActor cardActor) {
        if (focussedCard != null) focusOff(focussedCard);

        group.swapActor(cardActor.getIndex(), cardActor.getIndex() + 1);
        cardActor.addAction(Actions.moveBy(0, FOCUS_HEIGHT, 0.05f, Interpolation.exp10Out));
        cardActor.setFocussed(true);
        focussedCard = cardActor;
    }

    public void gameScreenUpdate(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public Stage prepareGameScreen(AssetHelper assetHelper) {
        layoutPositions = handLayout.prepareLayout(cardGameDemo.getGame().getHandSize());

        float w = CardGameDemo.WORLD_WIDTH;
        float h = CardGameDemo.WORLD_HEIGHT;

        this.stage = new Stage(new StretchViewport(w, h));

        Image imgBg = new Image(assetHelper.getBackground());
        imgBg.setFillParent(true);
        stage.addActor(imgBg);

        Image imgTable = new Image(assetHelper.getTable());
        imgTable.setScaling(Scaling.fillX);
        imgTable.setFillParent(true);
        stage.addActor(imgTable);

        this.assetHelper = assetHelper;

        DeckActor deckActor = new DeckActor(this, w / 2, h / 2, assetHelper);
        DrawButton drawButton = new DrawButton(this, w, (h / 3) * 2, Enums.ButtonType.DRAW_ORDER, assetHelper.getButton(), assetHelper.getFontBlack26());
        DrawButton drawButton1 = new DrawButton(this, w, drawButton.getY() - drawButton.getHeight(), Enums.ButtonType.DRAW_GROUP, assetHelper.getButton(),
                                                assetHelper.getFontBlack26());
        DrawButton drawButton2 = new DrawButton(this, w, drawButton1.getY() - drawButton1.getHeight(), Enums.ButtonType.DRAW_SMART, assetHelper.getButton(),
                                                assetHelper.getFontBlack26());

        stage.addActor(deckActor);
        stage.addActor(drawButton);
        stage.addActor(drawButton1);
        stage.addActor(drawButton2);

        return stage;
    }

    private void arrangeCards() {
        if (focussedCard != null) focusOff(focussedCard);

        HashMap<Integer, CardActor> posSwap = new HashMap<Integer, CardActor>();

        // loop thru all card actors IN ORDER and set new positions
        for (CardActor cardActor : cardActors) {
            Card card = cardActor.getCard();

            int i = 0;
            for (Card c : hand.getCards()) {
                if (c.equals(card)) {
                    posSwap.put(i, cardActor);
                    cardActor.setNewPosition(layoutPositions.get(i), i, FOCUS_HEIGHT);
                    break;
                }
                i++;
            }
        }

        // TODO: 18.07.2016 => :/
        group.clear();
        for (int i = 0; i < cardActors.size(); i++) {
            for (CardActor cardActor : cardActors) {
                if (cardActor.getIndex() == i) {
                    group.addActor(cardActor);
                    break;
                }
            }
        }
    }

    private void createCardActors() {
        group = new Group();

        int i = 0;
        for (Card card : hand.getCards()) {
            CardActor cardActor = new CardActor(this, i, layoutPositions.get(i++), handLayout.getCardWidth(), card, assetHelper);
            cardActors.add(cardActor);
            group.addActor(cardActor);
        }

        stage.addActor(group);
    }
}
