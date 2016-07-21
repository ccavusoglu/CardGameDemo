package com.cardgamedemo.controller;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.cardgamedemo.CardGame;
import com.cardgamedemo.CardGameDemo;
import com.cardgamedemo.entity.Card;
import com.cardgamedemo.entity.Hand;
import com.cardgamedemo.utils.SortHelper;
import com.cardgamedemo.view.IHandLayout;
import com.cardgamedemo.view.actor.CardActor;
import com.cardgamedemo.view.actor.DeckActor;
import com.cardgamedemo.view.actor.HandGroup;
import fakes.MainControllerFake;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

/**
 * Created by Çağatay Çavuşoğlu on 20.07.2016.
 */
public class MainControllerTest {
    MainController sut;
    private CardGameDemo cardGameDemo;
    private SortHelper   sortHelper;
    private IHandLayout  handLayout;

    @Before
    public void setUp() throws Exception {
        handLayout = mock(IHandLayout.class);
        cardGameDemo = mock(CardGameDemo.class);
        sortHelper = mock(SortHelper.class);
        sut = new MainControllerFake(handLayout, cardGameDemo, sortHelper);
    }

    // if an action in progress don't drag a card
    @Test
    public void drag_IfAnActionInProgress_DoNotDrag() throws Exception {
        // arrange
        CardActor cardActor = mock(CardActor.class);

        sut.setActionFlag(false);

        // act
        boolean res = sut.drag(cardActor, 1f, 1f);

        // assert
        Assert.assertTrue(!res);
    }

    // when deck button is clicked, clear card actors list to prevent duplications
    @Test
    public void drawPlayerCards_IfDeckDrawing_ShouldClearCardActorsList() throws Exception {
        // arrange
        ArrayList<Card> list = new ArrayList<Card>();
        ArrayList<CardActor> listV = new ArrayList<CardActor>();
        list.add(new Card());
        CardActor cardActor = mock(CardActor.class);
        CardGame cardGame = mock(CardGame.class);
        DeckActor deckActor = mock(DeckActor.class);
        listV.add(cardActor);

        sut = spy(sut);
        sut.setCardGameDemo(cardGameDemo);
        sut.setCardActors(listV);

        doNothing().when(sut).createCardActors();

        when(cardGame.draw()).thenReturn(list);
        when(cardGameDemo.getGame()).thenReturn(cardGame);
        HandGroup group = new HandGroup();
        group.addActor(cardActor);

        sut.setHandGroup(group);
        sut.setDeckActor(deckActor);

        // act
        sut.drawPlayerCards();

        // assert
        Assert.assertTrue(listV.size() == 0);
    }

    // when deck button is clicked, clear active hand group which added to stage
    @Test
    public void drawPlayerCards_IfDeckDrawing_ShouldClearGroup() throws Exception {
        // arrange
        ArrayList<Card> list = new ArrayList<Card>();
        ArrayList<Vector3> listV = new ArrayList<Vector3>();
        list.add(new Card());
        listV.add(new Vector3());
        CardActor cardActor = mock(CardActor.class);
        CardGame cardGame = mock(CardGame.class);
        DeckActor deckActor = mock(DeckActor.class);

        when(cardGame.draw()).thenReturn(list);
        when(cardGameDemo.getGame()).thenReturn(cardGame);
        HandGroup group = new HandGroup();
        group.addActor(cardActor);

        sut.setHandGroup(group);
        sut.setDeckActor(deckActor);
        sut.setLayoutPositions(listV);

        // act
        sut.drawPlayerCards();

        // assert
        Assert.assertTrue(!group.hasChildren());
    }

    // when arranging cards positions based on sorting or smth. do not initiate new arrangement until previous one is completed
    @Test
    public void reArrangeGroup_IfPreviousLayoutArrangementNotDone_DoNotInitiateNewLayoutArrangement() throws Exception {
        // arrange
        sut.setArranged(true);

        // act
        boolean res = sut.reArrangeGroup();

        // assert
        Assert.assertTrue(!res);
    }

    // when swapping two actors group positions, if any other card is focused, clear its focus
    @Test
    public void swapActorPositions_IfAnyOtherActorIsFocussed_ClearItsFocus() throws Exception {
        // arrange
        CardActor cardActor = mock(CardActor.class);
        CardActor cardActor1 = mock(CardActor.class);
        CardActor cardActor2 = mock(CardActor.class);
        HandGroup handGroup = mock(HandGroup.class);
        ArrayList<CardActor> list = new ArrayList<CardActor>();
        list.add(cardActor);
        list.add(cardActor1);

        when(handGroup.swapActor(anyInt(), anyInt())).thenReturn(true);
        when(cardActor2.getFocussed()).thenReturn(true);

        sut = spy(sut);
        sut.setHandGroup(handGroup);
        sut.setCardActors(list);
        sut.setFocusedCard(cardActor2);

        // act
        sut.swapActorPositions(cardActor, cardActor1);

        // assert
        verify(sut).focusOff(cardActor2);
    }

    // when swapping two actors group positions, if dragged one is focused, clear its focus
    @Test
    public void swapActorPositions_IfDraggedActorIsFocussed_ClearItsFocus() throws Exception {
        // arrange
        CardActor cardActor = mock(CardActor.class);
        CardActor cardActor1 = mock(CardActor.class);
        HandGroup handGroup = mock(HandGroup.class);
        ArrayList<CardActor> list = new ArrayList<CardActor>();
        list.add(cardActor);
        list.add(cardActor1);

        when(handGroup.swapActor(anyInt(), anyInt())).thenReturn(true);
        when(cardActor.getFocussed()).thenReturn(true);

        sut = spy(sut);
        sut.setHandGroup(handGroup);
        sut.setCardActors(list);

        // act
        sut.swapActorPositions(cardActor, cardActor1);

        // assert
        verify(sut).clearFocus(cardActor);
    }

    // when swapping two actors group positions, if target one is focused, clear its focus
    @Test
    public void swapActorPositions_IfTargetActorIsFocussed_ClearItsFocus() throws Exception {
        // arrange
        CardActor cardActor = mock(CardActor.class);
        CardActor cardActor1 = mock(CardActor.class);
        HandGroup handGroup = mock(HandGroup.class);
        ArrayList<CardActor> list = new ArrayList<CardActor>();
        list.add(cardActor);
        list.add(cardActor1);

        when(handGroup.swapActor(anyInt(), anyInt())).thenReturn(true);
        when(cardActor1.getFocussed()).thenReturn(true);

        sut = spy(sut);
        sut.setHandGroup(handGroup);
        sut.setCardActors(list);

        // act
        sut.swapActorPositions(cardActor, cardActor1);

        // assert
        verify(sut).clearFocus(cardActor1);
    }
}
