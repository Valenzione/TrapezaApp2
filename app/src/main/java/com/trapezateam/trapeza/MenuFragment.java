package com.trapezateam.trapeza;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.trapezateam.trapeza.api.TrapezaRestClient;
import com.trapezateam.trapeza.api.models.CategoryResponse;
import com.trapezateam.trapeza.api.models.DishResponse;
import com.trapezateam.trapeza.database.RealmClient;
import com.trapezateam.trapeza.models.HashMapMenu;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuFragment extends Fragment {

    private static final String TAG = "MenuFragment";


    private DishAdapter mDishAdapter;
    private CategoryAdapter mCategoryAdapter;
    private GridView mMenu;

    private static List<DishResponse> dishResponseList;
    private static List<CategoryResponse> categoryResponseList;
    private static HashMapMenu menuTree;


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
        dialog.setMessage("Getting dishes");
        dialog.setCancelable(false);
        dialog.show();
        TrapezaRestClient.categoriesList(new Callback<List<CategoryResponse>>() {
            @Override
            public void onResponse(Call<List<CategoryResponse>> call,
                                   Response<List<CategoryResponse>> response) {
                Log.d(TAG, "Category Response received");
                categoryResponseList = new ArrayList<>(response.body());

                dialog.setMessage("Getting dishes");
                TrapezaRestClient.dishesList(new Callback<List<DishResponse>>() {
                    @Override
                    public void onResponse(Call<List<DishResponse>> call,
                                           Response<List<DishResponse>> response) {
                        Log.d(TAG, "Dish Response received");
                        dishResponseList = new ArrayList<>(response.body());
                        menuTree = new HashMapMenu(dishResponseList, categoryResponseList);
                        mCategoryAdapter = new CategoryAdapter(RealmClient.getCategories(), mMenu, null);
                        mMenu.setAdapter(mCategoryAdapter);

                    }

                    @Override
                    public void onFailure(Call<List<DishResponse>> call, Throwable t) {
                        Toast.makeText(getActivity(), "Error getting dishes " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<CategoryResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error getting categories " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


}

