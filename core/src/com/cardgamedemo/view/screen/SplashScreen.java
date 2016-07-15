package com.cardgamedemo.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.cardgamedemo.CardGameDemo;
import com.cardgamedemo.utils.AssetHelper;
import com.cardgamedemo.view.widget.LoadingBar;

import javax.inject.Inject;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */
public class SplashScreen extends AbstractScreen {
    private final AssetHelper  assetHelper;
    private final CardGameDemo cardGameDemo;
    private final GameScreen   gameScreen;
    private       GlyphLayout  glyphLayout;
    private       BitmapFont   font;

    @Inject
    public SplashScreen(AssetHelper assetHelper, CardGameDemo cardGameDemo, GameScreen gameScreen) {
        this.assetHelper = assetHelper;
        this.cardGameDemo = cardGameDemo;
        this.gameScreen = gameScreen;
    }

    @Override
    public void init() {
        assetHelper.loadSplashAssets();
        assetHelper.loadGameAssets();

        font = assetHelper.getFontBlack50();

        stage = new Stage();
        Image img = new Image(assetHelper.getBackground());
        img.setFillParent(true);
        stage.addActor(img);
        stage.addActor(new LoadingBar(assetHelper));

        glyphLayout = new GlyphLayout();
        glyphLayout.setText(font, "GAME NAME HERE");
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        stage.getBatch().begin();
        font.draw(stage.getBatch(), glyphLayout, Gdx.graphics.getWidth() / 2 - glyphLayout.width / 2, (Gdx.graphics.getHeight() / 3) * 2);
        stage.getBatch().end();

        if (assetHelper.update()) {
            assetHelper.setGameAssets();

            cardGameDemo.getGame().setScreen(gameScreen);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
    }
}
