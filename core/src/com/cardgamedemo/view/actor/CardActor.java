package com.cardgamedemo.view.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.cardgamedemo.controller.MainController;
import com.cardgamedemo.entity.Card;
import com.cardgamedemo.utils.AssetHelper;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */
public class CardActor extends Group {
    private       float          tiltAngle;
    private final TextureRegion  cardTexture;
    private final BitmapFont     cardFont;
    private final Card           card;
    private final MainController mainController;
    private       int            index;
    private       boolean        focussed;

    public CardActor(MainController mainController, int index, Vector3 pos, float w, final Card card, AssetHelper assetHelper) {
        tiltAngle = pos.z;
        this.card = card;
        this.mainController = mainController;
        this.index = index;
        this.focussed = false;

        cardTexture = assetHelper.getCard(card.getSuitType(), card.getCardType());
        cardFont = assetHelper.getFontBlack26();

        setBounds(pos.x, pos.y, w, cardTexture.getRegionHeight() * w / cardTexture.getRegionWidth());
        setOrigin(getWidth() / 2, getHeight() / 2);
        rotateBy(tiltAngle);

        Actor cardPointActor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                cardFont.draw(batch, card.getPointString(), 12, getParent().getHeight() - 4);
            }
        };

        addActor(cardPointActor);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setClicked();

                return true;
            }
        });

        debugAll();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(cardTexture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), tiltAngle);
        super.draw(batch, parentAlpha);
    }

    public Card getCard() {
        return card;
    }

    public int getIndex() {
        return index;
    }

    public void setClicked() {
        Gdx.app.log(getClass().getSimpleName(), "clicked: " + card.getSuitType() + ":" + card.getOrder());

        // focus on card
        if (!focussed) mainController.focusOn(this);
        else mainController.focusOff(this);
    }

    public void setFocussed(boolean focussed) {
        this.focussed = focussed;
    }

    public void setNewPosition(final Vector3 pos, int index, float focusHeight) {
        Gdx.app.log(getClass().getSimpleName(), "SetNewPosition: " + getX() + " " + getY() + " " + getRotation() + " ");
        this.index = index;
        // change tiltAngle etc.
        addAction(Actions.sequence(Actions.moveBy(0, focusHeight, 0.3f, Interpolation.exp10Out),
                                   Actions.parallel(Actions.delay(1f), Actions.moveTo(pos.x, pos.y, 2.5f, Interpolation.exp10Out),
                                                    Actions.rotateTo(pos.z, 2.4f, Interpolation.exp10Out), Actions.run(new Runnable() {
                                               @Override
                                               public void run() {
                                                   tiltAngle = pos.z;
                                               }
                                           }))));
    }
}
