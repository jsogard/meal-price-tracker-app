package com.cpre388.joesogard.mealworm.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cpre388.joesogard.mealworm.fragments.GroceryItemFragment;
import com.cpre388.joesogard.mealworm.fragments.MealItemFragment;
import com.cpre388.joesogard.mealworm.fragments.MyPantryItemRecyclerViewAdapter;
import com.cpre388.joesogard.mealworm.fragments.PantryItemFragment;
import com.cpre388.joesogard.mealworm.R;
import com.cpre388.joesogard.mealworm.data.AppData;
import com.cpre388.joesogard.mealworm.models.FoodItem;

import java.io.FileNotFoundException;
import java.io.IOException;

public class PantryActivity extends AppCompatActivity
    implements PantryItemFragment.OnListFragmentInteractionListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

//        AppData.populateDummyData();
        readFoodItemData();
//        readFoodUseData();
    }

    private void readFoodItemData(){
        try{
//            AppData.readFoodItemData(openFileInput(AppData.FOOD_ITEM_FILE_NAME));
            AppData.readFakeData(getApplicationContext().getAssets().open("fooditems.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFoodUseData(){
        try{
            AppData.readFoodUseData(openFileInput(AppData.FOOD_USE_FILE_NAME));
        } catch (FileNotFoundException e) {
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

        if(getSupportFragmentManager().getFragments().size() == 0){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.mealLayout, MealItemFragment.newInstance(null))
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.groceryLayout, GroceryItemFragment.newInstance(null))
                    .commit();

        }
        else {
            // necessary to refresh the items
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mealLayout, MealItemFragment.newInstance(null))
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.groceryLayout, GroceryItemFragment.newInstance(null))
                    .commit();
        }
    }
}
