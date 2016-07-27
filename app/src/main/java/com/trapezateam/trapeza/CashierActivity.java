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

import com.trapezateam.trapeza.api.models.CategoryResponse;
import com.trapezateam.trapeza.api.models.DishResponse;
import com.trapezateam.trapeza.database.Category;
import com.trapezateam.trapeza.database.Dish;
import com.trapezateam.trapeza.database.RealmClient;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;


public class CashierActivity extends Activity {

    public static final String TAG = "CashierActivity";

    Bill mBill;

    private GridView mMenu;

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

        RealmClient.updateDatabase();

        DishAdapter da = new DishAdapter(this, RealmClient.getDishes());
        da.setOnDishClickedListener(new OnDishClickedListener() {
            @Override
            public void onDishClicked(Dish dish) {
                mBill.addEntry(dish);
            }
        });
        CategoriesAdapter ca = new CategoriesAdapter(this, RealmClient.getCategories());

        MenuAdapter ma = new MenuAdapter(da, ca);

        mMenu.setAdapter(ma);

        dialog.dismiss();


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
        RecyclerView billRecyclerView = (RecyclerView) findViewById(R.id.item_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        billRecyclerView.setLayoutManager(layoutManager);
        billRecyclerView.setAdapter(mBill);

        String priceText = String.valueOf(mBill.getTotalPrice()) + " руб";
        mTotalPrice.setText(priceText);
    }

}
