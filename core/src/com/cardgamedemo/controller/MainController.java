package com.cardgamedemo.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.cardgamedemo.CardGameDemo;
import com.cardgamedemo.entity.Hand;
import com.cardgamedemo.utils.AssetHelper;
import com.cardgamedemo.utils.Enums;
import com.cardgamedemo.utils.SortHelper;
import com.cardgamedemo.view.IHandLayout;
import com.cardgamedemo.view.actor.CardActor;
import com.cardgamedemo.view.actor.DeckActor;
import com.cardgamedemo.view.widget.DrawButton;

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

    public MainController(IHandLayout handLayout, CardGameDemo cardGameDemo, SortHelper sortHelper) {
        this.handLayout = handLayout;
        this.cardGameDemo = cardGameDemo;
        this.sortHelper = sortHelper;
    }

    public void drawPlayerCards(Enums.ButtonType buttonType) {
        Hand hand = null;

        switch (buttonType) {
            case DECK:
            case DRAW_ORDER:
                hand = sortHelper.sortInOrder(cardGameDemo.getGame().draw());
                break;
            case DRAW_GROUP:
                hand = sortHelper.sortInGroups(cardGameDemo.getGame().draw());
                break;
            case DRAW_SMART:
                hand = sortHelper.sortSmart(cardGameDemo.getGame().draw());
                break;
        }

        for (int i = 0; i < cardGameDemo.getGame().getHandSize(); i++) {
            CardActor cardActor = new CardActor(this, layoutPositions.get(i), handLayout.getCardWidth(), hand.getCards().get(i), assetHelper);

            stage.addActor(cardActor);
        }
    }

    public void focusOn(CardActor cardActor) {

    }

    public void prepareGameScreen(Stage stage, AssetHelper assetHelper) {
        layoutPositions = handLayout.prepareLayout(cardGameDemo.getGame().getHandSize());

        this.stage = stage;
        this.assetHelper = assetHelper;

        DeckActor deckActor = new DeckActor(this, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, assetHelper);
        DrawButton drawButton = new DrawButton(this, Gdx.graphics.getWidth(), (Gdx.graphics.getHeight() / 3) * 2, Enums.ButtonType.DRAW_ORDER, assetHelper.getButton(),
                                               assetHelper.getFontBlack26());
        DrawButton drawButton1 =
                new DrawButton(this, Gdx.graphics.getWidth(), drawButton.getY() - drawButton.getHeight(), Enums.ButtonType.DRAW_GROUP, assetHelper.getButton(),
                               assetHelper.getFontBlack26());
        DrawButton drawButton2 =
                new DrawButton(this, Gdx.graphics.getWidth(), drawButton1.getY() - drawButton1.getHeight(), Enums.ButtonType.DRAW_SMART, assetHelper.getButton(),
                               assetHelper.getFontBlack26());

        stage.addActor(deckActor);
        stage.addActor(drawButton);
        stage.addActor(drawButton1);
        stage.addActor(drawButton2);
    }
}
