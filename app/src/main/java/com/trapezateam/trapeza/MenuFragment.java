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

import com.trapezateam.trapeza.adapters.CategoryAdapter;
import com.trapezateam.trapeza.adapters.CategoryEventsListener;
import com.trapezateam.trapeza.adapters.DishAdapter;
import com.trapezateam.trapeza.adapters.DishEventsListener;
import com.trapezateam.trapeza.adapters.MenuAdapter;
import com.trapezateam.trapeza.adapters.OnAddCategoryClickListener;
import com.trapezateam.trapeza.adapters.OnAddDishClickListener;
import com.trapezateam.trapeza.api.TrapezaRestClient;
import com.trapezateam.trapeza.api.models.StatusResponse;
import com.trapezateam.trapeza.database.Category;
import com.trapezateam.trapeza.database.Dish;
import com.trapezateam.trapeza.database.RealmClient;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
                        deleteDish(dish);
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
            public boolean onCategoryLongClicked(final Category category, View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton("Изменение", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getAdministratorActivity().startCategoryConfigurationFragment(category, false);
                    }
                });
                builder.setNegativeButton("Удаление", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteCategory(category);
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

    private void deleteCategory(final Category category) {
        TrapezaRestClient.CategoryMethods.delete(category, new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                Toast toast;
                if (response.body().isSuccess()) {
                    toast = Toast.makeText(getAdministratorActivity(), "Категория успешно удалена", Toast.LENGTH_SHORT);
                    RealmClient.deleteModel(category);
                    mMenuAdapter.notifyDataSetChanged();
                } else {
                    toast = Toast.makeText(getAdministratorActivity(), "Произошла ошибка, категория не удалена", Toast.LENGTH_SHORT);
                }
                toast.show();
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Error deleting category " + throwable.getMessage(),
                        Toast.LENGTH_LONG).show();
                throwable.printStackTrace();
            }
        });

    }


    private void deleteDish(final Dish dish) {
        TrapezaRestClient.DishMethods.delete(dish, new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                Toast toast;
                if (response.body().isSuccess()) {
                    toast = Toast.makeText(getAdministratorActivity(), "Блюдо успешно удалено", Toast.LENGTH_SHORT);
                    RealmClient.deleteModel(dish);
                    mMenuAdapter.notifyDataSetChanged();
                } else {
                    toast = Toast.makeText(getAdministratorActivity(), "Произошла ошибка, блюдо не удалено", Toast.LENGTH_SHORT);
                }
                toast.show();
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Error deleting dish " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                t.printStackTrace();

            }
        });

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

