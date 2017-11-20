package com.cpre388.joesogard.mealworm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cpre388.joesogard.mealworm.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class NewFoodItem extends AppCompatActivity
    implements PantryItemFragment.OnListFragmentInteractionListener{

    private boolean isGrocery;

    private List<FoodItem> ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food_item);
        isGrocery = true;
        ingredients = new ArrayList<>();
    }

    public void checkFormat(View v) {
        boolean newIsGrocery = ((RadioButton) findViewById(R.id.groceryRadio)).isChecked();
        if(newIsGrocery == isGrocery) return;

        if (newIsGrocery) reformatGrocery(null);
        else reformatMeal(null);
    }

    private FrameLayout emptyFrame() {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.specialView);
        frameLayout.removeAllViews();
        return frameLayout;
    }

    public void reformatMeal(View v) {
        if(!isGrocery) return;
        isGrocery = false;

        TextView text = new TextView(this);
        text.setText("Ingredients");
        text.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        emptyFrame().addView(text);

        findViewById(R.id.fragmentHolder).setVisibility(View.VISIBLE);
    }

    public void reformatGrocery(View v) {
        if(isGrocery) return;
        isGrocery = true;

        EditText editText = new EditText(this);
        editText.setHint("$0.00");
        editText.setId(R.id.priceInput);
        editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        emptyFrame().addView(editText);

        findViewById(R.id.fragmentHolder).setVisibility(View.GONE);
    }

    public void finish(View v){
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

        DummyContent.addItem(food);
        setResult(RESULT_OK);
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