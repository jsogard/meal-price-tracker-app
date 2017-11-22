package com.cpre388.joesogard.mealworm.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Joe Sogard on 11/17/2017.
 */

public class FoodUse {

    private List<String> useLabels;
    private Calendar useDate;
    private FoodItem usedBy;

    public static final String USE_DATE_MILLIS = "USE_DATE";

    public FoodUse(FoodItem usedBy, String... useLabels){
        this.usedBy = usedBy;
        this.useLabels = new ArrayList<String>();
        for(String label : useLabels)
            this.useLabels.add(label);
        useDate = Calendar.getInstance();
    }

    public List<String> getLabels(){
        return useLabels;
    }

    public Calendar getDateOfUse(){
        return useDate;
    }

    public FoodItem getUsedBy(){ return usedBy; }

    public JSONObject toJSON() {
        try{
            JSONObject json = new JSONObject();
            json.put(USE_DATE_MILLIS, useDate.getTimeInMillis());
            return json;
        } catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}
