package com.cardgamedemo.controller;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

import javax.print.attribute.HashDocAttributeSet;
import java.util.List;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */
public class MainController {
    private IHandLayout   handLayout;
    private CardGameDemo  cardGameDemo;
    private SortHelper    sortHelper;
    private Stage         stage;
    private AssetHelper   assetHelper;
    private List<Vector3> layoutPositions;
    private Hand hand;

    public MainController(IHandLayout handLayout, CardGameDemo cardGameDemo, SortHelper sortHelper) {
        this.handLayout = handLayout;
        this.cardGameDemo = cardGameDemo;
        this.sortHelper = sortHelper;
    }

    public void drawPlayerCards(Enums.ButtonType buttonType) {
        switch (buttonType) {
            case DECK:
                hand = new Hand(cardGameDemo.getGame().draw());
                break;
            case DRAW_ORDER:
                if (hand == null) hand = new Hand(cardGameDemo.getGame().draw());
                sortHelper.sortSequential(hand.getCards());
                break;
            case DRAW_GROUP:
                if (hand == null) hand = new Hand(cardGameDemo.getGame().draw());
                sortHelper.sortInGroups(hand.getCards());
                break;
            case DRAW_SMART:
                if (hand == null) hand = new Hand(cardGameDemo.getGame().draw());
                sortHelper.sortSmart(hand.getCards());
                break;
        }

        int i = 0;
        for (Card card : hand.getCards()) {
            CardActor cardActor = new CardActor(this, layoutPositions.get(i++), handLayout.getCardWidth(), card, assetHelper);

            stage.addActor(cardActor);
        }
    }

    public void focusOn(CardActor cardActor) {

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
}
