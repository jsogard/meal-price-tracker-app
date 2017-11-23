package com.cpre388.joesogard.mealworm.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cpre388.joesogard.mealworm.MyPantryItemRecyclerViewAdapter;
import com.cpre388.joesogard.mealworm.PantryItemFragment;
import com.cpre388.joesogard.mealworm.R;
import com.cpre388.joesogard.mealworm.data.AppData;
import com.cpre388.joesogard.mealworm.models.FoodItem;
import com.cpre388.joesogard.mealworm.models.FoodUse;
import com.cpre388.joesogard.mealworm.models.MealItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity
    implements PantryItemFragment.OnListFragmentInteractionListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        readFoodItemData();
        readFoodUseData();
    }

    private void readFoodItemData(){
        try{
            AppData.readFoodItemData(openFileInput(FOOD_ITEM_FILE_NAME));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void readFoodUseData(){
        try{
            AppData.readFoodUseData(openFileInput(FOOD_USE_FILE_NAME));
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
                    .add(R.id.frameHolder, new PantryItemFragment())
                    .commit();
        }
        else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameHolder, new PantryItemFragment())
                    .commit();
        }

        //writeFoodItemData();
    }
}
