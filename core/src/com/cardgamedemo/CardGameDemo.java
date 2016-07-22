package com.cardgamedemo;

import com.badlogic.gdx.ApplicationAdapter;
import com.cardgamedemo.infrastructure.di.DaggerMainComponent;
import com.cardgamedemo.infrastructure.di.MainComponent;
import com.cardgamedemo.infrastructure.di.MainModule;

import javax.inject.Inject;

public class CardGameDemo extends ApplicationAdapter {
    public static final int WORLD_WIDTH  = 1280;
    public static final int WORLD_HEIGHT = 720;

    private final MainComponent component;
    @Inject       CardGame      cardGame;

    public CardGameDemo() {
        component = DaggerMainComponent.builder().mainModule(new MainModule(this)).build();
    }

    @Override
    public void create() {
        init();
        cardGame.create();
    }

    @Override
    public void resize(int width, int height) {
        cardGame.resize(width, height);
    }

    @Override
    public void render() {
        cardGame.render();
    }

    @Override
    public void pause() {
        cardGame.pause();
    }

    @Override
    public void resume() {
        cardGame.resume();
    }

    @Override
    public void dispose() {
        cardGame.dispose();
    }

    public MainComponent getComponent() {
        return component;
    }

    public CardGame getGame() {
        return cardGame;
    }

    public void init() {
        component.inject(this);
    }
}
