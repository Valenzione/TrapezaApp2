package com.example.yuriy.trapeza;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


public class Cashier extends Activity {

    String[] names = {"Иван", "keka"};
    // имена атрибутов для Map

    final String ATTRIBUTE_NAME_NAME = "name";
    final String ATTRIBUTE_NAME_NUMBER = "number";
    final String ATTRIBUTE_NAME_PRICE = "price";
    final String ATTRIBUTE_NAME_TOTAL_PRICE = "tprice";
    SimpleAdapter sAdapter;
    ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);


        // массив имен атрибутов, из которых будут читаться данные
        String[] from = {ATTRIBUTE_NAME_NAME, ATTRIBUTE_NAME_NUMBER, ATTRIBUTE_NAME_PRICE, ATTRIBUTE_NAME_TOTAL_PRICE};
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = {R.id.pay_check_item_name, R.id.pay_check_item_number, R.id.pay_check_item_price, R.id.pay_check_item_total_price};

        // создаем адаптер
        sAdapter = new SimpleAdapter(this, data, R.layout.pay_check_item_view,
                from, to);

        // определяем список и присваиваем ему адаптер
        ListView lvSimple = (ListView) findViewById(R.id.item_list);
        lvSimple.setAdapter(sAdapter);

        addItemToPayCheck("Картошка по гречески, с луком и чесноком, сдобренная маслом и травами", 25);
        for (int i=0;i<=10;i++){
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
        addItemToPayCheck(((Button) v).getText().toString(), 26);

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
        addAbstractItem(name,  R.attr.catButtonStyle);
    }

    private void addItem(String name, GridLayout gridLayout) {
        addAbstractItem(name, R.attr.itemButtonStyle);
    }

    private void addItemToPayCheck(String name, int price) {

        int number = 1;
        Map<String, Object> m;
        m = new HashMap<String, Object>();
        m.put(ATTRIBUTE_NAME_NAME, name);
        m.put(ATTRIBUTE_NAME_NUMBER, number);
        m.put(ATTRIBUTE_NAME_PRICE, String.valueOf(price));
        m.put(ATTRIBUTE_NAME_TOTAL_PRICE, price * number);
        data.add(m);

        TextView totalPrice = (TextView) findViewById(R.id.totalPrice);
        int total=Integer.parseInt(totalPrice.getText().toString());
        totalPrice.setText(String.valueOf(total+price));

        sAdapter.notifyDataSetChanged();
    }

    private void prepareItemGrid() {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        if (((GridLayout) gridLayout).getChildCount() > 0)
            ((GridLayout) gridLayout).removeAllViews();
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
        if (((GridLayout) gridLayout).getChildCount() > 0)
            ((GridLayout) gridLayout).removeAllViews();
        populateCategories(gridLayout);
        addCategory("Test Category");
    }

    public void onClickDeleteItem(View v) {
        View parentRow = (View) v.getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);

        Map<String, Object> m = data.get(position);
        int price = Integer.parseInt(m.get(ATTRIBUTE_NAME_PRICE).toString());
        int number = Integer.parseInt(m.get(ATTRIBUTE_NAME_NUMBER).toString());
        TextView totalPrice = (TextView) findViewById(R.id.totalPrice);
        int total=Integer.parseInt(totalPrice.getText().toString());
        if(number>1){totalPrice.setText(String.valueOf(total-price*(number)));}


        data.remove(position);
        sAdapter.notifyDataSetChanged();
    }

    public void onClickAddPayCheckItem(View v) {
        View parentRow = (View) v.getParent();
        ListView lv = (ListView) findViewById(R.id.item_list);
        EditText ed = (EditText) parentRow.findViewById(R.id.pay_check_item_number);
        int number = Integer.parseInt(String.valueOf(ed.getText()));
        int position = lv.getPositionForView(parentRow);


        Map<String, Object> m = data.get(position);
        m.remove(ATTRIBUTE_NAME_NUMBER);
        m.remove(ATTRIBUTE_NAME_TOTAL_PRICE);
        int price = Integer.parseInt(m.get(ATTRIBUTE_NAME_PRICE).toString());
        m.put(ATTRIBUTE_NAME_NUMBER, ++number);
        m.put(ATTRIBUTE_NAME_TOTAL_PRICE, number * price);


        TextView totalPrice = (TextView) findViewById(R.id.totalPrice);
        int total=Integer.parseInt(totalPrice.getText().toString());
        totalPrice.setText(String.valueOf(total+price));

        sAdapter.notifyDataSetChanged();

    }

    public void onClickRemovePayCheckItem(View v) {
        View parentRow = (View) v.getParent();
        ListView lv = (ListView) findViewById(R.id.item_list);
        EditText ed = (EditText) parentRow.findViewById(R.id.pay_check_item_number);
        int number = Integer.parseInt(String.valueOf(ed.getText()));
        int position = lv.getPositionForView(parentRow);
        Map<String, Object> m = data.get(position);
        int price = Integer.parseInt(m.get(ATTRIBUTE_NAME_PRICE).toString());
        TextView totalPrice = (TextView) findViewById(R.id.totalPrice);
        int total=Integer.parseInt(totalPrice.getText().toString());
        totalPrice.setText(String.valueOf(total-price));
        if ((number - 1) == 0) {
            onClickDeleteItem(v);
        } else {

            m.remove(ATTRIBUTE_NAME_NUMBER);
            m.remove(ATTRIBUTE_NAME_TOTAL_PRICE);

            m.put(ATTRIBUTE_NAME_NUMBER, --number);
            m.put(ATTRIBUTE_NAME_TOTAL_PRICE, number * price);

            sAdapter.notifyDataSetChanged();
            ;
        }

    }


    private void populateCategoryItems(GridLayout gl, int categoryId) {
        //TODO get items from this category and populate
        String[] names = {"Картошка", "Сало"};
        for (int i = 0; i < names.length; i++) {
            addItem(names[i], gl);
        }
    }

    private void populateCategories(GridLayout gl) {
        //TODO get categories
    }


    public void onClickCancelOrder(View view) {
        data.clear();
        sAdapter.notifyDataSetChanged();
        TextView totalPrice =(TextView) findViewById(R.id.totalPrice);
        int total=Integer.parseInt(totalPrice.getText().toString());
        totalPrice.setText(String.valueOf(0));

    }
}
