package com.cpre388.joesogard.mealworm.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.RenderProcessGoneDetail;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cpre388.joesogard.mealworm.fragments.MyPantryItemRecyclerViewAdapter;
import com.cpre388.joesogard.mealworm.fragments.PantryItemFragment;
import com.cpre388.joesogard.mealworm.R;
import com.cpre388.joesogard.mealworm.data.AppData;
import com.cpre388.joesogard.mealworm.models.FoodItem;
import com.cpre388.joesogard.mealworm.models.GroceryItem;
import com.cpre388.joesogard.mealworm.models.MealItem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewFoodItem extends AppCompatActivity
    implements PantryItemFragment.OnListFragmentInteractionListener {

    private List<FoodItem> ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food);
        ingredients = new ArrayList<>();
    }

    public void checkFormat(View v) {
        boolean isGrocery = ((RadioButton) findViewById(R.id.groceryRadio)).isChecked();
        if (isGrocery) reformatGrocery(null);
        else reformatMeal(null);
    }

    public void reformatMeal(View v) {
        findViewById(R.id.priceInput).setVisibility(View.GONE);
        findViewById(R.id.ingredients).setVisibility(View.VISIBLE);
        findViewById(R.id.fragmentHolder).setVisibility(View.VISIBLE);
    }

    public void reformatGrocery(View v) {
        findViewById(R.id.priceInput).setVisibility(View.VISIBLE);
        findViewById(R.id.ingredients).setVisibility(View.GONE);
        findViewById(R.id.fragmentHolder).setVisibility(View.GONE);
    }

    public void completeNewItem(View v){
        EditText foodName = ((EditText)findViewById(R.id.inputName));
        if(foodName.getText().toString().length() == 0){
            Toast.makeText(this, "Must enter valid name", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = foodName.getText().toString();
        FoodItem food;
        if(((RadioButton) findViewById(R.id.groceryRadio)).isChecked()){
            // grocery item

            double price = Double.parseDouble(((EditText)findViewById(R.id.priceInput)).getText().toString());
            food = new GroceryItem(name, (float)price);
        } else {
            // meal item

            food = new MealItem(name, ingredients.toArray(new FoodItem[ingredients.size()]));
        }

        FoodItem.addItem(food);
        try{
            AppData.writeFoodItemData(openFileOutput(AppData.FOOD_ITEM_FILE_NAME, Context.MODE_PRIVATE));
        } catch (FileNotFoundException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        finish();
    }

    @Override
    public void onListFragmentInteraction(FoodItem item, MyPantryItemRecyclerViewAdapter.ViewHolder viewHolder) {
        if(viewHolder.toggleSelect()){
            ingredients.add(viewHolder.mItem);
        } else {
            ingredients.remove(viewHolder.mItem);
        }
    }
}