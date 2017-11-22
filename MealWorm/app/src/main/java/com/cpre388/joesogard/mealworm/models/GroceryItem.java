package com.cpre388.joesogard.mealworm.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Joe Sogard on 9/6/2017.
 */

public class GroceryItem extends FoodItem {

    private float price;
    protected FoodItemType type = FoodItemType.GROCERY;

    public static final String GROCERY_ITEM_PRICE_FLOAT = "GROCERY_ITEM_PRICE";
    public static final String GROCERY_ITEM_TYPE = "GROCERY";

    public GroceryItem(String name, float price) {
        super(name);
        this.price = price;
    }

    private GroceryItem(String name, float price, )

    public float getPrice() {
        return price;
    }

    @Override
    public float getCostPerUse() {
        if(useHistory.size() == 0) return 0;
        return price / (float)useHistory.size();
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        try{
            json.put(FOOD_ITEM_TYPE_STRING, GROCERY_ITEM_TYPE);
            json.put(GROCERY_ITEM_PRICE_FLOAT, price);
            return json;
        } catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public static GroceryItem fromJSON(JSONObject json){
        try{
            String name = json.getString(FoodItem.FOOD_ITEM_NAME_STRING);
            float price = 0;
            GroceryItem groceryItem = new GroceryItem();
        }

    }

    @Override
    public String getQuickFacts() {
        return String.format("Bought on %s for $%03.2f", getGenesisString(), price);
    }
}
