package com.cpre388.joesogard.mealworm;

import java.util.Calendar;

/**
 * Created by Joe Sogard on 9/7/2017.
 */

public class MealItem extends FoodItem{

    private FoodItem[] ingredients;

    public MealItem(String name, FoodItem... ingredients){
        super(name);
        this.ingredients = ingredients;
        // TODO ensure not cyclical
    }

    public MealItem(String name){
        super(name);
        ingredients = new FoodItem[]{};
    }

    public FoodItem[] getIngredients(){
        return ingredients;
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
        if(useCount == 0) return -1;
        float costPerUse = 0;
        for(FoodItem f : ingredients)
            costPerUse += f.getCostPerUse() * useCount;
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
