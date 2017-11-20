package com.cpre388.joesogard.mealworm;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
    implements PantryItemFragment.OnListFragmentInteractionListener{


    private static final int NEW_FOOD_ITEM_CODE = 69;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onListFragmentInteraction(FoodItem item, MyPantryItemRecyclerViewAdapter.ViewHolder viewHolder) {
        Toast.makeText(this, String.format("%s, pos %d", item.getName(), viewHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
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
