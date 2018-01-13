package com.cpre388.joesogard.mealworm.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpre388.joesogard.mealworm.R;
import com.cpre388.joesogard.mealworm.models.FoodItem;
import com.cpre388.joesogard.mealworm.models.FoodUse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PantryItemFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private PantryFilter pantryFilter;
    private Comparator<FoodItem> sorter;

    public static final int SORT_BY_ID = 1;
    public static final int SORT_BY_NAME = 2;
    public static final int SORT_BY_LAST_USE = 3;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PantryItemFragment() {
    }

    @SuppressWarnings("unused")
    public static PantryItemFragment newInstance(PantryFilter filter, int sortType, boolean ascending) {
        PantryItemFragment fragment = new PantryItemFragment();
        fragment.pantryFilter = filter;
        fragment.sorter = assignComparator(sortType, ascending);
        return fragment;
    }

    private static Comparator<FoodItem> assignComparator(int sortType, boolean ascending){
        final int ascMulti = ascending ? -1 : 1;
        switch(sortType){
            case SORT_BY_NAME:
                return new Comparator<FoodItem>() {
                    @Override
                    public int compare(FoodItem foodItem, FoodItem t1) {
                        return foodItem.getName().compareTo(t1.getName()) * ascMulti;
                    }
                };
            case SORT_BY_LAST_USE:
                return new Comparator<FoodItem>() {
                    @Override
                    public int compare(FoodItem foodItem, FoodItem t1) {
                        return foodItem.getLastUseTime().compareTo(t1.getLastUseTime()) * ascMulti;
                    }
                };
            case SORT_BY_ID:
            default:
                return new Comparator<FoodItem>() {
                    @Override
                    public int compare(FoodItem foodItem, FoodItem t1) {
                         return foodItem.compareTo(t1) * ascMulti;
                    }
                };
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pantryitem_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            List<FoodItem> foodList;
            if(pantryFilter != null)
                foodList = pantryFilter.filter(FoodItem.getItemList());
            else
                foodList = FoodItem.getItemList();

            foodList.sort(sorter);

            recyclerView.setAdapter(new MyPantryItemRecyclerViewAdapter(foodList, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(FoodItem item, MyPantryItemRecyclerViewAdapter.ViewHolder viewHolder);
    }


}
