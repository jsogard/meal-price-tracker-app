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
}
