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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment {

    private static final String TAG ="CategoryFragment" ;
    final int MENU_EDIT = 4;
    final int MENU_DELETE = 5;


    GridView gvCategories;
    CategoryAdapter adapterCategories;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        gvCategories = (GridView) getView().findViewById(R.id.gvCategories);
        requestCategories();


    }


    void requestCategories() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Getting categories");
        dialog.setCancelable(false);
        dialog.show();
        TrapezaRestClient.categoriesList(new Callback<List<CategoryResponse>>() {
            @Override
            public void onResponse(Call<List<CategoryResponse>> call,
                                   Response<List<CategoryResponse>> response) {
                Log.d(TAG, "Response received");
                List<CategoryResponse> body = response.body();
                adapterCategories = new CategoryAdapter(body);
                gvCategories.setAdapter(adapterCategories);
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

