package com.cpre388.joesogard.mealworm.fragments;

import com.cpre388.joesogard.mealworm.models.FoodItem;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Joe Sogard on 1/2/2018.
 */

public class PantryFilter {

    public class FilterBy<T extends Object>{

        public Class filterType;
        public T filterValue = null;
        public FilterBy(Class filterType){
            this.filterType = filterType;
        }
        public final void filter(){
            if(filterValue == null) return;

            List<FoodItem> newItems = new Stack<>();
            for(FoodItem food : items)
                if(checkAdd(food)) newItems.add(food);

            items = newItems;
        };
        protected boolean checkAdd(FoodItem foodItem){
            throw new RuntimeException(String.format("%s.checkAdd must be implemented", this.getClass().getName()));
        };
    }

    private Map<String, FilterBy> filterByMap;
    private List<FoodItem> items;

    public static final String FILTER_BY_ID             = "FILTER_BY_ID";
    public static final String FILTER_BY_CLASS          = "FILTER_BY_CLASS";
    public static final String FILTER_BY_TIME_AFTER     = "FILTER_BY_TIME_AFTER";
    public static final String FILTER_BY_TIME_BEFORE    = "FILTER_BY_TIME_BEFORE";
    public static final String FILTER_BY_DEPLETION      = "FILTER_BY_DEPLETION";

    public PantryFilter() {
        filterByMap = new HashMap<>();
        filterByMap.put(
                FILTER_BY_ID,
                new FilterBy<long[]>(long[].class) {

                    @Override
                    protected boolean checkAdd(FoodItem foodItem) {
                        for(long id : filterValue)
                            if(foodItem.getId() == id)
                                return true;
                        return false;
                        // i know, O(n2) idec it's late
                    }
                }
        );
        filterByMap.put(
                FILTER_BY_CLASS,
                new FilterBy<Class<? extends FoodItem>>(Class.class) {
                    @Override
                    protected boolean checkAdd(FoodItem foodItem) {
                        return foodItem.getClass().equals(filterValue);
                    }
                }
        );
        filterByMap.put(
                FILTER_BY_TIME_AFTER,
                new FilterBy<Calendar>(Calendar.class) {
                    @Override
                    protected boolean checkAdd(FoodItem foodItem) {
                        return foodItem.getGenesis().compareTo(filterValue) >= 0;
                    }
                }
        );
        filterByMap.put(
                FILTER_BY_TIME_BEFORE,
                new FilterBy<Calendar>(Calendar.class) {
                    @Override
                    protected boolean checkAdd(FoodItem foodItem) {
                        return foodItem.getGenesis().compareTo(filterValue) <= 0;
                    }
                }
        );
        filterByMap.put(
                FILTER_BY_DEPLETION,
                new FilterBy<Boolean>(Boolean.class) {
                    @Override
                    protected boolean checkAdd(FoodItem foodItem) {
                        return foodItem.isDepleted() == filterValue.booleanValue();
                    }
                }
        );
    }

    public <T extends Object> PantryFilter addFilterValue(T objValue, String filterKey){
        FilterBy filter = filterByMap.get(filterKey);
        if(filter == null)
            return this;
        if(!filter.filterType.equals(objValue.getClass()))
            throw new IllegalArgumentException(String.format(
                    "Object is type <%s> but must be of type <%s>",
                    objValue.getClass().getSimpleName(),
                    filter.filterType.getSimpleName()
            ));
        filter.filterValue = objValue;
        return this;
    }

    public List<FoodItem> filter(List<FoodItem> foodItemList) {

        items = foodItemList;
        for(FilterBy filter : filterByMap.values()){
            filter.filter();
        }
        return items;
    }

}
