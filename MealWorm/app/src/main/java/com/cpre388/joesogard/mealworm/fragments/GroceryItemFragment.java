package com.cpre388.joesogard.mealworm.fragments;

import android.os.Bundle;

import com.cpre388.joesogard.mealworm.models.GroceryItem;

/**
 * Created by Joe Sogard on 12/28/2017.
 */

public class GroceryItemFragment extends PantryItemFragment {

    public static PantryItemFragment newInstance(long filterItems[]) {
        PantryItemFragment fragment = PantryItemFragment.newInstance(filterItems);
        Bundle newArgs = fragment.getArguments();
        newArgs.putInt(EXTRA_CLASS_FILTER, GroceryItem.FoodTypeID);
        fragment.setArguments(newArgs);
        return fragment;
    }
}
