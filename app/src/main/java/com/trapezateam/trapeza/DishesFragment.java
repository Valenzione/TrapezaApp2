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
import com.trapezateam.trapeza.api.models.DishResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DishesFragment extends Fragment {
    private static final String TAG = "DishesFragment";
    final int MENU_EDIT = 4;
    final int MENU_DELETE = 5;


    GridView gvDishes;
    DishAdapter adapterDishes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dishes_fragment, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        gvDishes = (GridView) getView().findViewById(R.id.gvDishes);
        requestDishes();


    }

    void requestDishes() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Getting dishes");
        dialog.setCancelable(false);
        dialog.show();
        TrapezaRestClient.dishesList(new Callback<List<DishResponse>>() {
            @Override
            public void onResponse(Call<List<DishResponse>> call,
                                   Response<List<DishResponse>> response) {
                Log.d(TAG, "Response received");
                List<DishResponse> body = response.body();
//                adapterDishes = new DishAdapter(body);
//                gvDishes.setAdapter(adapterDishes);
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<DishResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error getting dishes " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}