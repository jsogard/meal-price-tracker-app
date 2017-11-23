package com.cpre388.joesogard.mealworm.models;

import android.support.constraint.solver.Goal;

import com.cpre388.joesogard.mealworm.data.AppData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Joe Sogard on 10/3/2017.
 */

public abstract class FoodItem {

    private static long FoodItemID = 0;

    private long id;
    private String name;
    protected List<FoodUse> useHistory;
    private boolean isDepleted;
    protected Calendar genesis;

    public FoodItem(String name){
        this.name = name;
        useHistory = new LinkedList<>();
        id = FoodItemID++;
        genesis = Calendar.getInstance();
    }

    public void use(FoodUse foodUse){
        useHistory.add(foodUse);
    }

    public void use(FoodItem usedBy, String ...useLabels){
        FoodUse foodUse = new FoodUse(usedBy, useLabels);
        use(foodUse);
    }

    protected int getTimesUsedBy(FoodItem user){
        int useCount = 0;
        for(FoodUse use : useHistory){
            if(use.getUsedBy() == user) useCount++;
        }
        return useCount;
    }

    private List<FoodUse> getUsesByMe(){
        ArrayList<FoodUse> uses = new ArrayList<>();
        for(FoodUse u : useHistory)
            if(u.getUsedBy() == this) uses.add(u);
        return uses;
    }

    // ---- NORMAL GETTERS & SETTERS ---- //

    public abstract float getCostPerUse();

    public abstract String getQuickFacts();

    public long getId(){ return id; }

    public int getUseCount(){ return useHistory.size(); }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDepleted() {
        return isDepleted;
    }

    public void setDepleted(boolean depleted) {
        isDepleted = depleted;
    }

    public List<FoodUse> getUseHistory(){
        return useHistory;
    }

    public Calendar getGenesis(){ return genesis; }

    public String getGenesisString(){
        return new DateFormatSymbols().getShortMonths()[genesis.get(Calendar.MONTH)]
                + " "
                + genesis.get(Calendar.DAY_OF_MONTH);
    }

    // ---- DATA FILE READ/WRITE METHODS ---- //


    public static final String FOOD_ITEM_TYPE_STRING = "FOOD_ITEM_TYPE";
    public static final String FOOD_ITEM_ID_LONG = "FOOD_ITEM_ID";
    public static final String FOOD_ITEM_NAME_STRING = "FOOD_ITEM_NAME";
    public static final String FOOD_ITEM_IS_DEPLETED_BOOLEAN = "FOOD_ITEM_IS_DEPLETED";
    public static final String FOOD_ITEM_GENESIS_MILLIS = "FOOD_ITEM_GENESIS";

    /**
     * used by subclasses to populate common data easily
     * @param json jsonobject from file data
     */
    protected FoodItem(JSONObject json){
        try{
            this.id = json.getLong(FOOD_ITEM_ID_LONG);
            if(FoodItemID <= this.id) FoodItemID = this.id + 1;

            this.name = json.getString(FOOD_ITEM_NAME_STRING);
            this.genesis = Calendar.getInstance();
            genesis.setTimeInMillis(json.getLong(FOOD_ITEM_GENESIS_MILLIS));
            this.isDepleted = json.getBoolean(FOOD_ITEM_IS_DEPLETED_BOOLEAN);
            useHistory = new LinkedList<>();
        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        try{
            json.put(FOOD_ITEM_NAME_STRING, name);
            json.put(FOOD_ITEM_ID_LONG, id);
            json.put(FOOD_ITEM_IS_DEPLETED_BOOLEAN, isDepleted);
            json.put(FOOD_ITEM_GENESIS_MILLIS, genesis.getTimeInMillis());

            return json;
        } catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public static FoodItem fromJSON(JSONObject json){
        try{
            String type = (String)json.getString(FOOD_ITEM_TYPE_STRING);
            if(type.equals(GroceryItem.GROCERY_ITEM_TYPE))
                return GroceryItem.fromJSON(json);
            else if(type.equals(MealItem.MEAL_ITEM_TYPE))
                return MealItem.fromJSON(json);
        } catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}
