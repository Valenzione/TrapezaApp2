package com.trapezateam.trapeza;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.trapezateam.trapeza.adapters.CategoryAdapter;
import com.trapezateam.trapeza.adapters.DishAdapter;
import com.trapezateam.trapeza.adapters.DishEventsListener;
import com.trapezateam.trapeza.adapters.MenuAdapter;
import com.trapezateam.trapeza.api.TrapezaRestClient;
import com.trapezateam.trapeza.api.models.StatusResponse;
import com.trapezateam.trapeza.database.Dish;
import com.trapezateam.trapeza.database.RealmClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CashierActivity extends Activity {

    public static final String TAG = "CashierActivity";

    Bill mBill;

    private GridView mMenu;

    @Bind(R.id.pay_button)
    Button mPayButton;

    @Bind(R.id.discount_button)
    Button mDiscountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cashier);

        ButterKnife.bind(this);
        mPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CashierActivity.this);
                builder.setPositiveButton("Наличный", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onPaymentComplete(0);
                    }
                });
                builder.setNegativeButton("Безналичный расчет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onPaymentComplete(1);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        mBill = new Bill();

        registerBillObserver();

        RecyclerView billRecyclerView = (RecyclerView) findViewById(R.id.item_list);
        RecyclerView.LayoutManager billLayoutManager = new LinearLayoutManager(this);
        billRecyclerView.setLayoutManager(billLayoutManager);
        billRecyclerView.setAdapter(mBill);
        mMenu = (GridView) findViewById(R.id.gvMenu);
        requestMenu();

        mDiscountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDiscountButtonClicked();
            }
        });
    }

    void onPaymentComplete(int paymentType) {
        TrapezaRestClient.PaymentMethods.buyDish(mBill.getPriceIdQuantityPairs(), paymentType,
                mBill.getDiscountFraction(),
                new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        if (!response.body().isSuccess()) {
                            onFailure(call, new Throwable(response.body().getMessage()));
                            return;
                        }
                        Toast.makeText(CashierActivity.this, "Оплата сохранена на сервере",
                                Toast.LENGTH_SHORT).show();
                        onPaymentSaved();
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(CashierActivity.this, "Ошибка сохранения оплаты на сервере: "
                                + t.getMessage(), Toast.LENGTH_SHORT).show();
                        onPaymentSaved();
                    }
                });
    }

    void onPaymentSaved() {
        mBill.clear();
    }

    private void registerBillObserver() {
        mBill.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                Log.i(TAG, "bill changed to " + mBill.getTotalPrice());
                String priceText = "Оплата " + mBill.getTotalPrice() + " руб";
                mPayButton.setText(priceText);
                String discountText = "Скидка " + mBill.getDiscount() + "%";
                mDiscountButton.setText(discountText);
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
        dialog.setMessage("Getting menu");
        dialog.setCancelable(false);
        dialog.show();

        DishAdapter da = new DishAdapter(this, RealmClient.getDishes());
        da.setDishEventsListener(new DishEventsListener() {
            @Override
            public void onDishClicked(Dish dish, View view) {
                mBill.addEntry(dish);
            }

            @Override
            public boolean onDishLongClicked(Dish dish, View view) {
                return false;
            }
        });
        final CategoryAdapter ca = new CategoryAdapter(this, RealmClient.getCategories());
        MenuAdapter ma = new MenuAdapter(da, ca);

        mMenu.setAdapter(ma);

        dialog.dismiss();
    }


    public void onClickCancelOrder(View view) {
        mBill.clear();
        String priceText = "Оплата " + String.valueOf(mBill.getTotalPrice()) + " руб";
        mPayButton.setText(priceText);
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

        registerBillObserver();

        String priceText = "Оплата " + String.valueOf(mBill.getTotalPrice()) + " руб";
        mPayButton.setText(priceText);
    }

    void openSaleDialog(int value) {
        LayoutInflater inflater = (LayoutInflater)
                getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final NumberPicker numberPicker = (NumberPicker) inflater.inflate(R.layout.number_picker_dialog_layout, null);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(100);
        numberPicker.setValue(value);
        AlertDialog d = new AlertDialog.Builder(this)
                .setTitle("Text Size:")
                .setView(numberPicker)
                .setPositiveButton("Готово",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mBill.setDiscount(numberPicker.getValue());
                            }
                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                .create();
        d.show();
    }

    void onDiscountButtonClicked() {
        openSaleDialog(mBill.getDiscount());
    }

}
