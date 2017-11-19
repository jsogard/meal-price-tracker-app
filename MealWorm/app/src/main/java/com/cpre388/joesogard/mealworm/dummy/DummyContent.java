package com.cpre388.joesogard.mealworm.dummy;

import com.cpre388.joesogard.mealworm.FoodItem;
import com.cpre388.joesogard.mealworm.GroceryItem;
import com.cpre388.joesogard.mealworm.MealItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<FoodItem> ITEMS = new ArrayList<FoodItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<Long, FoodItem> ITEM_MAP = new HashMap<Long, FoodItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(FoodItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }

    private static FoodItem createDummyItem(int position) {
        Random r = new Random();
        if(r.nextBoolean())
            return new GroceryItem("Grocery Item " + position, r.nextFloat() + (float)r.nextInt(10));
        if(ITEMS.size() == 0) return new MealItem("Meal Item " + position);
        return new MealItem("Meal Item " + position, ITEMS.get(r.nextInt(ITEMS.size())));
    }
}