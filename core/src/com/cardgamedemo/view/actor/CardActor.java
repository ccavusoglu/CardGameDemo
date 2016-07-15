package com.cardgamedemo.view.actor;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.cardgamedemo.entity.Card;
import com.cardgamedemo.utils.AssetHelper;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */
public class CardActor extends Group implements InputProcessor {
    private final float         tiltAngle;
    private final TextureRegion cardTexture;
    private final BitmapFont    cardFont;
    private final Card          card;

    public CardActor(Vector3 pos, float w, final Card card, AssetHelper assetHelper) {
        tiltAngle = pos.z;
        this.card = card;

        cardTexture = assetHelper.getCard(card.getSuitType(), card.getCardType());
        cardFont = assetHelper.getFontBlack26();

        setBounds(pos.x, pos.y, w, cardTexture.getRegionHeight() * w / cardTexture.getRegionWidth());
        setOrigin(getWidth() / 2, getHeight() / 2);
        rotateBy(tiltAngle);

        Actor cardPointActor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                cardFont.draw(batch, card.getPoint() + "", 32, getParent().getHeight() - 8);
            }
        };

        addActor(cardPointActor);
        debugAll();
    }

    public void setNewPosition() {
        // change tiltAngle etc.
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

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
