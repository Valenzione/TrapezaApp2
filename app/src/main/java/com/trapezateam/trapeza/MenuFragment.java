package com.trapezateam.trapeza;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.trapezateam.trapeza.database.Category;
import com.trapezateam.trapeza.database.Dish;
import com.trapezateam.trapeza.database.RealmClient;


public class MenuFragment extends AdministratorActivityFragment {

    private static final String TAG = "MenuFragment";


    private GridView mMenu;
    private MenuAdapter mMenuAdapter;


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

        DishAdapter dishAdapter = new DishAdapter(getActivity(), RealmClient.getDishes());
        dishAdapter.setOnDishClickedListener(new OnDishClickedListener() {
            @Override
            public void onDishClicked(Dish dish) {
                getAdministratorActivity().startDishConfigurationFragment(dish, null, false);
            }
        });
        dishAdapter.setShowAddButton(true);
        CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), RealmClient.getCategories());
        categoryAdapter.setShowAddButton(true);
        mMenuAdapter = new MenuAdapter(dishAdapter, categoryAdapter);
        mMenuAdapter.setOnAddCategoryClickListener(new OnAddCategoryClickListener() {
            @Override
            public void onAddCategoryClicked() {
                getAdministratorActivity().startCategoryConfigurationFragment(false);
            }
        });
        mMenuAdapter.setOnAddDishClickListener(new OnAddDishClickListener() {
            @Override
            public void onAddDishClicked(Category category) {
                getAdministratorActivity().startDishConfigurationFragment(null, category.getCategoryId(), false);
            }
        });
        mMenu.setAdapter(mMenuAdapter);
        dialog.dismiss();
    }

    /**
     * Calls {@link MenuAdapter#goBack()}
     *
     * @return <code>true</code> if something happened
     */
    boolean onBackPressed() {
        return mMenuAdapter.goBack();
    }
}

