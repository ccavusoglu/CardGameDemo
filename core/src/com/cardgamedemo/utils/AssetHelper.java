package com.cardgamedemo.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */

/**
 * Loads assets using LibGDX AssetManager.
 * Lazy create objects for corresponding assets.
 */
@Singleton
public class AssetHelper {
    private final HashMap<String, TextureRegion> cardHashMap;
    private       AssetManager                   assetManager;

    private TextureAtlas gameTextureAtlas;
    private TextureAtlas splashTextureAtlas;

    private TextureRegion background;
    private TextureRegion table;
    private TextureRegion button;
    private TextureRegion loadingBack;
    private TextureRegion loadingBar;
    private BitmapFont    black50;
    private BitmapFont    white60;
    private BitmapFont    black26;

    private TextureRegion cardBack;

    @Inject
    public AssetHelper(AssetManager assetManager) {
        this.assetManager = assetManager;
        cardHashMap = new HashMap<String, TextureRegion>();
    }

    public TextureRegion getBackground() {
        if (background == null) background = new TextureRegion(assetManager.get("images/SplashBg.jpg", Texture.class));

        return background;
    }

    public TextureRegion getButton() {
        if (button == null) button = new TextureRegion(assetManager.get("images/Button.png", Texture.class));

        return button;
    }

    public TextureRegion getCard(Enums.SuitType suitType, Enums.CardType cardType) {
        String key = suitType.toString() + cardType.toString();
        if (cardHashMap.get(key) == null) cardHashMap.put(key, new TextureRegion(gameTextureAtlas.findRegion(key)));

        return cardHashMap.get(key);
    }

    public TextureRegion getCardBack() {
        if (cardBack == null) cardBack = new TextureRegion(gameTextureAtlas.findRegion("card_back"));

        return cardBack;
    }

    public BitmapFont getFontBlack26() {
        if (black26 == null) black26 = assetManager.get("images/26pt_black.fnt", BitmapFont.class);

        return black26;
    }

    public BitmapFont getFontBlack50() {
        if (black50 == null) black50 = assetManager.get("images/50pt_black.fnt", BitmapFont.class);

        return black50;
    }

    public BitmapFont getFontWhite60() {
        if (white60 == null) white60 = assetManager.get("images/60pt.fnt", BitmapFont.class);

        return white60;
    }

    public TextureRegion getLoadingBack() {
        if (loadingBack == null) loadingBack = new TextureRegion(splashTextureAtlas.findRegion("loading_back"));

        return loadingBack;
    }

    public TextureRegion getLoadingBar() {
        if (loadingBar == null) loadingBar = new TextureRegion(splashTextureAtlas.findRegion("loading_bar"));

        return loadingBar;
    }

    public TextureRegion getTable() {
        if (table == null) table = new TextureRegion(assetManager.get("images/Table.png", Texture.class));

        return table;
    }

    public void loadGameAssets() {
        assetManager.load("images/Cards.atlas", TextureAtlas.class);
        assetManager.load("images/Table.png", Texture.class);
        assetManager.load("images/Button.png", Texture.class);
        assetManager.load("images/26pt_black.fnt", BitmapFont.class);
        assetManager.load("images/60pt.fnt", BitmapFont.class);
    }

    public void loadSplashAssets() {
        assetManager.load("images/LoadingBar.atlas", TextureAtlas.class);
        assetManager.load("images/SplashBg.jpg", Texture.class);
        assetManager.load("images/50pt_black.fnt", BitmapFont.class);
        assetManager.finishLoading();

        setSplashAssets();
    }

    public void setGameAssets() {
        gameTextureAtlas = assetManager.get("images/Cards.atlas", TextureAtlas.class);
    }

    public void setSplashAssets() {
        splashTextureAtlas = assetManager.get("images/LoadingBar.atlas", TextureAtlas.class);
    }

    public boolean update() {
        return assetManager.update();
    }
}
