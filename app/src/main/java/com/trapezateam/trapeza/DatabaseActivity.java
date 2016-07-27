package com.trapezateam.trapeza;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.trapezateam.trapeza.database.Category;
import com.trapezateam.trapeza.database.Dish;
import com.trapezateam.trapeza.database.RealmClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.*;


public class DatabaseActivity extends Activity {


    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.button)
    Button buttton;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.listView2)
    ListView listView2;
    @Bind(R.id.button3)
    Button buttton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        ButterKnife.bind(this);

        buttton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm realm = Realm.getDefaultInstance();
                textView.setText(String.valueOf(realm.where(Dish.class).findAll().size()));
                textView2.setText(String.valueOf(realm.where(Category.class).findAll().size()));
            }
        });
    }
}