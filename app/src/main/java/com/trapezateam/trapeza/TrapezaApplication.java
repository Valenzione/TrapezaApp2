package com.trapezateam.trapeza;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class TrapezaApplication extends Application {

    private static Context context;
    private static int mCompanyId;

    public static void setCompany(int companyId) {
        TrapezaApplication.mCompanyId = companyId;
    }

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

    public static int getCompany() {
        return mCompanyId;
    }
}
