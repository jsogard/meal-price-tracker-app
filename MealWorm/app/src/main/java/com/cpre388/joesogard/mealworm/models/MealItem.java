package com.cpre388.joesogard.mealworm.models;

import com.cpre388.joesogard.mealworm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Joe Sogard on 9/7/2017.
 */

public class MealItem extends FoodItem{

    private FoodItem[] ingredients;
    public static final int FoodTypeID = 0;
    protected static Map<Long, MealItem> ItemMap = new HashMap<>();

    public MealItem(String name){
        super(name);
        ingredients = new FoodItem[]{};
    }

    public MealItem(String name, FoodItem... ingredients){
        this(name);
        this.ingredients = ingredients;
    }

    public FoodItem[] getIngredients(){
        return ingredients;
    }

    @Override
    public void updateImgResourceID() {
        if(isDepleted())
            smallImgResourceID = R.mipmap.meal_small_red;
        else
            smallImgResourceID = R.mipmap.meal_small;
        bigImgResourceID = R.mipmap.meal_background;
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
    public String getLongFacts() {
        String facts = "Made on " + getGenesisString();
        int len = ingredients.length;
        if(len > 0)
            facts += String.format(" from %s", ingredients[0]);

        if(len > 1){
            for(int i = 1; i < len-1; i++){
                facts += String.format(", %s", ingredients[i]);
            }
            if(len != 2) facts += ",";
            facts += String.format(" and %s", ingredients[len-1]);
        }

        return facts;
    }

    // ---- MEAL ITEM STATIC DATA OPERATIONS ---- //

    protected static List<MealItem> getItems(){
        return new ArrayList<>(ItemMap.values());
    }

    protected static void addItem(MealItem item){
        ItemMap.put(item.getId(), item);
    }

    protected static List<MealItem> filterItems(long[] ids){
        if(ids == null) return new ArrayList<>(ItemMap.values());
        List<MealItem> items = new ArrayList<>(ids.length);
        MealItem mealItem;
        for(long id : ids){
            mealItem = ItemMap.get(id);
            if(mealItem != null)
                items.add(ItemMap.get(id));
        }
        return items;
    }

    // ---- DATA FILE READ/WRITE METHODS ---- //

    private long[] queuedIngredientIDs = null;
    public static final String MEAL_ITEM_INGREDIENTS_IDS_ARRAY = "MEAL_ITEM_INGREDIENTS_IDS";
    public static final String MEAL_ITEM_TYPE = "MEAL";

    private MealItem(JSONObject json) {
        super(json);
        try{
            JSONArray ingr = json.getJSONArray(MEAL_ITEM_INGREDIENTS_IDS_ARRAY);
            queuedIngredientIDs = new long[ingr.length()];
            for(int i = 0; i < ingr.length(); i++){
                queuedIngredientIDs[i] = ingr.getLong(i);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    public static MealItem fromJSON(JSONObject json){
        return new MealItem(json);
    }

    /**
     * WARNING: only to be called once all items have been added to AppData.ITEM_MAP
     */
    public void populateIngredients(){
        // TODO: 12/30/2017 enter in ingr ids as parameter to this
        if(queuedIngredientIDs == null) return;

        ingredients = new FoodItem[queuedIngredientIDs.length];
        for(int i = 0; i < ingredients.length; i++){
            ingredients[i] = FoodItem.getItem(queuedIngredientIDs[i]);
        }
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
}
