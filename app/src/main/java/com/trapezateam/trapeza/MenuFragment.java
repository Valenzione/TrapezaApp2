package com.trapezateam.trapeza;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

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
        dishAdapter.setDishEventsListener(new DishEventsListener() {
            @Override
            public void onDishClicked(Dish dish, View view) {
                getAdministratorActivity().startDishConfigurationFragment(dish, null, false);
            }

            @Override
            public boolean onDishLongClicked(final Dish dish, View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton("Изменение", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getAdministratorActivity().startDishConfigurationFragment(dish, null, false);
                    }
                });
                builder.setNegativeButton("Удаление", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast t = Toast.makeText(getAdministratorActivity(), "Произошло удаление",Toast.LENGTH_SHORT);
                        t.show();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
        dishAdapter.setShowAddButton(true);
        CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), RealmClient.getCategories());
        categoryAdapter.setShowAddButton(true);
        categoryAdapter.setCategoryEventsListener(new CategoryEventsListener() {
            @Override
            public void onCategoryClicked(Category category, View view) {

            }

            @Override
            public boolean onCategoryLongClicked(Category category, View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton("Изменение", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast t = Toast.makeText(getAdministratorActivity(), "Произошло изменение",Toast.LENGTH_SHORT);
                        t.show();
                    }
                });
                builder.setNegativeButton("Удаление", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast t = Toast.makeText(getAdministratorActivity(), "Произошло удаление",Toast.LENGTH_SHORT);
                        t.show();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
        mMenuAdapter = new MenuAdapter(dishAdapter, categoryAdapter);
        mMenuAdapter.setOnAddCategoryClickListener(new OnAddCategoryClickListener() {
            @Override
            public void onAddCategoryClicked(View view) {
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

