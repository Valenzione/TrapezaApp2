package com.trapezateam.trapeza;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.trapezateam.trapeza.api.TrapezaRestClient;
import com.trapezateam.trapeza.api.models.DishResponse;
import com.trapezateam.trapeza.models.Dish;
import com.trapezateam.trapeza.models.DishTree;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CashierActivity extends Activity {

    public static final String TAG = "CashierActivity";

    Bill mBill;

    private DishTreeAdapter mDishAdapter;
    private GridView mDishMenu;

    @Bind(R.id.totalPrice)
    TextView mTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);
        ButterKnife.bind(this);
        mBill = new Bill();

        mBill.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                Log.i(TAG, "bill changed");
                mTotalPrice.setText(mBill.getTotalPrice() + " руб.");
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                Log.i(TAG, "Range changed " + positionStart + ", itemCount " + itemCount);
                onChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                super.onItemRangeChanged(positionStart, itemCount, payload);
                Log.i(TAG, "Range changed with payload " + positionStart + ", itemCount " + itemCount);
                onChanged();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                Log.i(TAG, "Range inserted " + positionStart + ", itemCount " + itemCount);
                onChanged();
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                Log.i(TAG, "Range moved " + fromPosition + " to " + toPosition + ", itemCount " + itemCount);
                onChanged();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                Log.i(TAG, "Range removed " + positionStart + ", itemCount " + itemCount);
                onChanged();
            }
        });

        RecyclerView billRecyclerView = (RecyclerView) findViewById(R.id.item_list);
        RecyclerView.LayoutManager billLayoutManager = new LinearLayoutManager(this);
        billRecyclerView.setLayoutManager(billLayoutManager);
        billRecyclerView.setAdapter(mBill);


        mDishMenu = (GridView) findViewById(R.id.gvMenu);

        requestDishes();

    }

    void requestDishes() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Getting dishes");
        dialog.setCancelable(false);
        dialog.show();
        TrapezaRestClient.dishesList(new Callback<List<DishResponse>>() {
            @Override
            public void onResponse(Call<List<DishResponse>> call,
                                   Response<List<DishResponse>> response) {
                Log.d(TAG, "Response received");
                List<DishResponse> body = response.body();
                DishTree tree = new DishTree(body);
                mDishAdapter = new DishTreeAdapter(tree, mBill);
                mDishMenu.setAdapter(mDishAdapter);
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<DishResponse>> call, Throwable t) {
                Toast.makeText(CashierActivity.this, "Error getting dishes " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                t.printStackTrace();

            }
        });
    }

    public void onClickCancelOrder(View view) {
        mBill.clear();
        mTotalPrice.setText(mBill.getTotalPrice() + "");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("bill", mBill);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mBill = savedInstanceState.getParcelable("bill");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.item_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mBill);


        mTotalPrice.setText(mBill.getTotalPrice() + " руб");

        // TODO: restore Dishes

//        if (mInCategoryMenu) {
//            mCategoryData = savedInstanceState.getStringArrayList("categoryData");
//            mCategoryAdapterMenu = new ArrayAdapter<>(this, R.layout.category_button, R.id.squareButton, mCategoryData);
//            mDishMenu.setAdapter(mCategoryAdapterMenu);
//        } else {
//            mDishData = savedInstanceState.getStringArrayList("dishData");
//            mDishAdapterMenu = new ArrayAdapter<>(this, R.layout.dish_button, R.id.squareButton, mDishData);
//            mDishMenu.setAdapter(mDishAdapterMenu);
//        }

    }

}
