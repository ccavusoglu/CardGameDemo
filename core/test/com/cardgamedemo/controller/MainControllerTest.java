package com.cardgamedemo.controller;

import com.cardgamedemo.controller.MainController;
import com.cardgamedemo.utils.Enums;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by Çağatay Çavuşoğlu on 20.07.2016.
 */
public class MainControllerTest {
    MainController sut;

    @Before
    public void setUp() throws Exception {

    }

    // when deck button is clicked, clear active hand group which added to stage
    @Test
    @Ignore
    public void drawPlayerCards_IfDeckDrawing_ShouldClearGroup() throws Exception {
    }

    // when deck button is clicked, clear card actors list to prevent duplications
    @Test
    @Ignore
    public void drawPlayerCards_IfDeckDrawing_ShouldClearCardActorsList() throws Exception {
    }

    // when deck button is clicked, create card actors and add to card actors list
    @Test
    @Ignore
    public void drawPlayerCards_IfDeckDrawing_ShouldCreateCardActors() throws Exception {
    }

    // when deck button is clicked, create card actors and add them to stage
    @Test
    @Ignore
    public void drawPlayerCards_IfDeckDrawing_ShouldAddCardsToActiveStage() throws Exception {
    }

    // when creating game screen, create deck, image and card actors then add them to stage
    @Test
    @Ignore
    public void prepareGameScreen_WhenCreatingGameScreen_CreateBaseActorsAndAddThemToStage() throws Exception {
    }

    // when arranging cards positions based on sorting or smth. do not initiate new arrangement until previous one is completed
    @Test
    @Ignore
    public void reArrangeGroup_IfPreviousLayoutArrangementNotDone_DoNotInitiateNewLayoutArrangement() throws Exception {
    }

    // when swapping two actors group positions, if dragged one is focused, clear its focus
    @Test
    @Ignore
    public void swapActorPositions_IfDraggedActorIsFocussed_ClearItsFocus() throws Exception {
    }

    // when swapping two actors group positions, if target one is focused, clear its focus
    @Test
    @Ignore
    public void swapActorPositions_IfTargetActorIsFocussed_ClearItsFocus() throws Exception {
    }

    // when swapping two actors group positions, if any other card is focused, clear its focus
    @Test
    @Ignore
    public void swapActorPositions_IfAnyOtherActorIsFocussed_ClearItsFocus() throws Exception {
    }
}
