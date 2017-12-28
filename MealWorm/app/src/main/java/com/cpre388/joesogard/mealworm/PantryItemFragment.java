package com.cpre388.joesogard.mealworm;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpre388.joesogard.mealworm.data.AppData;
import com.cpre388.joesogard.mealworm.models.FoodItem;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PantryItemFragment extends Fragment {

    private static final String EXTRA_ID_FILTER = "EXTRA-ID-FILTER";
    private long[] itemIdFilter = null;

    protected static final String EXTRA_CLASS_FILTER = "EXTRA-CLASS-FILTER";
    private int classFilter = -1;


    private OnListFragmentInteractionListener mListener;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PantryItemFragment() {
    }

    @SuppressWarnings("unused")
    public static PantryItemFragment newInstance(long filterItems[]) {
        PantryItemFragment fragment = new PantryItemFragment();
        Bundle args = new Bundle();
        args.putLongArray(EXTRA_ID_FILTER, filterItems);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            itemIdFilter = getArguments().getLongArray(EXTRA_ID_FILTER);
            classFilter = getArguments().getInt(EXTRA_CLASS_FILTER, 0);
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
            if(itemIdFilter != null) foodList = AppData.filterItems(itemIdFilter);
            else if(classFilter != -1) foodList = AppData.filterItems(classFilter);
            else foodList = AppData.ITEMS;

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
