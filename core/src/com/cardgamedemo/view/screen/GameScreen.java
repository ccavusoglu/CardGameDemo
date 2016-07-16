package com.cardgamedemo.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.cardgamedemo.controller.MainController;
import com.cardgamedemo.utils.AssetHelper;

import javax.inject.Inject;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */
public class GameScreen extends AbstractScreen {
    private final AssetHelper    assetHelper;
    private       MainController mainController;

    @Inject
    public GameScreen(MainController mainController, AssetHelper assetHelper) {
        this.mainController = mainController;
        this.assetHelper = assetHelper;
    }

    @Override
    public void init() {
        stage = new Stage();

        Image imgBg = new Image(assetHelper.getBackground());
        imgBg.setFillParent(true);
        stage.addActor(imgBg);

        Image imgTable = new Image(assetHelper.getTable());
        imgTable.setScaling(Scaling.fillX);
        imgTable.setFillParent(true);
        stage.addActor(imgTable);

        mainController.prepareGameScreen(stage, assetHelper);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
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
    }
}
