package com.trapezateam.trapeza;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.trapezateam.trapeza.api.TrapezaRestClient;
import com.trapezateam.trapeza.api.models.SaveCompleteResponse;
import com.trapezateam.trapeza.database.RealmClient;
import com.trapezateam.trapeza.database.User;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCreationFragment extends AdministratorActivityFragment {

    private static final String TAG = "UserConfFragment";

    public static final String KEY_USER = "user";


    Button mSaveUserButton;
    EditText mUserName, mUserPhone, mUserEmail, mUserSurname, mUserPass;
    RadioGroup mRoleChoice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_creation_fragment, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        mSaveUserButton = (Button) getView().findViewById(R.id.save_user_button);
        mUserName = (EditText) getView().findViewById(R.id.user_name);
        mUserSurname = (EditText) getView().findViewById(R.id.user_surname);
        mUserPhone = (EditText) getView().findViewById(R.id.user_phone);
        mUserEmail = (EditText) getView().findViewById(R.id.user_email);
        mUserPass = (EditText) getView().findViewById(R.id.user_password);
        mRoleChoice = (RadioGroup) getView().findViewById(R.id.radio_group);



        mSaveUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    User user = realm.createObject(User.class);
                    user.setCompanyId(TrapezaApplication.getCompany());
                    user.setName(String.valueOf(mUserName.getText()));
                    user.setSurname(String.valueOf(mUserSurname.getText()));
                    user.setEmail(String.valueOf(mUserEmail.getText()));
                    user.setPhone(String.valueOf(mUserPhone.getText()));
                    user.setRoleFromId(mRoleChoice.getCheckedRadioButtonId());
                    realm.commitTransaction();
                    RealmClient.updateModel(user);
                    saveUser(user);
                    Log.d(TAG, "User saved");
                    returnToMenu();
                }
            }
        });
    }


    private void returnToMenu() {
        getAdministratorActivity().startMenuFragment(true);
    }


    void saveUser(final User user) {

        TrapezaRestClient.UserMethods.create(user, String.valueOf(mUserPass.getText()), new Callback<SaveCompleteResponse>() {
            @Override
            public void onResponse(Call<SaveCompleteResponse> call,
                                   final Response<SaveCompleteResponse> response) {
                Toast toast;
                if (response.body().isSuccess()) {
                    toast = Toast.makeText(getAdministratorActivity(), "Пользователь успешно добавлен", Toast.LENGTH_SHORT);
                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            RealmResults<User> users = realm.where(User.class).equalTo("id", 0).findAll();

                            if (users.size() != 0) {
                                Log.d(TAG, "Everything is ok. oldId is " + users.first().getId() + ". New id is " + response.body().getId());
                                users.first().setId(response.body().getId());
                            }
                        }
                    });
                } else {
                    toast = Toast.makeText(getAdministratorActivity(), "Произошла ошибка, пользователь не добавлен", Toast.LENGTH_SHORT);
                }
                toast.show();

            }

            @Override
            public void onFailure(Call<SaveCompleteResponse> call, Throwable t) {
                Toast.makeText(getAdministratorActivity(), "Error saving user " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                t.printStackTrace();


            }
        });

    }


    public boolean validate() {
        boolean valid = true;
        //TODO write validate();
        return valid;
    }


}
