package com.trapezateam.trapeza;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.trapezateam.trapeza.api.TrapezaRestClient;
import com.trapezateam.trapeza.api.models.StatisticsResponse;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StatisticsFragment extends AdministratorActivityFragment {

    private static final String TAG = "StatisticsFragment";

    @Bind(R.id.daily_sales)
    Button mDailySales;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistics_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mDailySales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDailySalesClicked();
            }
        });
    }


    private void onDailySalesClicked() {
        TrapezaRestClient.StatisticsMethods.boughtDay(new StatisticsCallback());
    }

    class StatisticsCallback implements Callback<StatisticsResponse> {

        @Override
        public void onResponse(Call<StatisticsResponse> call,
                               Response<StatisticsResponse> response) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(TrapezaRestClient.getFileUrl(
                            response.body().getFile())));
            startActivity(intent);
        }

        @Override
        public void onFailure(Call<StatisticsResponse> call, Throwable t) {
            t.printStackTrace();
            Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

