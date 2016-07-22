package fakes;

import com.cardgamedemo.CardGameDemo;
import com.cardgamedemo.controller.MainController;
import com.cardgamedemo.utils.SortHelper;
import com.cardgamedemo.view.IHandLayout;

/**
 * Created by Çağatay Çavuşoğlu on 21.07.2016.
 */
public class MainControllerFake extends MainController {
    public MainControllerFake(IHandLayout handLayout, CardGameDemo cardGameDemo, SortHelper sortHelper) {
        super(handLayout, cardGameDemo, sortHelper);
    }

    @Override
    protected void createCardActors() {
        return;
    }
}
