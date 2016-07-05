package com.trapezateam.trapeza;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.trapezateam.trapeza.api.TrapezaRestClient;
import com.trapezateam.trapeza.api.models.CategoryResponse;
import com.trapezateam.trapeza.api.models.DishResponse;
import com.trapezateam.trapeza.models.HashMapMenu;

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

    private DishAdapter mDishAdapter;
    private CategoryAdapter mCategoryAdapter;
    private GridView mMenu;

    private static List<DishResponse> dishResponseList;
    private static List<CategoryResponse> categoryResponseList;
    private static HashMapMenu menuTree;

    @Bind(R.id.totalPrice)
    TextView mTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cashier);


        ButterKnife.bind(this);
        mBill = new Bill();

        registerBillObserver();

        RecyclerView billRecyclerView = (RecyclerView) findViewById(R.id.item_list);
        RecyclerView.LayoutManager billLayoutManager = new LinearLayoutManager(this);
        billRecyclerView.setLayoutManager(billLayoutManager);
        billRecyclerView.setAdapter(mBill);
        mMenu = (GridView) findViewById(R.id.gvMenu);
        requestMenu();
    }

    private void registerBillObserver() {
        mBill.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                Log.i(TAG, "bill changed to " + mBill.getTotalPrice());
                String priceText = String.valueOf(mBill.getTotalPrice()) + " руб";
                mTotalPrice.setText(priceText);

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
    }

    void requestMenu() {
        final ProgressDialog dialog = new ProgressDialog(this);
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
                        mCategoryAdapter = new CategoryAdapter(menuTree, mMenu, mBill);
                        mMenu.setAdapter(mCategoryAdapter);

                    }

                    @Override
                    public void onFailure(Call<List<DishResponse>> call, Throwable t) {
                        Toast.makeText(CashierActivity.this, "Error getting dishes " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<CategoryResponse>> call, Throwable t) {
                Toast.makeText(CashierActivity.this, "Error getting categories " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


    public void onClickCancelOrder(View view) {
        mBill.clear();
        mTotalPrice.setText(mBill.getTotalPrice() + " руб");
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

        String priceText = String.valueOf(mBill.getTotalPrice()) + " руб";
        mTotalPrice.setText(priceText);


    }

}
