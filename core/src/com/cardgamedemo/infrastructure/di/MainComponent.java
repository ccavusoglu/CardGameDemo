package com.cardgamedemo.infrastructure.di;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.cardgamedemo.CardGameDemo;
import dagger.Component;

import javax.inject.Singleton;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */
@Singleton
@Component(modules = MainModule.class)
public interface MainComponent {
    void inject(CardGameDemo applicationListener);

    void inject(Actor actor);
}
