package com.trapezateam.trapeza;

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


public class CashierActivity extends Activity {

    public static final String TAG = "CashierActivity";

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

        mBill.addEntry(new Dish("dish dish", 8));

        for (int i = 0; i <= 11; i++) {
            addCategory(new Category(String.valueOf(i)));
        }
    }


    private void addAbstractItem(String name, int ButtonStyle, View.OnClickListener listener) {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        GridLayout.LayoutParams param = new GridLayout.LayoutParams();
        int viewCount = gridLayout.getChildCount();
        param.columnSpec = GridLayout.spec(viewCount % 4);
        param.rowSpec = GridLayout.spec(viewCount / 4);
        Button b = new Button(this, null, ButtonStyle);
        b.setOnClickListener(listener);
        b.setText(name);
        b.setLayoutParams(param);
        gridLayout.addView(b);
    }

    private void addCategory(Category c) {
        View.OnClickListener categoryListener =new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareItemGrid();
            }
        };
        addAbstractItem(c.getName(), R.attr.catButtonStyle,categoryListener);
    }

    private void addBackButton(){
        View.OnClickListener backListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareCategoryGrid();

            }

        };
        addAbstractItem("<-",R.attr.backButtonStyle,backListener);
    }

    private void addDish(final Dish d) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBill.addEntry(d);
            }

        };
        addAbstractItem(d.getName(), R.attr.dishButtonStyle, listener);
    }


    private void prepareItemGrid() {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        if (gridLayout.getChildCount() > 0)
            gridLayout.removeAllViews();
        addBackButton();
        populateCategoryItems(gridLayout, 0);
    }


    private void prepareCategoryGrid() {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        if (gridLayout.getChildCount() > 0)
            gridLayout.removeAllViews();
        populateCategories(gridLayout);
    }

    private void populateCategoryItems(GridLayout gl, int categoryId) {
        //TODO get items from this category and populate
        String[] names = {"Картошка", "Сало"};
        for (String name : names) {
            addDish(new Dish(name,45));
        }


    }

    private void populateCategories(GridLayout gl) {
        //TODO get categories
        for (int i = 0; i <= 10; i++) {
            addCategory(new Category("Olala"));
        }
    }


    public void onClickCancelOrder(View view) {
        mBill.clear();
        mTotalPrice.setText(mBill.getTotalPrice() + "");
    }
}
