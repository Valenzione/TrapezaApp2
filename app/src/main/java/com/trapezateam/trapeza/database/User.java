package com.trapezateam.trapeza.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.trapezateam.trapeza.R;
import com.trapezateam.trapeza.api.models.UserResponse;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Yuriy on 7/5/2016.
 */
public class User extends RealmObject implements Parcelable {
    @PrimaryKey
    private int id;
    private String name;
    private String email;
    private int companyId;
    private String photoURL;
    private int role;
    private String surname;
    private String phone;


    public User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        email = in.readString();
        companyId = in.readInt();
        photoURL = in.readString();
        role = in.readInt();
        surname = in.readString();
        phone = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User() {

    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getFullName() {
        return surname + " " + name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String toString() {
        String out = this.getFullName() + System.lineSeparator()
                + "Email: " + this.getEmail() + "; Phone: " + this.getPhone();
        return out;
    }

    public void setData(UserResponse userResponse) {
        id = userResponse.getId();
        name = userResponse.getName();
        surname = userResponse.getSurname();
        email = userResponse.getEmail();
        companyId = getCompanyId();
        role = userResponse.getRole();
        phone = userResponse.getPhone();
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setRoleFromId(int resourceId) {
        switch (resourceId) {
            case R.id.cashier_radio_button:
                this.role = 0;
                break;
            case R.id.admin_radio_button:
                this.role = 1;
                break;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeInt(companyId);
        parcel.writeString(photoURL);
        parcel.writeInt(role);
        parcel.writeString(surname);
        parcel.writeString(phone);
    }
}
