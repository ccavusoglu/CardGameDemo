package com.cardgamedemo.view.widget;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.cardgamedemo.controller.MainController;
import com.cardgamedemo.utils.Enums;

/**
 * Created by Çağatay Çavuşoğlu on 16.07.2016.
 */
public class DrawButton extends Actor {
    private final TextureRegion    buttonBack;
    private final BitmapFont       buttonFont;
    private final String           text;
    private final Enums.ButtonType buttonType;
    private       GlyphLayout      glyphLayout;

    public DrawButton(final MainController mainController, float x, float y, final Enums.ButtonType buttonType, TextureRegion button, BitmapFont buttonFont) {
        this.buttonBack = button;
        this.buttonFont = buttonFont;
        this.buttonType = buttonType;

        // TODO: move
        if (buttonType == Enums.ButtonType.DRAW_ORDER) text = "1-2-3 \nSORT";
        else if (buttonType == Enums.ButtonType.DRAW_GROUP) text = "GROUP \nSORT";
        else if (buttonType == Enums.ButtonType.DRAW_SMART) text = "SMART \nSORT";
        else text = "";

        glyphLayout = new GlyphLayout();
        glyphLayout.setText(buttonFont, text);

        setBounds(x - button.getRegionWidth() - 32, y, button.getRegionWidth(), button.getRegionHeight());
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                addAction(Actions.sequence(Actions.scaleBy(0.1f, 0.1f, 0.02f, Interpolation.exp10Out), Actions.scaleTo(1, 1, 0.03f, Interpolation.exp10In)));

                mainController.drawPlayerCards(buttonType);
                return true;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(buttonBack, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        buttonFont.draw(batch, text, getX() + 8, getY() + getHeight() - 16, getWidth(), Align.center, true);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
