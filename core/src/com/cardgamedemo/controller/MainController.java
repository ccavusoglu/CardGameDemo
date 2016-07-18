package com.cardgamedemo.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
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

import java.util.*;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */

/**
 * Only controller, yet. Handles logic, data and updates views (CardActors, DrawButtons, DeckActor, GameScreen).
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
    private ArrayList<CardActor> cardActors;
    private Group                 group;
    private CardActor             focussedCard;
    private Vector3               lastCardActorPos;
    private float                 dragOffsetX;
    private float                 dragOffsetY;
    private CardActor             dragNextActorToCheck;

    public MainController(IHandLayout handLayout, CardGameDemo cardGameDemo, SortHelper sortHelper) {
        this.handLayout = handLayout;
        this.cardGameDemo = cardGameDemo;
        this.sortHelper = sortHelper;
        cardActors = new ArrayList<CardActor>();
        group = new Group();
    }

    public void clearFocus(CardActor cardActor) {
        group.swapActor(cardActor.getIndex() + 1, cardActor.getIndex());
        cardActor.setFocussed(false);
        focussedCard = null;
    }

    public void drag(CardActor actor, float x, float y) {
        float actorX = actor.getX();
        //        Gdx.app.log("ActorX", actorX + " ");

        Vector2 coords = actor.localToParentCoordinates(new Vector2(x, y));
        actor.setPosition(coords.x - dragOffsetX, coords.y - dragOffsetY);

        boolean direction = false;
        // actor moving left or right
        if (actorX > coords.x - dragOffsetX) {
            // no card to check on its left
            if (actor.getIndex() == 0) return;

            dragNextActorToCheck = cardActors.get(actor.getIndex() - 1);
            direction = true;
            //            Gdx.app.log("Drag Main Left:", actor.toString() + " next:" + dragNextActorToCheck.toString());
        } else {
            // no card to check on its right
            if (actor.getIndex() + 1 == cardActors.size()) return;

            dragNextActorToCheck = cardActors.get(actor.getIndex() + 1);
            direction = false;
            //            Gdx.app.log("Drag Main R:", actor.toString() + " next:" + dragNextActorToCheck.toString());
        }

        if (actor.overlaps(dragNextActorToCheck, direction)) {
            swapActorPositions(actor, dragNextActorToCheck);
        }
    }

    public void dragStart(CardActor actor, float x, float y) {
        lastCardActorPos = actor.getPositionVector();
        Gdx.app.log("Main", lastCardActorPos.toString());

        Vector2 coords = actor.localToParentCoordinates(new Vector2(x, y));
        dragOffsetX = coords.x - actor.getX();
        dragOffsetY = coords.y - actor.getY();
        //        if (!actor.getFocussed()) actor.addAction(Actions.moveBy(0, FOCUS_HEIGHT, 0.05f, Interpolation.exp10Out));
    }

    public void dragStop(CardActor actor, float x, float y) {
        actor.moveToNewPosition(lastCardActorPos);
        Gdx.app.log("Main", actor.getPositionVector().toString());
        //        if (actor.getFocussed()) clearFocus(actor);
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
        cardActor.addAction(Actions.moveBy(0, -FOCUS_HEIGHT, 0.05f, Interpolation.pow2Out));
        cardActor.setFocussed(false);
        focussedCard = null;
    }

    public void focusOn(CardActor cardActor) {
        if (focussedCard != null) focusOff(focussedCard);

        group.swapActor(cardActor.getIndex(), cardActor.getIndex() + 1);
        cardActor.addAction(Actions.moveBy(0, FOCUS_HEIGHT, 0.05f, Interpolation.pow2Out));
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

    public void reArrangeGroup() {
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
                    cardActor.reArrangeLayout(layoutPositions.get(i), i, FOCUS_HEIGHT);
                    break;
                }
                i++;
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

    private void swapActorPositions(CardActor dragged, CardActor target) {
        lastCardActorPos = target.getPositionVector();
        target.moveToNewPosition(dragged.getPositionVector());

        int index = dragged.getIndex();
        dragged.swapPosition(target, lastCardActorPos);
        target.setIndex(index);
//
//        if (cardActors.size() < target.getIndex()) cardActors.addLast(target);
//        else if (target.getIndex() == 0) cardActors.addFirst(target);
//        else cardActors.add(target.getIndex() - 1, target);
//
//        if (cardActors.size() < dragged.getIndex()) cardActors.addLast(dragged);
//        else if (dragged.getIndex() == 0) cardActors.addFirst(dragged);
//        else cardActors.add(dragged.getIndex(), dragged);

        group.swapActor(dragged.getIndex(), target.getIndex());

        Collections.swap(cardActors, dragged.getIndex(), target.getIndex());

        Gdx.app.log("MainSwapAct", "Target: " + target.toString() + " Dragged: " + dragged.toString() + " LastPos: " + lastCardActorPos.toString());
    }
}
