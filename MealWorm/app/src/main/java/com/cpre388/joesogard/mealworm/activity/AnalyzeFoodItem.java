package com.cpre388.joesogard.mealworm.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cpre388.joesogard.mealworm.fragments.MyPantryItemRecyclerViewAdapter;
import com.cpre388.joesogard.mealworm.fragments.PantryItemFragment;
import com.cpre388.joesogard.mealworm.R;
import com.cpre388.joesogard.mealworm.data.AppData;
import com.cpre388.joesogard.mealworm.models.*;

import java.io.FileNotFoundException;

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

        if (foodItem instanceof MealItem) {
            setContentView(R.layout.activity_meal_item);
        } else if (foodItem instanceof GroceryItem) {
            setContentView(R.layout.activity_grocery_item);
        }

        updateUI();
        if (foodItem instanceof MealItem) {
            FoodItem[] ingredients = ((MealItem) foodItem).getIngredients();
            long[] filterIds = new long[ingredients.length];
            for (int i = 0; i < ingredients.length; i++)
                filterIds[i] = ingredients[i].getId();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ingredients, PantryItemFragment.newInstance(filterIds))
                    .commit();
        }
    }

    private void updateUI() {

        if (foodItem.isDepleted()) {
            findViewById(R.id.use).setVisibility(GONE);
            findViewById(R.id.finish).setVisibility(GONE);
        }
        ((TextView) findViewById(R.id.name)).setText(foodItem.getName());
        ((TextView) findViewById(R.id.quickInfo)).setText(foodItem.getQuickFacts());
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
            e.printStackTrace();
        }
    }

    public void finishItem(View v) {
        foodItem.setDepleted(true);
        updateUI();
        try {
            AppData.writeFoodItemData(openFileOutput(AppData.FOOD_ITEM_FILE_NAME, Context.MODE_PRIVATE));
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

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
}