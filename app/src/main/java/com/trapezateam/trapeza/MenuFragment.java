package com.trapezateam.trapeza;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;


import com.trapezateam.trapeza.database.Dish;
import com.trapezateam.trapeza.database.RealmClient;


public class MenuFragment extends Fragment {

    private static final String TAG = "MenuFragment";


    private GridView mMenu;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMenu = (GridView) getView().findViewById(R.id.gvCategories);
        requestMenu();
    }

    void requestMenu() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Getting menu");
        dialog.setCancelable(false);
        dialog.show();

        DishAdapter da = new DishAdapter(getActivity(), RealmClient.getDishes());
        da.setOnDishClickedListener(new OnDishClickedListener() {
            @Override
            public void onDishClicked(Dish dish) {
                modifyDish(dish);
            }
        });
        CategoriesAdapter ca = new CategoriesAdapter(getActivity(), RealmClient.getCategories());
        mMenu.setAdapter(new MenuAdapter(da, ca));

        dialog.dismiss();


    }

    private void modifyDish(Dish dish) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("dish", dish);
        DishConfigurationFragment configurationFragment = new DishConfigurationFragment();
        configurationFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.dummy_fragment, configurationFragment);
        fragmentTransaction.commit();
    }


}

