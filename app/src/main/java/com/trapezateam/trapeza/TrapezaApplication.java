package com.trapezateam.trapeza;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

public class TrapezaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();


        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
