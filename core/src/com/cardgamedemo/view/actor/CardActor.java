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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.cardgamedemo.controller.MainController;
import com.cardgamedemo.entity.Card;
import com.cardgamedemo.utils.AssetHelper;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */

/**
 * View for Cards. Handles inputs and delegates logic to controller.
 */
public class CardActor extends Group {
    private static final String TAG = "CardActor";
    private final TextureRegion  cardTexture;
    private final BitmapFont     cardFont;
    private final Card           card;
    private final MainController mainController;
    private       float          tiltAngle;
    private       int            index;
    private       boolean        focussed;
    private       Vector3        positionVector;

    public CardActor(MainController mainController, int index, Vector3 pos, float w, final Card card, AssetHelper assetHelper) {
        tiltAngle = pos.z;
        this.card = card;
        this.mainController = mainController;
        this.index = index;
        this.focussed = false;
        this.positionVector = pos;

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

        addListener(new DragListener(this));
        addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                singleTap();
            }
        });

        //        debugAll();
    }

    @Override
    public void act(float delta) {
        //        if (card.getOrder() == 3 && card.getSuitType() == Enums.SuitType.DIAMONDS) {
        //            Gdx.app.log(TAG, "rotation: " + getRotation() + " index " + index);
        //            Gdx.app.log(TAG, "POS: " + positionVector.toString());
        //        }
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(cardTexture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        super.draw(batch, parentAlpha);
    }

    public String toString() {
        return "[index: " + index + "] " + positionVector;
    }

    public double getAngle() {
        return tiltAngle;
    }

    public Card getCard() {
        return card;
    }

    public boolean getFocussed() {
        return focussed;
    }

    public void setFocussed(boolean focussed) {
        this.focussed = focussed;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Vector3 getPositionVector() {
        return positionVector;
    }

    public void moveToNewPosition(Vector3 pos) {
        positionVector = pos;

        addAction(
                Actions.sequence(Actions.parallel(Actions.moveTo(pos.x, pos.y, 0.5f, Interpolation.pow2Out), Actions.rotateTo(pos.z, 0.4f, Interpolation.pow2Out))));
    }

    public boolean overlaps(CardActor actor, boolean left) {
        if (left) return getX() < actor.getX() + actor.getWidth() / 4;

        return getX() + getWidth() > actor.getX() + actor.getWidth() / 2;
        //        return getX() < actor.getX() + actor.getWidth() && getX() + getWidth() > actor.getX() && getY() < actor.getY() + actor
        //                .getHeight() && getY() + getHeight() > actor.getY();
    }

    public void reArrangeLayout(final Vector3 pos, int index, float focusHeight) {
        this.index = index;
        this.positionVector = pos;

        if (focussed) {
            focusHeight = 0;
            focussed = false;
        }

        // change tiltAngle etc.
        addAction(Actions.sequence(Actions.moveBy(0, focusHeight, 0.3f, Interpolation.pow2Out), Actions.delay(0.5f), Actions.parallel(Actions.run(new Runnable() {
            @Override
            public void run() {
                mainController.reArrangeGroup();
            }
        }), Actions.moveTo(pos.x, pos.y, 2.1f, Interpolation.exp5Out), Actions.rotateTo(pos.z, 2.0f, Interpolation.exp5Out)), Actions.run(new Runnable() {
            @Override
            public void run() {
                mainController.setLayoutArranged();
            }
        })));
    }

    public void singleTap() {
        Gdx.app.log(getClass().getSimpleName(), "clicked: " + card.getSuitType() + ":" + card.getOrder());

        // focus on card
        if (!focussed) mainController.focusOn(this);
        else mainController.focusOff(this);
    }

    public void swapPosition(CardActor target, Vector3 targetPos) {
        index = target.getIndex();
        positionVector = targetPos;
        addAction(Actions.rotateTo(targetPos.z, 0.5f, Interpolation.exp5Out));
    }

    private class DragListener extends com.badlogic.gdx.scenes.scene2d.utils.DragListener {
        private final CardActor actor;

        public DragListener(CardActor actor) {
            //            setTapSquareSize(48);
            this.actor = actor;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            //            Gdx.app.log(TAG, "Gesture touchDown: " + x + " " + y);
            return super.touchDown(event, x, y, pointer, button);
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            //            Gdx.app.log(TAG, "Gesture touchUp: " + x + " " + y);
            super.touchUp(event, x, y, pointer, button);
        }

        @Override
        public void dragStart(InputEvent event, float x, float y, int pointer) {
            //            Gdx.app.log(TAG, "Gesture dragStart: " + x + " " + y);
            mainController.dragStart(actor, x, y);
            super.dragStart(event, x, y, pointer);
        }

        @Override
        public void drag(InputEvent event, float x, float y, int pointer) {
            mainController.drag(actor, x, y);
            super.drag(event, x, y, pointer);
        }

        @Override
        public void dragStop(InputEvent event, float x, float y, int pointer) {
            //            Gdx.app.log(TAG, "Gesture dragStop: " + x + " " + y);
            mainController.dragStop(actor, x, y);
            super.dragStop(event, x, y, pointer);
        }

        @Override
        public void cancel() {
            //            Gdx.app.log(TAG, "Gesture cancel");
            super.cancel();
        }
    }
}
