package com.trapezateam.trapeza;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class CashierActivity extends Activity {

    public static final String TAG = "CashierActivity";

    Bill mBill;

    private List<String> mCategoryData = new ArrayList<String>();
    private List<String> mDishData=new ArrayList<String>();;
    private GridView mGvMenu;
    private ArrayAdapter<String> mCategoryAdapterMenu;
    private ArrayAdapter<String> mDishAdapterMenu;
    @Bind(R.id.totalPrice)
    TextView mTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);
        ButterKnife.bind(this);
        mBill = new Bill();

        mCategoryData.add("a");
        mCategoryData.add("b");
        mDishData.add("Back");
        mDishData.add("alphabet");
        mDishData.add("Google");

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

        // определяем список и присваиваем ему адаптер
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.item_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mBill);

        mBill.addEntry(new Dish("dish dish", 8));

        mCategoryAdapterMenu = new ArrayAdapter<String>(this, R.layout.category_button, R.id.squareButton, mCategoryData);
        mDishAdapterMenu = new ArrayAdapter<String>(this, R.layout.dish_button, R.id.squareButton, mDishData);
        mGvMenu = (GridView) findViewById(R.id.gvMenu);
        mGvMenu.setAdapter(mCategoryAdapterMenu);


        Log.d(TAG, "Done");

    }

    public void onClickAddToBill(View v) {
        if (mDishData.indexOf(((TextView) v).getText()) == 0) {
            mGvMenu.setAdapter(mCategoryAdapterMenu);
        } else {
            mBill.addEntry(new Dish(((TextView) v).getText().toString(), 12));
        }
    }

    public void onClickShowDish(View v) {
        //TODO change dish data on category
        mGvMenu.setAdapter(mDishAdapterMenu);
    }


    public void onClickCancelOrder(View view) {
        mBill.clear();
        mTotalPrice.setText(mBill.getTotalPrice() + "");
    }
}
