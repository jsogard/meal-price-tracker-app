package com.cpre388.joesogard.mealworm.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cpre388.joesogard.mealworm.MyPantryItemRecyclerViewAdapter;
import com.cpre388.joesogard.mealworm.PantryItemFragment;
import com.cpre388.joesogard.mealworm.R;
import com.cpre388.joesogard.mealworm.models.FoodItem;

public class MainActivity extends AppCompatActivity
    implements PantryItemFragment.OnListFragmentInteractionListener {


    private static final int NEW_FOOD_ITEM_CODE = 69;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode){
            case NEW_FOOD_ITEM_CODE:
                PantryItemFragment currFrag = (PantryItemFragment)getSupportFragmentManager().getFragments().get(0);
                getSupportFragmentManager().beginTransaction()
                        .detach(currFrag)
                        .attach(currFrag)
                        .commit();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }




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
    }
}
