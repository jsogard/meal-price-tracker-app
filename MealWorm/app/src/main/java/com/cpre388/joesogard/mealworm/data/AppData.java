package com.cpre388.joesogard.mealworm.data;

import android.content.Context;

import com.cpre388.joesogard.mealworm.models.FoodItem;
import com.cpre388.joesogard.mealworm.models.FoodUse;
import com.cpre388.joesogard.mealworm.models.GroceryItem;
import com.cpre388.joesogard.mealworm.models.MealItem;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class AppData {

    public static final String FOOD_ITEM_FILE_NAME = "food_items_info.json";
    public static final String FOOD_USE_FILE_NAME = "food_use_info.json";

    /**
     * An array of sample (dummy) items.
     */
    public static final List<FoodItem> ITEMS = new ArrayList<FoodItem>();
    public static final List<FoodUse> USES = new ArrayList<FoodUse>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<Long, FoodItem> ITEM_MAP = new HashMap<Long, FoodItem>();

    private static final int COUNT = 7;

    static {
        // Add some sample items.
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createDummyItem(i));
//        }
    }

    public static void addItem(FoodItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }

    public static void addUse(FoodUse use){
        USES.add(use);
    }

    public static void readFoodItemData(FileInputStream fIn){
        try{
            String json = "";
            int c;
            while((c = fIn.read()) != -1){
                json += Character.toString((char)c);
            }
            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0 ; i < jsonArray.length(); i++){
                AppData.addItem(FoodItem.fromJSON(jsonArray.getJSONObject(i)));
            }
            for(FoodItem foodItem : AppData.ITEMS){
                if(foodItem instanceof MealItem)
                    ((MealItem)foodItem).populateIngredients();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void writeFoodItemData(FileOutputStream fOut){
        try{
            JSONArray json = new JSONArray();
            for(FoodItem item : AppData.ITEMS)
                json.put(item.toJSON());
            String jsonString = json.toString(3);
            fOut.write(jsonString.getBytes());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void readFoodUseData(FileInputStream fIn){
        try{
            String jsonString = "";
            int c;
            while((c = fIn.read()) != -1){
                jsonString += Character.toString((char)c);
            }
            JSONArray jsonArray = new JSONArray(jsonString);
            FoodUse foodUse;
            for(int i = 0 ; i < jsonArray.length(); i++){
                foodUse = FoodUse.fromJSON(jsonArray.getJSONObject(i));
                foodUse.getUsedBy().use(foodUse);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void writeFoodUseData(FileOutputStream fOut){
        try{
            JSONArray jsonArray = new JSONArray();
            for(FoodUse use : AppData.USES)
                jsonArray.put(use.toJSON());
            fOut.write(jsonArray.toString().getBytes());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static List<FoodItem> filterItems(long itemFilter[]){
        LinkedList<FoodItem> items = new LinkedList<>();
        for(long id : itemFilter){
            items.add(ITEM_MAP.get(new Long(id)));
        }
        return items;
    }

    public static List<FoodItem> filterItems(int classFilter){
        LinkedList<FoodItem> items = new LinkedList<>();
        for(FoodItem foodItem : ITEMS){
            if(foodItem instanceof MealItem)
            if(foodItem.getFoodTypeID() == classFilter) items.add(foodItem);
        }
        return items;
    }

    private static FoodItem createDummyItem(int position) {
        Random r = new Random();
        if(r.nextBoolean())
            return new GroceryItem("Grocery Item " + position, r.nextFloat() + (float)r.nextInt(10));
        if(ITEMS.size() == 0) return new MealItem("Meal Item " + position);
        return new MealItem("Meal Item " + position, ITEMS.get(r.nextInt(ITEMS.size())));
    }
}
