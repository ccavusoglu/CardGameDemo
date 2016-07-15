package com.cardgamedemo.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Çağatay Çavuşoğlu on 14.07.2016.
 */
public class HandLayout implements IHandLayout {
    private List<Vector3> layoutPositions; // z = Card's tilt angle
    private int           handSize;
    private float         cardWidthApprox;

    public HandLayout() {
        layoutPositions = new ArrayList<Vector3>();
    }

    @Override
    public float getCardWidth() {
        return cardWidthApprox * 2;
    }

    @Override
    public List<Vector3> prepareLayout(int handSize) {
        this.handSize = handSize;
        return generatePositions();
    }

    public Vector3 getLayoutPosition(int pos) {
        return layoutPositions.get(pos);
    }

    private List<Vector3> generatePositions() {
        float arcAngle = 60; // circle arc angle; 60
        float radius = 800; // TODO: should be set dynamically according to screen witdh

        float chordLength = radius; // 60-60-60 triangle
        float arcHeight = (float) (radius * (1 - Math.cos(Math.toRadians(arcAngle) / 2)));
        float arcLength = (float) (radius * arcAngle);

        cardWidthApprox = chordLength / 12;

        float segmentAngle = arcAngle / 10; // 12 segment for 11 cards

        float sidePadding = (Gdx.graphics.getWidth() - chordLength) / 2;
        float bottomPadding = sidePadding / 5;
        float angle = 25; // TODO: calc angle

        float originX = sidePadding + chordLength / 2;

        for (int i = 0; i < handSize + 2; i++) { // +2 is the last card's end point
            int k = i;
            //            if (i < 6) k = i;
            //            if (i > 5) k = 10 - i;

            // calc a point on the ARC according to angle
            float x = (float) (originX + radius * Math.cos(Math.toRadians(arcAngle * 2) - k * Math.toRadians(segmentAngle))) - cardWidthApprox;
            float y = (float) (bottomPadding + radius * Math.sin(Math.toRadians(arcAngle * 2) - k * Math.toRadians(segmentAngle))) - radius + arcHeight;

//            float x1 = (float) (radius + radius * Math.cos(Math.toRadians(arcAngle * 2) - (k + 2) * Math.toRadians(segmentAngle)));
//            float y1 = (float) (bottomPadding + radius * Math.sin(Math.toRadians(arcAngle * 2) - (k + 2) * Math.toRadians(segmentAngle))) - radius + arcHeight;

            //            float angle = (float) Math.toDegrees(Math.atan2(y1 - y, x1 - x));

            layoutPositions.add(new Vector3(x, y, angle));
            angle -= 5;
        }

        return layoutPositions;
    }
}
