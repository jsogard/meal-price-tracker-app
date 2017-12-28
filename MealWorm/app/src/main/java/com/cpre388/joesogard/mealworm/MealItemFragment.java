package com.cpre388.joesogard.mealworm;

import android.os.Bundle;

import com.cpre388.joesogard.mealworm.models.MealItem;

/**
 * Created by Joe Sogard on 12/18/2017.
 */

public class MealItemFragment extends PantryItemFragment {

    public static MealItemFragment newInstance(long filterItems[]) {
        MealItemFragment fragment = (MealItemFragment) PantryItemFragment.newInstance(filterItems);
        Bundle newArgs = fragment.getArguments();
        newArgs.putInt(EXTRA_CLASS_FILTER, MealItem.FoodTypeID);
        fragment.setArguments(newArgs);
        return fragment;
    }
}
