package com.cardgamedemo.controller;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.cardgamedemo.CardGame;
import com.cardgamedemo.CardGameDemo;
import com.cardgamedemo.entity.Card;
import com.cardgamedemo.entity.Hand;
import com.cardgamedemo.utils.AssetHelper;
import com.cardgamedemo.utils.Enums;
import com.cardgamedemo.utils.SortHelper;
import com.cardgamedemo.view.IHandLayout;
import com.cardgamedemo.view.actor.CardActor;
import com.cardgamedemo.view.actor.DeckActor;
import com.cardgamedemo.view.actor.HandGroup;
import com.cardgamedemo.view.widget.DrawButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */

/**
 * Only controller, yet. Handles logic, data and updates views (CardActors, DrawButtons, DeckActor, GameScreen).
 */
public class MainController {
    private static final float  FOCUS_HEIGHT = CardGame.FOCUS_HEIGTH;
    private static final String TAG          = "MainController";
    private IHandLayout          handLayout;
    private CardGameDemo         cardGameDemo;
    private SortHelper           sortHelper;
    private Stage                stage;
    private AssetHelper          assetHelper;
    private List<Vector3>        layoutPositions;
    private Hand                 hand;
    private ArrayList<CardActor> cardActors;
    private HandGroup            handGroup;
    private CardActor            focussedCard;
    private Vector3              lastCardActorPos;
    private float                dragOffsetX;
    private float                dragOffsetY;
    private CardActor            dragNextActorToCheck;
    private DeckActor            deckActor;
    private boolean arranged  = false;
    private boolean letAction = false;

    public MainController() {
    }

    public MainController(IHandLayout handLayout, CardGameDemo cardGameDemo, SortHelper sortHelper) {
        this.handLayout = handLayout;
        this.cardGameDemo = cardGameDemo;
        this.sortHelper = sortHelper;
        cardActors = new ArrayList<CardActor>();
        handGroup = new HandGroup();
    }

    public void clearFocus(CardActor cardActor) {
        handGroup.swapActor(cardActor.getIndex() + 1, cardActor.getIndex());
        cardActor.setFocussed(false);
        focussedCard = null;
    }

    public boolean drag(CardActor actor, float x, float y) {
        if (!letAction) return false;
        float actorX = actor.getX();

        Vector2 coords = actor.localToParentCoordinates(new Vector2(x, y));
        actor.setPosition(coords.x - dragOffsetX, coords.y - dragOffsetY);

        boolean direction = false;
        // actor moving left or right
        if (actorX > coords.x - dragOffsetX) {
            // no card to check on its left
            if (actor.getIndex() == 0) return false;

            dragNextActorToCheck = cardActors.get(actor.getIndex() - 1);
            direction = true;
        } else {
            // no card to check on its right
            if (actor.getIndex() + 1 == cardActors.size()) return false;

            dragNextActorToCheck = cardActors.get(actor.getIndex() + 1);
            direction = false;
        }

        if (actor.overlaps(dragNextActorToCheck, direction)) {
            swapActorPositions(actor, dragNextActorToCheck);
        }

        return true;
    }

    public void dragStart(CardActor actor, float x, float y) {
        lastCardActorPos = actor.getPositionVector();

        Vector2 coords = actor.localToParentCoordinates(new Vector2(x, y));
        dragOffsetX = coords.x - actor.getX();
        dragOffsetY = coords.y - actor.getY();
    }

    public void dragStop(CardActor actor, float x, float y) {
        actor.moveToNewPosition(lastCardActorPos);
    }

    public void drawPlayerCards() {
        hand = new Hand(cardGameDemo.getGame().draw());
        handGroup.clear();
        handGroup.remove();
        cardActors.clear();
        deckActor.setVisible(false);
        letAction = false;
        createCardActors();
    }

    public void focusOff(CardActor cardActor) {
        handGroup.swapActor(cardActor.getIndex() + 1, cardActor.getIndex());
        cardActor.addAction(Actions.moveBy(0, -FOCUS_HEIGHT, 0.05f, Interpolation.pow2Out));
        cardActor.setFocussed(false);
        focussedCard = null;
    }

    public void focusOn(CardActor cardActor) {
        if (!letAction) return;
        if (focussedCard != null) focusOff(focussedCard);

        handGroup.swapActor(cardActor.getIndex(), cardActor.getIndex() + 1);
        cardActor.addAction(Actions.moveBy(0, FOCUS_HEIGHT, 0.05f, Interpolation.pow2Out));
        cardActor.setFocussed(true);
        focussedCard = cardActor;
    }

    public void gameScreenUpdate(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public boolean getArranged() {
        return arranged;
    }

    public void setArranged(boolean arranged) {
        this.arranged = arranged;
    }

    public DeckActor getDeckActor() {
        return deckActor;
    }

    public void setDeckActor(DeckActor deckActor) {
        this.deckActor = deckActor;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public HandGroup getHandGroup() {
        return handGroup;
    }

    public void setHandGroup(HandGroup handGroup) {
        this.handGroup = handGroup;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // after last cards action, deck is available
    public void handDrawn(int index) {
        if (index > hand.getCards().size() - 2) {
            deckActor.setVisible(true);
            letAction = true;
        }
    }

    public boolean isLetAction() {
        return letAction;
    }

    public void setLetAction(boolean state) {
        letAction = state;
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

        deckActor = new DeckActor(this, w / 2, h / 2, assetHelper);
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

    public boolean reArrangeGroup() {
        if (!arranged) {
            handGroup.clear();
            ArrayList<CardActor> tempActors = new ArrayList<CardActor>();
            for (int i = 0; i < cardActors.size(); i++) {
                for (CardActor cardActor : cardActors) {
                    if (cardActor.getIndex() == i) {
                        handGroup.addActor(cardActor);
                        tempActors.add(cardActor);
                        break;
                    }
                }
            }

            cardActors = tempActors;
            return (arranged = true);
        }
        return false;
    }

    public void setCardActors(ArrayList<CardActor> cardActors) {
        this.cardActors = cardActors;
    }

    public void setCardGameDemo(CardGameDemo cardGameDemo) {
        this.cardGameDemo = cardGameDemo;
    }

    public void setFocusedCard(CardActor actor) {
        focussedCard = actor;
    }

    public void setLayoutPositions(List<Vector3> layoutPositions) {
        this.layoutPositions = layoutPositions;
    }

    public boolean sortPlayerCards(Enums.ButtonType buttonType) {
        if (!letAction) return false;
        letAction = false;

        if (hand == null) hand = new Hand(cardGameDemo.getGame().draw());

        if (buttonType == Enums.ButtonType.DRAW_ORDER) {
            hand.setCards(sortHelper.sortSequential(hand.getCards()));
        } else if (buttonType == Enums.ButtonType.DRAW_GROUP) {
            hand.setCards(sortHelper.sortInGroups(hand.getCards()));
        } else {
            hand.setCards(sortHelper.sortSmart(hand.getCards()));
        }

        arrangeCards();

        return true;
    }

    protected void arrangeCards() {
        // don't move the card if it had focus
        float height = FOCUS_HEIGHT;
        if (focussedCard != null) {
            clearFocus(focussedCard);
            height = 0;
        }
        arranged = false;

        // loop thru all card actors IN ORDER and set new positions
        for (CardActor cardActor : cardActors) {
            Card card = cardActor.getCard();

            int i = 0;
            for (Card c : hand.getCards()) {
                if (c.equals(card)) {
                    final int index = i;
                    cardActor.reArrangeLayout(layoutPositions.get(i), i, height, new Runnable() {
                        @Override
                        public void run() {
                            handDrawn(index);
                        }
                    });
                    break;
                }
                i++;
            }
        }
    }

    protected void createCardActors() {
        int i = 0;
        for (Card card : hand.getCards()) {
            CardActor cardActor = new CardActor(this, i, layoutPositions.get(i++), handLayout.getCardWidth(), card, assetHelper);
            cardActors.add(cardActor);
            handGroup.addActor(cardActor);
        }

        stage.addActor(handGroup);
    }

    protected void swapActorPositions(CardActor dragged, CardActor target) {
        if (dragged.getFocussed()) clearFocus(dragged);
        if (target.getFocussed()) clearFocus(target);
        if (focussedCard != null) focusOff(focussedCard);

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

        handGroup.swapActor(dragged.getIndex(), target.getIndex());

        Collections.swap(cardActors, dragged.getIndex(), target.getIndex());

        //        Gdx.app.log(TAG, "Target: " + target.toString() + " Dragged: " + dragged.toString() + " LastPos: " + lastCardActorPos.toString());
    }
}
