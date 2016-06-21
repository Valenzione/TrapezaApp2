package com.example.yuriy.trapeza;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DummyFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View emptyView = inflater.inflate(R.layout.dummy_layout,container,false);
        return emptyView;
    }



}