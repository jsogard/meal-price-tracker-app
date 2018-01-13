package com.cpre388.joesogard.mealworm.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cpre388.joesogard.mealworm.fragments.MyPantryItemRecyclerViewAdapter;
import com.cpre388.joesogard.mealworm.fragments.PantryFilter;
import com.cpre388.joesogard.mealworm.fragments.PantryItemFragment;
import com.cpre388.joesogard.mealworm.R;
import com.cpre388.joesogard.mealworm.data.AppData;
import com.cpre388.joesogard.mealworm.models.FoodItem;
import com.cpre388.joesogard.mealworm.models.GroceryItem;
import com.cpre388.joesogard.mealworm.models.MealItem;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class PantryActivity extends AppCompatActivity
    implements PantryItemFragment.OnListFragmentInteractionListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

//        AppData.populateDummyData();
//        readFakeData();
        readData();
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

    @Override
    public void onListFragmentInteraction(FoodItem item, MyPantryItemRecyclerViewAdapter.ViewHolder viewHolder) {
        Intent i = new Intent(this, AnalyzeFoodItem.class);
        i.putExtra(AnalyzeFoodItem.EXTRA_FOOD_ID, item.getId());
        startActivity(i);
    }

    public void goToNewItem(View v){
        startActivity(new Intent(this, NewFoodItem.class));
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        PantryItemFragment mealFragment = PantryItemFragment.newInstance(
                new PantryFilter().addFilterValue(MealItem.class, PantryFilter.FILTER_BY_CLASS),
                PantryItemFragment.SORT_BY_LAST_USE, true);

        PantryItemFragment groceryFragment = PantryItemFragment.newInstance(
                new PantryFilter().addFilterValue(GroceryItem.class, PantryFilter.FILTER_BY_CLASS),
                PantryItemFragment.SORT_BY_LAST_USE, true);

        if(getSupportFragmentManager().getFragments().size() == 0){

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.mealLayout, mealFragment)
                    .commit();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.groceryLayout, groceryFragment)
                    .commit();

        }
        else {
            // necessary to refresh the items
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mealLayout, mealFragment)
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.groceryLayout, groceryFragment)
                    .commit();
        }
    }
}
