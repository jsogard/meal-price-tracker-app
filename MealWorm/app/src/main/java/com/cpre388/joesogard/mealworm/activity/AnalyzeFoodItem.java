package com.cpre388.joesogard.mealworm.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cpre388.joesogard.mealworm.fragments.MyPantryItemRecyclerViewAdapter;
import com.cpre388.joesogard.mealworm.fragments.PantryFilter;
import com.cpre388.joesogard.mealworm.fragments.PantryItemFragment;
import com.cpre388.joesogard.mealworm.R;
import com.cpre388.joesogard.mealworm.data.AppData;
import com.cpre388.joesogard.mealworm.models.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import static android.view.View.GONE;

public class AnalyzeFoodItem extends AppCompatActivity
        implements PantryItemFragment.OnListFragmentInteractionListener {

    public static final String EXTRA_FOOD_ID = "EXTRA_FOOD_ID";

    private FoodItem foodItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long foodId = getIntent().getLongExtra(EXTRA_FOOD_ID, -1);
        foodItem = FoodItem.getItem(foodId);

        setContentView(R.layout.activity_analyze_food);

        ((ImageView)findViewById(R.id.backgroundImage)).setImageResource(foodItem.getBigImgResourceID());

        updateUI();
        if (foodItem instanceof MealItem) {
            FoodItem[] ingredients = ((MealItem) foodItem).getIngredients();
            long[] filterIds = new long[ingredients.length];
            for (int i = 0; i < ingredients.length; i++)
                filterIds[i] = ingredients[i].getId();

            PantryFilter filter = new PantryFilter();
            filter.addFilterValue(filterIds, PantryFilter.FILTER_BY_ID);



            getSupportFragmentManager().beginTransaction()
                    .add(R.id.foodContent, PantryItemFragment.newInstance(filter, PantryItemFragment.SORT_BY_ID, true))
                    .commit();
        }
    }

    private void updateUI() {

        if (foodItem.isDepleted()) {
            findViewById(R.id.use).setVisibility(GONE);
            findViewById(R.id.finish).setVisibility(GONE);
        }
        ((TextView) findViewById(R.id.name)).setText(foodItem.getName());
        ((TextView) findViewById(R.id.quickInfo)).setText(foodItem.getLongFacts());
        ((TextView) findViewById(R.id.useCount)).setText(
                String.format("You have used this %d time" + (foodItem.getUseCount() == 1 ? "" : "s"), foodItem.getUseCount()));
        ((TextView) findViewById(R.id.pricePerUse)).setText(
                String.format("This has cost you $%03.2f per use", foodItem.getCostPerUse()));
    }

    public void useItem(View v) {
        foodItem.use(foodItem);
        updateUI();
        try {
            AppData.writeFoodUseData(openFileOutput(AppData.FOOD_USE_FILE_NAME, Context.MODE_PRIVATE));
        } catch (FileNotFoundException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void finishItem(View v) {
        foodItem.setDepleted(true);
        updateUI();
        try {
            AppData.writeFoodItemData(openFileOutput(AppData.FOOD_ITEM_FILE_NAME, Context.MODE_PRIVATE));
        } catch (FileNotFoundException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onListFragmentInteraction(FoodItem item, MyPantryItemRecyclerViewAdapter.ViewHolder viewHolder) {
        Intent i = new Intent(this, AnalyzeFoodItem.class);
        i.putExtra(AnalyzeFoodItem.EXTRA_FOOD_ID, item.getId());
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
}