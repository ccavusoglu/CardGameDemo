package com.cardgamedemo.view.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.cardgamedemo.utils.AssetHelper;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */
public class LoadingBar extends Group {
    private TextureRegion loadingBar;
    private TextureRegion loadingBack;

    public LoadingBar(AssetHelper assetHelper) {
        loadingBar = assetHelper.getLoadingBar();
        loadingBack = assetHelper.getLoadingBack();

        setBounds(Gdx.graphics.getWidth() / 2 - loadingBack.getRegionWidth() / 2,
                  Gdx.graphics.getHeight() / 2 - loadingBack.getRegionHeight() / 2 - loadingBar.getRegionHeight() / 2, loadingBar.getRegionWidth(),
                  loadingBar.getRegionHeight());
        setOrigin(loadingBar.getRegionWidth() / 2, loadingBar.getRegionHeight() / 2);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        rotateBy(5f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(loadingBack, getX(), getY());
        batch.draw(loadingBar, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
