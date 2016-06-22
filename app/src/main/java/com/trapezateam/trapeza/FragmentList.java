package com.trapezateam.trapeza;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

public class FragmentList extends ListFragment {

    private static final Map<String, Integer> myMap;

    static {
        myMap = new HashMap<String, Integer>();
        myMap.put("Category", R.layout.category_detail);
        myMap.put("Statistics", R.layout.statistics_detail);
        myMap.put("Dishes", R.layout.dishes_detail);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] values = new String[]{
                "Dishes",
                "Category",
                "Statistics"};
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);

        Fragment replacementFragment;
        switch (item) {
            case "Statistics":
                replacementFragment = new StatisticsFragment();
                break;
            case "Dishes":
                replacementFragment = new DishesFragment();
                break;
            case "Categories":
                replacementFragment = new CategoryFragment();
                break;
            default:
                replacementFragment = new CategoryFragment();
                break;
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.dummy_fragment, replacementFragment, "F1");
        fragmentTransaction.commit();
    }
}