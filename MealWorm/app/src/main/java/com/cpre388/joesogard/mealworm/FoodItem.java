package com.cpre388.joesogard.mealworm;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Joe Sogard on 10/3/2017.
 */

public abstract class FoodItem {

    public enum FoodItemType {
        GROCERY,
        MEAL
    }

    protected FoodItemType type;

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
    public Calendar getGenesis(){ return genesis; }

    public String getGenesisString(){
        return new DateFormatSymbols().getShortMonths()[genesis.get(Calendar.MONTH)]
                + " "
                + genesis.get(Calendar.DAY_OF_MONTH);
    }

    public void use(String ...useLabels){
        FoodUse foodUse = new FoodUse(useLabels);
        use(foodUse);
    }

    public FoodItemType getType(){ return type; }

    protected void use(FoodUse foodUse){
        useHistory.add(foodUse);
    }

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
}
