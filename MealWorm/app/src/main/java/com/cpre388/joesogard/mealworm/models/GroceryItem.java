package com.cpre388.joesogard.mealworm.models;

import com.cpre388.joesogard.mealworm.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Joe Sogard on 9/6/2017.
 */

public class GroceryItem extends FoodItem {

    private float price;
    protected static Map<Long, GroceryItem> ItemMap = new HashMap<>();

    public GroceryItem(String name, float price) {
        super(name);
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    @Override
    public void updateImgResourceID() {
        if(isDepleted())
            smallImgResourceID = R.mipmap.grocery_small_red;
        else
            smallImgResourceID = R.mipmap.grocery_small;
        bigImgResourceID = R.mipmap.grocery_background;
    }

    @Override
    public float getCostPerUse() {
        if(useHistory.size() == 0) return 0;
        return price / (float)useHistory.size();
    }

    @Override
    public String getLongFacts() {
        return String.format("Bought on %s for $%03.2f", getGenesisString(), price);
    }

    // ---- GROCERY ITEM STATIC DATA OPERATIONS ---- //

    protected static List<GroceryItem> getItems(){
        return new ArrayList<>(ItemMap.values());
    }

    protected static void addItem(GroceryItem item){
        ItemMap.put(item.getId(), item);
    }

    protected static List<GroceryItem> filterItems(long[] ids){
        if(ids == null) return new ArrayList<>(ItemMap.values());
        List<GroceryItem> items = new ArrayList<>(ids.length);
        GroceryItem groceryItem;
        for(long id : ids){
            groceryItem = ItemMap.get(id);
            if(groceryItem != null)
                items.add(groceryItem);
        }
        return items;
    }


    // ---- METHODS USED FOR READ/WRITING DATA FILE ----- //

    public static final String GROCERY_ITEM_PRICE_FLOAT = "GROCERY_ITEM_PRICE";
    public static final String GROCERY_ITEM_TYPE = "GROCERY";

    private GroceryItem(JSONObject json){
        super(json);
        try{
            this.price = (float)json.getDouble(GROCERY_ITEM_PRICE_FLOAT);
        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    public static GroceryItem fromJSON(JSONObject json){
        return new GroceryItem(json);
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


}
