package com.example.yuriy.trapeza;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;


public class Cashier extends Activity {

    public static final String TAG = "Cashier";

    Bill mBill;

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

        // определяем список и присваиваем ему адаптер
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.item_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mBill);

        mBill.addEntry(new Dish("dish dish", 12));

        for (int i = 0; i <= 10; i++) {
            addCategory(String.valueOf("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK"));
        }
    }

    public void onBackButtonCLicked(View v) {
        prepareCategoryGrid();
    }

    public void onCategoryButtonClicked(View v) {
        prepareItemGrid();

    }

    public void onItemButtonClicked(View v) {
        Button b = (Button) v;
        mBill.addEntry(new Dish(b.getText().toString(), 12));
        Log.i(TAG, "Want to add " + b.getText().toString());
    }

    private void addAbstractItem(String name, int ButtonStyle) {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        GridLayout.LayoutParams param = new GridLayout.LayoutParams();
        int viewCount = gridLayout.getChildCount();
        param.columnSpec = GridLayout.spec(viewCount % 4);
        param.rowSpec = GridLayout.spec(viewCount / 4);
        Button example = new Button(this, null, ButtonStyle);
        example.setText(name);
        example.setLayoutParams(param);
        gridLayout.addView(example);
    }

    private void addCategory(String name) {
        addAbstractItem(name, R.attr.catButtonStyle);
    }

    private void addItem(String name, GridLayout gridLayout) {
        addAbstractItem(name, R.attr.itemButtonStyle);
    }


    private void prepareItemGrid() {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        if (gridLayout.getChildCount() > 0)
            gridLayout.removeAllViews();
        addBackButton(gridLayout);
        populateCategoryItems(gridLayout, 0);
    }

    private void addBackButton(GridLayout gridLayout) {
        GridLayout.LayoutParams param = new GridLayout.LayoutParams();
        int ButtonStyle = R.attr.backButtonStyle;
        Button example = new Button(this, null, ButtonStyle);
        example.setText("<-");
        example.setLayoutParams(param);
        gridLayout.addView(example);
    }

    private void prepareCategoryGrid() {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        if (gridLayout.getChildCount() > 0)
            gridLayout.removeAllViews();
        populateCategories(gridLayout);
        addCategory("Test Category");
    }

    private void populateCategoryItems(GridLayout gl, int categoryId) {
        //TODO get items from this category and populate
        String[] names = {"Картошка", "Сало"};
        for (String name : names) {
            addItem(name, gl);
        }
    }

    private void populateCategories(GridLayout gl) {
        //TODO get categories
    }


    public void onClickCancelOrder(View view) {
        mBill.clear();
        mTotalPrice.setText(mBill.getTotalPrice() + "");
    }
}
