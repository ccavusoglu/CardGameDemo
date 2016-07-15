package com.cardgamedemo.view;

import com.badlogic.gdx.math.Vector3;

import java.util.List;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */
public interface IHandLayout {
    float getCardWidth();

    List<Vector3> prepareLayout(int handSize);
}
