package com.cpre388.joesogard.mealworm;

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
        isGrocery = false;
        ingredients = new ArrayList<>();

        reformatGrocery(null);
    }

    public void checkFormat(View v) {
        boolean newIsGrocery = ((RadioButton) findViewById(R.id.groceryRadio)).isChecked();
        if(newIsGrocery == isGrocery) return;

        if (newIsGrocery) reformatGrocery(null);
        else reformatMeal(null);
    }

    private FrameLayout emptyFrame() {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.extraSettingHolder);
        frameLayout.removeAllViews();
        return frameLayout;
    }

    public void reformatMeal(View v) {
        if(!isGrocery) return;
        isGrocery = false;

        emptyFrame();
        PantryItemFragment frag = new PantryItemFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.extraSettingHolder, new PantryItemFragment())
                .commit();
    }

    public void reformatGrocery(View v) {
        if(isGrocery) return;
        isGrocery = true;

        EditText editText = new EditText(this);
        editText.setHint("$0.00");
        editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        emptyFrame().addView(editText);
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