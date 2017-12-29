package com.cpre388.joesogard.mealworm.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpre388.joesogard.mealworm.R;
import com.cpre388.joesogard.mealworm.models.FoodItem;
import com.cpre388.joesogard.mealworm.models.MealItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link FoodItem} and makes a call to the
 * specified {@link PantryItemFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPantryItemRecyclerViewAdapter extends RecyclerView.Adapter<MyPantryItemRecyclerViewAdapter.ViewHolder> {

    private final List<FoodItem> mValues;
    private final PantryItemFragment.OnListFragmentInteractionListener mListener;

    public MyPantryItemRecyclerViewAdapter(List<FoodItem> items, PantryItemFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_pantryitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mFoodName.setText(holder.mItem.getName());
        if(holder.mItem instanceof MealItem)
            holder.imageId = R.mipmap.meal_small;
        else
            holder.imageId = R.mipmap.ingr_small;
        holder.mFoodIcon.setImageResource(holder.imageId);
        holder.mQuickFacts.setText(holder.mItem.getQuickFacts());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem, holder);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mFoodName;
        public final TextView mQuickFacts;
        public ImageView mFoodIcon;
        public FoodItem mItem;
        public int imageId;

        private boolean selected;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mFoodName = (TextView) view.findViewById(R.id.foodName);
            mQuickFacts = (TextView) view.findViewById(R.id.quickFacts);
            mFoodIcon = (ImageView) view.findViewById(R.id.foodIcon);
            selected = false;
        }

        public boolean toggleSelect(){
            if(selected)
                mFoodIcon.setImageResource(imageId);
            else
                mFoodIcon.setImageResource(R.mipmap.check_med);
            selected = !selected;
            return selected;
        }

        public boolean isSelected(){ return selected; }

        @Override
        public String toString() {
            return super.toString() + " '" + mQuickFacts.getText() + "'";
        }
    }
}
