package com.trapezateam.trapeza;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class TrapezaApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        TrapezaApplication.context = getApplicationContext();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public static Context getAppContext() {
        return TrapezaApplication.context;
    }
}
