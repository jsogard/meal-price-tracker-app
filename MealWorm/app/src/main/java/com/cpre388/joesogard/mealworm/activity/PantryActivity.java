package com.cpre388.joesogard.mealworm.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;

import com.cpre388.joesogard.mealworm.fragments.MyPantryItemRecyclerViewAdapter;
import com.cpre388.joesogard.mealworm.fragments.PantryFilter;
import com.cpre388.joesogard.mealworm.fragments.PantryItemFragment;
import com.cpre388.joesogard.mealworm.R;
import com.cpre388.joesogard.mealworm.data.AppData;
import com.cpre388.joesogard.mealworm.models.FoodItem;
import com.cpre388.joesogard.mealworm.models.GroceryItem;
import com.cpre388.joesogard.mealworm.models.MealItem;

import org.json.JSONException;

import java.io.IOException;

public class PantryActivity extends AppCompatActivity
    implements PantryItemFragment.OnListFragmentInteractionListener {

    private static final String MEAL_FRAGMENT = "MEAL FRAGMENT";
    private static final String GROCERY_FRAGMENT = "GROCERY FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

//        AppData.populateDummyData();
//        readFakeData();
        readData();

        getSupportFragmentManager().beginTransaction()
                .add(
                        R.id.mealsFrame,
                        PantryItemFragment.newInstance(
                                new PantryFilter()
                                        .addFilterValue(MealItem.class, PantryFilter.FILTER_BY_CLASS)
                                        .addFilterValue(false, PantryFilter.FILTER_BY_DEPLETION),
                                PantryItemFragment.SORT_BY_LAST_USE, true),
                        MEAL_FRAGMENT
                )
                .add(
                        R.id.ingredientsFrame,
                        PantryItemFragment.newInstance(
                                new PantryFilter()
                                        .addFilterValue(GroceryItem.class, PantryFilter.FILTER_BY_CLASS)
                                        .addFilterValue(false, PantryFilter.FILTER_BY_DEPLETION),
                                PantryItemFragment.SORT_BY_LAST_USE, true),
                        GROCERY_FRAGMENT
                )
                .commit();
    }

    private void readFakeData(){
        try{
            AppData.readFakeItems(getApplicationContext().getAssets().open("fooditems.json"));
            AppData.readFakeUses(getApplicationContext().getAssets().open("fooduses.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readData(){
        try{
            AppData.readFoodItemData(openFileInput(AppData.FOOD_ITEM_FILE_NAME));
            AppData.readFoodUseData(openFileInput(AppData.FOOD_USE_FILE_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void goToIngredients(View v){
        setIngredientsVisibility(true);
        setMealsVisibility(false);
//        emptyFragments();

        colorSelectedButton(R.id.ingredientsButton);
        populateGroceryFragment(
                new PantryFilter()
                        .addFilterValue(GroceryItem.class, PantryFilter.FILTER_BY_CLASS)
                        .addFilterValue(false, PantryFilter.FILTER_BY_DEPLETION)
        );
    }

    public void goToMeal(View v){
        setIngredientsVisibility(false);
        setMealsVisibility(true);
//        emptyFragments();

        colorSelectedButton(R.id.mealsButton);
        populateMealFragment(
                new PantryFilter()
                        .addFilterValue(MealItem.class, PantryFilter.FILTER_BY_CLASS)
                        .addFilterValue(false, PantryFilter.FILTER_BY_DEPLETION)
        );
    }

    public void goToTrash(View v){
        setIngredientsVisibility(true);
        setMealsVisibility(true);
//        emptyFragments();

        colorSelectedButton(R.id.trashButton);
        populateMealFragment(
                new PantryFilter()
                        .addFilterValue(MealItem.class, PantryFilter.FILTER_BY_CLASS)
                        .addFilterValue(true, PantryFilter.FILTER_BY_DEPLETION)
        );
        populateGroceryFragment(
                new PantryFilter()
                        .addFilterValue(GroceryItem.class, PantryFilter.FILTER_BY_CLASS)
                        .addFilterValue(true, PantryFilter.FILTER_BY_DEPLETION)
        );
    }

    private void colorSelectedButton(int buttonId){
        ((ImageButton)findViewById(R.id.ingredientsButton)).setColorFilter(Color.argb(255, 16, 16, 16));
        ((ImageButton)findViewById(R.id.mealsButton)).setColorFilter(Color.argb(255, 16, 16, 16));
        ((ImageButton)findViewById(R.id.trashButton)).setColorFilter(Color.argb(255, 16, 16, 16));
        ((ImageButton)findViewById(buttonId)).setColorFilter(Color.argb(255, 255, 255, 255));
    }

    @Override
    public void onListFragmentInteraction(FoodItem item, MyPantryItemRecyclerViewAdapter.ViewHolder viewHolder) {
        Intent i = new Intent(this, AnalyzeFoodItem.class);
        i.putExtra(AnalyzeFoodItem.EXTRA_FOOD_ID, item.getId());
        startActivity(i);
    }

    public void goToNewItem(View v){
        startActivity(new Intent(this, NewFoodItem.class));
    }

    private void populateMealFragment(PantryFilter filter){
        getSupportFragmentManager().beginTransaction()
                .replace(
                        R.id.mealsFrame,
                        PantryItemFragment.newInstance(filter, PantryItemFragment.SORT_BY_LAST_USE, true)
                )
                .commit();
    }

    private void populateGroceryFragment(PantryFilter filter){
        getSupportFragmentManager().beginTransaction()
                .replace(
                        R.id.ingredientsFrame,
                        PantryItemFragment.newInstance(filter, PantryItemFragment.SORT_BY_LAST_USE, true)
                )
                .commit();
    }

    private void setIngredientsVisibility(boolean isVisible){
        findViewById(R.id.ingredientsFrame).setVisibility(isVisible ? View.VISIBLE : View.GONE);
        findViewById(R.id.IngredientsText).setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void setMealsVisibility(boolean isVisible){
        findViewById(R.id.mealsFrame).setVisibility(isVisible ? View.VISIBLE : View.GONE);
        findViewById(R.id.MealsText).setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void emptyFragments(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        for(Fragment frag : fm.getFragments()){
            trans.remove(frag);
        }
        trans.commit();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        goToIngredients(null);

//        PantryItemFragment mealFragment = PantryItemFragment.newInstance(
//                new PantryFilter().addFilterValue(MealItem.class, PantryFilter.FILTER_BY_CLASS),
//                PantryItemFragment.SORT_BY_LAST_USE, true);
//
//        PantryItemFragment groceryFragment = PantryItemFragment.newInstance(
//                new PantryFilter().addFilterValue(GroceryItem.class, PantryFilter.FILTER_BY_CLASS),
//                PantryItemFragment.SORT_BY_LAST_USE, true);
//
//        emptyFragments();
//
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.mealsFrame, mealFragment, MEAL_FRAGMENT)
//                .commit();
//
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.groceryLayout, groceryFragment, GROCERY_FRAGMENT)
//                .commit();


//        if(getSupportFragmentManager().getFragments().size() == 0){
//
//
//
//        }
//        else {
//            // necessary to refresh the items
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.mealLayout, mealFragment, MEAL_FRAGMENT)
//                    .commit();
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.groceryLayout, groceryFragment, GROCERY_FRAGMENT)
//                    .commit();
//        }
    }
}
