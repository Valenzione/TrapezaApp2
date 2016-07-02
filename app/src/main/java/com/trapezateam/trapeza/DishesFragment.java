package com.trapezateam.trapeza;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.trapezateam.trapeza.api.TrapezaRestClient;
import com.trapezateam.trapeza.api.models.DishResponse;
import com.trapezateam.trapeza.models.DishTree;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DishesFragment extends Fragment {
    private static final String TAG = "DishesFragment";
    final int MENU_EDIT = 4;
    final int MENU_DELETE = 5;

    String[] data = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"};

    GridView gvDishes;
    ArrayAdapter<String> adapterDishes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            data = savedInstanceState.getStringArray("dishData");
        }
        View view = inflater.inflate(R.layout.dishes_fragment, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        gvDishes = (GridView) getView().findViewById(R.id.gvDishes);
        adapterDishes = new ArrayAdapter<>(getActivity(), R.layout.dish_button, R.id.squareButton, data);
        gvDishes.setAdapter(adapterDishes);

    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, MENU_EDIT, 0, "Edit");
        menu.add(0, MENU_DELETE, 0, "Delete");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray("dishData", data);
    }

}