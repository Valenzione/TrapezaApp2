package com.trapezateam.trapeza;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.NumberPicker;
import android.widget.TextView;
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

    private static final int PAYMENT_CASH = 0;
    private static final int PAYMENT_CASHLESS = 1;

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
                if (mBill.getItemCount() == 0) {
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(CashierActivity.this, R.style.AppCompatAlertDialogStyle);
                builder.setPositiveButton("Наличный расчёт", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showChangeDialog(mBill.getPriceAfterDiscounts());
                    }
                });
                builder.setNegativeButton("Безналичный расчёт", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showPaymentConfirmationDialog(PAYMENT_CASHLESS, mBill.getPriceAfterDiscounts());
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

        mBill.clear();
    }

    void showChangeDialog(final double totalPrice) {
        LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.dialog_change, null);
        EditText changeInput = (EditText) dialogLayout.findViewById(R.id.change_input);
        final TextView changeOutput = (TextView) dialogLayout.findViewById(R.id.change_amount);
        changeOutput.setText("Клиент должен ещё " + totalPrice + " руб.");
        changeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                double eps = 1e-3;
                String text = editable.toString();
                double given = 0;
                Log.e(TAG, "Text is " + text);
                if (text.equals("") || text.equals(".")) {
                    given = 0;
                } else {
                    given = Double.parseDouble(text);
                }
                double difference = totalPrice - given;
                if (Math.abs(difference) < eps) {
                    changeOutput.setText("Готово!");
                    return;
                }
                if (difference > 0) {
                    changeOutput.setText("Клиент должен ещё " + difference + " руб.");
                } else {
                    changeOutput.setText("Сдача: " + -difference + " руб.");
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setView(dialogLayout);
        builder.setTitle("Сумма: " + totalPrice);
        builder.setPositiveButton("Готово", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showPaymentConfirmationDialog(PAYMENT_CASH, totalPrice);
            }
        });
        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    void showPaymentConfirmationDialog(final int paymentType, double price) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Подтверждение оплаты");
        String message = "Оплата производится ";
        if (paymentType == PAYMENT_CASH) {
            message += "НАЛИЧКОЙ";
        } else {
            message += "КАРТОЧКОЙ";
        }
        message += ". Сумма " + price;
        builder.setMessage(message);
        builder.setPositiveButton("Правильно", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                savePaymentToServer(paymentType);
            }
        });
        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    void savePaymentToServer(int paymentType) {
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
                double totalPrice = mBill.getPriceAfterDiscounts();
                double discount = mBill.getDiscount();
                double discountMoney = mBill.getPriceWithoutDiscounts() - mBill.getPriceAfterDiscounts(); // how much money client does not pay
                String priceText = "Оплата " + totalPrice + " руб";
                mPayButton.setText(priceText);
                if (discountMoney == -0) {
                    discountMoney = 0;
                }
                String discountText = "Скидка " + discount + "% (" + discountMoney + " .руб)";
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

        String priceText = "Оплата " + String.valueOf(mBill.getPriceAfterDiscounts()) + " руб";
        mPayButton.setText(priceText);
    }

    void openDiscountDialog(int value) {
        LayoutInflater inflater = (LayoutInflater)
                getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.number_picker_dialog_layout, null);
        final NumberPicker numberPicker = (NumberPicker) v.findViewById(R.id.picker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(100);
        numberPicker.setValue(value);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                .setTitle("Скидка")
                .setView(v)
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
                .show();
    }

    void onDiscountButtonClicked() {
        openDiscountDialog(mBill.getDiscount());
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CashierActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setPositiveButton("Остаться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setNegativeButton("Закончить смену", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                logOutAndFinish();
            }
        });
        builder.setNeutralButton("Не заканчивать смену", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    void logOutAndFinish() {
        SharedPreferencesHelper helper = new SharedPreferencesHelper(this);
        helper.removeToken();
        TrapezaRestClient.logout(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call,
                                   Response<StatusResponse> response) {
                Toast.makeText(CashierActivity.this, "Смена успешно закрыта", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Toast.makeText(CashierActivity.this, "Ошибка закрытия смены: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
