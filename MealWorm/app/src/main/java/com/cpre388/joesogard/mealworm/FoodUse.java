package com.cpre388.joesogard.mealworm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Joe Sogard on 11/17/2017.
 */

public class FoodUse {

    private List<String> useLabels;
    private Calendar useDate;

    public FoodUse(String... useLabels){
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
}
