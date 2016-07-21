package com.cardgamedemo.view.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.cardgamedemo.controller.MainController;
import com.cardgamedemo.utils.AssetHelper;
import com.cardgamedemo.utils.Enums;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */

/**
 * View for deck object. For now, only a button which generates deck.
 */
public class DeckActor extends Actor {
    private final MainController mainController;
    private       TextureRegion  deck;

    public DeckActor(final MainController mainController, float x, float y, AssetHelper assetHelper) {
        deck = assetHelper.getCardBack();

        this.mainController = mainController;

        setBounds(x - deck.getRegionWidth() / 2, y, deck.getRegionWidth(), deck.getRegionHeight());
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                addAction(Actions.sequence(Actions.scaleBy(0.1f, 0.1f, 0.02f, Interpolation.exp10Out), Actions.scaleTo(1, 1, 0.03f, Interpolation.exp10In)));

                mainController.drawPlayerCards();

                return true;
            }
        });

//        debug();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(deck, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
