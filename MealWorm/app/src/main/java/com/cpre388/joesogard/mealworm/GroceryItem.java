package com.cpre388.joesogard.mealworm;

import java.lang.reflect.GenericArrayType;
import java.util.Calendar;

/**
 * Created by Joe Sogard on 9/6/2017.
 */

public class GroceryItem extends FoodItem {

    private float price;

    public GroceryItem(String name, float price) {
        super(name);
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    @Override
    public float getCostPerUse() {
        if(useHistory.size() == 0) return -1;
        return price / (float)useHistory.size();
    }

    @Override
    public String getQuickFacts() {
        return String.format("Bought on %s for $%03.2f", getGenesisString(), price);
    }
}
