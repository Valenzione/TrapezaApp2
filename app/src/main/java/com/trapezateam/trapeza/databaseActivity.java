package com.trapezateam.trapeza;

import android.app.Activity;
import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.trapezateam.trapeza.database.Dish;
import com.trapezateam.trapeza.database.RealmClient;
import com.trapezateam.trapeza.database.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

public class databaseActivity extends Activity {


    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.button)
    Button buttton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        ButterKnife.bind(this);



        RealmClient.createCategory("HotDishes");
        RealmClient.addDish("Hot soup", "Delicious hot soup from Barcelona", 125, 2);
        RealmClient.addDish("Chili soup", "Delicious chili soup from Chili", 650, 2);


        buttton.setOnClickListener(new View.OnClickListener()

                                   {
                                       @Override
                                       public void onClick(View view) {
                                           Realm realm = Realm.getDefaultInstance();
                                           RealmResults<Dish> result = realm.where(Dish.class).findAll();

                                           textView.setText(result.last().getName());
                                           textView2.setText(result.first().getName());
                                       }
                                   }

        );
    }
}