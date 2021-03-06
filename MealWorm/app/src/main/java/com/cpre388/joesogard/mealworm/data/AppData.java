package com.cpre388.joesogard.mealworm.data;

import android.content.Context;

import com.cpre388.joesogard.mealworm.fragments.PantryFilter;
import com.cpre388.joesogard.mealworm.models.FoodItem;
import com.cpre388.joesogard.mealworm.models.FoodUse;
import com.cpre388.joesogard.mealworm.models.GroceryItem;
import com.cpre388.joesogard.mealworm.models.MealItem;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
 */
public class AppData {

    public static final String FOOD_ITEM_FILE_NAME = "food_items_info.json";
    public static final String FOOD_USE_FILE_NAME = "food_use_info.json";


    public static void populateDummyData(){
        GroceryItem[] ingrs = new GroceryItem[]{
                new GroceryItem("Peanut Butter", 0.88f),
                new GroceryItem("Jelly", 3.48f),
                new GroceryItem("Bread", 0.56f),
                new GroceryItem("Ham", 2.97f),
                new GroceryItem("Cheese", 4.78f)
        };

        for(GroceryItem g : ingrs){
            FoodItem.addItem(g);
        }

        MealItem[] meals = new MealItem[6];
        meals[0] = new MealItem("Toast", ingrs[2]);
        meals[1] = new MealItem("PB&J", ingrs[0], ingrs[1], ingrs[2]);
        meals[2] = new MealItem("PB Toast", meals[0], ingrs[0]);
        meals[3] = new MealItem("Ham Sammy", ingrs[2], ingrs[3], ingrs[4]);
        meals[4] = new MealItem("Grilled Cheeser", ingrs[2], ingrs[4]);
        meals[5] = new MealItem("Awfulness Sandwich", meals[1], meals[3], meals[4]);

        for(MealItem m : meals){
            FoodItem.addItem(m);
        }
    }

    public static void readFakeItems(InputStream is){

        String contents = "";
        int b;
        try {
            while((b = is.read()) != -1){
                contents += (char)b;
            }
            stringToItems(contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFakeUses(InputStream is){

        String contents = "";
        int b;
        try {
            while((b = is.read()) != -1){
                contents += (char)b;
            }
            stringToUses(contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void stringToItems(String jsonString){
        try{
            JSONArray jsonArray = new JSONArray(jsonString);
            for(int i = 0; i < jsonArray.length(); i++){
                FoodItem.addItem(FoodItem.fromJSON(jsonArray.getJSONObject(i)));
            }
            PantryFilter filter = new PantryFilter();
            filter.addFilterValue(MealItem.class, PantryFilter.FILTER_BY_CLASS);
            List<FoodItem> items = filter.filter(FoodItem.getItemList());
            for(FoodItem meal : items){
                ((MealItem)meal).populateIngredients();
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    private static void stringToUses(String jsonString){
        try{
            JSONArray jsonArray = new JSONArray(jsonString);
            FoodUse foodUse;
            for(int i = 0; i < jsonArray.length(); i++){
                foodUse = FoodUse.fromJSON(jsonArray.getJSONObject(i));
                FoodUse.addUse(foodUse);
                foodUse.getUsedBy().use(foodUse);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
    }
    
    public static void readFoodItemData(FileInputStream fIn){

        try{
            String json = "";
            int c;
            while((c = fIn.read()) != -1){
                json += Character.toString((char)c);
            }
            stringToItems(json);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void writeFoodItemData(FileOutputStream fOut) throws IOException {
        JSONArray json = new JSONArray();
        for(FoodItem item : FoodItem.getItemList())
            json.put(item.toJSON());
        String jsonString = json.toString();
        fOut.write(jsonString.getBytes());
    }

    /* warning: food use data must be read after food item data */
    public static void readFoodUseData(FileInputStream fIn) throws IOException, JSONException {
        String jsonString = "";
        int c;
        while((c = fIn.read()) != -1){
            jsonString += Character.toString((char)c);
        }
        stringToUses(jsonString);
    }

    public static void writeFoodUseData(FileOutputStream fOut) throws IOException {
        JSONArray jsonArray = new JSONArray();
        for(FoodUse use : FoodUse.getUseHistory())
            jsonArray.put(use.toJSON());
        fOut.write(jsonArray.toString().getBytes());
    }




}
