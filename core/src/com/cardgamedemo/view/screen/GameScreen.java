package com.cardgamedemo.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.cardgamedemo.controller.MainController;
import com.cardgamedemo.utils.AssetHelper;

import javax.inject.Inject;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */
public class GameScreen extends AbstractScreen {
    private static final String TAG = "GameScreen";

    private final AssetHelper    assetHelper;
    private       MainController mainController;

    @Inject
    public GameScreen(MainController mainController, AssetHelper assetHelper) {
        this.mainController = mainController;
        this.assetHelper = assetHelper;
    }

    @Override
    public void init() {
        stage = mainController.prepareGameScreen(assetHelper);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new GestureDetector(this));
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        mainController.gameScreenUpdate(delta);
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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log(TAG, "touchDown: " + screenX + " " + screenY);
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Gdx.app.log(TAG, "touchDragged: " + screenX + " " + screenY);
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Gdx.app.log(TAG, "Gesture touchDown: " + x + " " + y);
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Gdx.app.log(TAG, "Gesture tap: " + x + " " + y);
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        Gdx.app.log(TAG, "Gesture longPress: " + x + " " + y);
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        Gdx.app.log(TAG, "Gesture fling: " + velocityX + " " + velocityY);
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        Gdx.app.log(TAG, "Gesture pan: " + x + " " + y);
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        Gdx.app.log(TAG, "Gesture pan: " + x + " " + y);
        return false;
    }
}
