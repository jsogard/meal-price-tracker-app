package com.cpre388.joesogard.mealworm.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Joe Sogard on 9/7/2017.
 */

public class MealItem extends FoodItem{

    private FoodItem[] ingredients;
    protected FoodItemType type = FoodItemType.MEAL;

    public static final String MEAL_ITEM_INGREDIENTS_IDS_ARRAY = "MEAL_ITEM_INGREDIENTS_IDS";
    public static final String MEAL_ITEM_TYPE = "MEAL";

    public MealItem(String name, FoodItem... ingredients){
        super(name);
        this.ingredients = ingredients;
    }

    public MealItem(String name){
        super(name);
        ingredients = new FoodItem[]{};
    }

    public FoodItem[] getIngredients(){
        return ingredients;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        try{
            json.put(FOOD_ITEM_TYPE_STRING, MEAL_ITEM_TYPE);
            JSONArray ingrIDs = new JSONArray();
            for(FoodItem ingr : ingredients)
                ingrIDs.put(ingr.getId());
            json.put(MEAL_ITEM_INGREDIENTS_IDS_ARRAY, ingrIDs);
            return json;
        } catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void use(FoodUse foodUse) {
        super.use(foodUse);
        for(FoodItem f : ingredients)
            f.use(foodUse);
    }



    @Override
    public float getCostPerUse(){
        float useCount = (float) useHistory.size();
        if(useCount == 0) return 0;

        float costPerUse = 0;

        for(FoodItem ingr : ingredients){
            costPerUse += (float)ingr.getTimesUsedBy(this) * ingr.getCostPerUse() / useCount;
        }

        return costPerUse;
    }

    @Override
    public String getQuickFacts() {
        String qFact = "Made on " + getGenesisString();
        if(ingredients.length > 0){
            qFact += " from ";
            boolean first = true;
            for(FoodItem ingr : ingredients){
                if(!first) qFact += ", ";
                first = false;
                qFact += ingr.getName();
            }
        }
        if(qFact.length() > 45)
            qFact = qFact.substring(0, 45) + "...";
        return qFact;
    }
}
