package com.cardgamedemo.infrastructure.di;

import com.badlogic.gdx.assets.AssetManager;
import com.cardgamedemo.CardGameDemo;
import com.cardgamedemo.controller.MainController;
import com.cardgamedemo.utils.SortHelper;
import com.cardgamedemo.view.HandLayout;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */
@Module
public class MainModule {
    private CardGameDemo cardGameDemo;

    public MainModule(CardGameDemo cardGameDemo) {
        this.cardGameDemo = cardGameDemo;
    }

    @Provides
    @Singleton
    MainController provideMainController() {
        return new MainController(new HandLayout(), cardGameDemo, new SortHelper());
    }

    @Provides
    @Singleton
    AssetManager provideAssetManager() {
        return new AssetManager();
    }

    @Provides
    @Singleton
    CardGameDemo provideCardGameDemo() {
        return cardGameDemo;
    }
}
