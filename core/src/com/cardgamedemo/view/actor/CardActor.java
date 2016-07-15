package com.cardgamedemo.view.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.cardgamedemo.CardGameDemo;
import com.cardgamedemo.entity.Card;
import com.cardgamedemo.utils.AssetHelper;

import javax.inject.Inject;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */
public class CardActor extends Group {
    private final float         tiltAngle;
    private final TextureRegion cardTexture;
    private final BitmapFont    cardFont;
    private final Card card;

    public CardActor(Vector3 pos, float w, final Card card, AssetHelper assetHelper) {
        tiltAngle = pos.z;
        this.card = card;

        cardTexture = assetHelper.getCard(card.getSuitType(), card.getCardType());
        cardFont = assetHelper.getFontWhite36();

        setBounds(pos.x, pos.y, w, cardTexture.getRegionHeight() * w / cardTexture.getRegionWidth());
        setOrigin(getWidth() / 2, getHeight() / 2);

        addActor(new Actor() {
            @Override
            public void act(float delta) {
                rotateBy(tiltAngle);
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                cardFont.draw(batch, card.getPoint() + "", getParent().getWidth() - 16, getParent().getHeight() - 16);
            }
        });

        debugAll();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(cardTexture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), tiltAngle);
    }
}
