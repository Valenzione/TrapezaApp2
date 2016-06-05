package com.example.yuriy.trapeza;

import android.app.Activity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Cashier extends Activity {

    String[] names = {"Иван", "keka", "k", "Иван", "keka", "k", "Иван", "keka", "k", "Иван", "keka", "k"};
    // имена атрибутов для Map
    final String ATTRIBUTE_NAME_DELETE = "delete";
    final String ATTRIBUTE_NAME_NAME = "name";
    final String ATTRIBUTE_NAME_REMOVE = "remove";
    final String ATTRIBUTE_NAME_NUMBER = "number";
    final String ATTRIBUTE_NAME_ADD = "add";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);
        Button payButton = (Button) findViewById(R.id.pay_button);


        // упаковываем данные в понятную для адаптера структуру
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(
                names.length);
        Map<String, Object> m;
        for (int i = 0; i < names.length; i++) {
            m = new HashMap<String, Object>();
            m.put(ATTRIBUTE_NAME_NAME, names[i]);
            m.put(ATTRIBUTE_NAME_NUMBER, 1);
            data.add(m);
        }

        // массив имен атрибутов, из которых будут читаться данные
        String[] from = {ATTRIBUTE_NAME_NAME, ATTRIBUTE_NAME_NUMBER};
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = {R.id.pay_check_item_name, R.id.pay_check_item_number};

        // создаем адаптер
        SimpleAdapter sAdapter = new SimpleAdapter(this, data, R.layout.pay_check_item_view,
                from, to);


        // определяем список и присваиваем ему адаптер
        ListView lvSimple = (ListView) findViewById(R.id.item_list);
        lvSimple.setAdapter(sAdapter);


    }

    public void onBackButtonCLicked(View v) {
        prepareCategoryGrid();
    }

    public void onCategoryButtonClicked(View v) {
        prepareItemGrid();

    }

    public void onItemButtonClicked(View v) {
        System.out.println("KEAK");

    }

    private void addItem(String name) {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        GridLayout.LayoutParams param = new GridLayout.LayoutParams();

        int viewCount = gridLayout.getChildCount();
        param.columnSpec = GridLayout.spec(viewCount % 4);
        param.rowSpec = GridLayout.spec(viewCount / 4);
        int ButtonStyle = R.attr.catButtonStyle;
        Button example = new Button(this, null, ButtonStyle);
        example.setText(name);
        example.setLayoutParams(param);
        gridLayout.addView(example);
    }

    private void prepareItemGrid() {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        if (((GridLayout) gridLayout).getChildCount() > 0)
            ((GridLayout) gridLayout).removeAllViews();
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
        addItem("NewGrid, Really!)");
    }

    public void deleteItem(View v) {
        //TODO Implement logic. Delete this item from list
    }

    public void addItem(View v) {
        //TODO Implement logic. Increment number value
    }

    public void removeItem(View v) {
        //TODO Implement logic. Decrement number value
    }


    class MyViewBinder implements SimpleAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View view, Object data,
                                    String textRepresentation) {
            return true;
        }
    }

}
