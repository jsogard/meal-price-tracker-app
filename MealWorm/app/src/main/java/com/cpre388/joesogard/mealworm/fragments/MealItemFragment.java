package com.cpre388.joesogard.mealworm.fragments;

import android.os.Bundle;

import com.cpre388.joesogard.mealworm.models.MealItem;

/**
 * Created by Joe Sogard on 12/18/2017.
 */

public class MealItemFragment extends PantryItemFragment {

    public static PantryItemFragment newInstance(long filterItems[]) {
        PantryItemFragment fragment = PantryItemFragment.newInstance(filterItems);
        Bundle newArgs = fragment.getArguments();
        newArgs.putInt(EXTRA_CLASS_FILTER, MealItem.FoodTypeID);
        fragment.setArguments(newArgs);
        return fragment;
    }
}
