package com.trapezateam.trapeza.database;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Yuriy on 7/5/2016.
 */
public class Company extends RealmObject {
    @PrimaryKey
    private int companyId;
    private String companyName;
    private RealmList<User> stuff;

    public Company() {
    }

    public RealmList<User> getStuff() {
        return stuff;
    }

    public void setStuff(RealmList<User> stuff) {
        this.stuff = stuff;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public void addToStuff(User u) {
        if (!stuff.contains(u)) {
            stuff.add(u);
        }

    }
}
