package com.cpre388.joesogard.mealworm.models;

import com.cpre388.joesogard.mealworm.data.AppData;

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
    public static final String USED_BY_ID_LONG = "USED_BY_ID";

    public FoodUse(FoodItem usedBy, String... useLabels){
        this.usedBy = usedBy;
        this.useLabels = new ArrayList<String>();
        for(String label : useLabels)
            this.useLabels.add(label);
        useDate = Calendar.getInstance();

        AppData.addUse(this);
    }

    private FoodUse(JSONObject json){
        try{
//            usedBy = AppData.ITEM_MAP.getOrDefault(json.getLong(USED_BY_ID_LONG), null);
            useDate = Calendar.getInstance();
            useDate.setTimeInMillis(json.getLong(USE_DATE_MILLIS));
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void setUseDate(long millis){ useDate.setTimeInMillis(millis);}

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
            json.put(USED_BY_ID_LONG, usedBy.getId());
            return json;
        } catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public static FoodUse fromJSON(JSONObject jsonObject){
        return new FoodUse(jsonObject);
    }
}
