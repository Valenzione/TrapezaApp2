package com.trapezateam.trapeza;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.trapezateam.trapeza.api.TrapezaRestClient;
import com.trapezateam.trapeza.api.models.SaveCompleteResponse;
import com.trapezateam.trapeza.database.Company;
import com.trapezateam.trapeza.database.RealmClient;
import com.trapezateam.trapeza.database.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_name)
    EditText mNameText;
    @Bind(R.id.input_email)
    EditText mEmailText;
    @Bind(R.id.input_password)
    EditText mPasswordText;
    @Bind(R.id.btn_signup)
    Button mSignupButton;
    @Bind(R.id.link_login)
    TextView mLoginLink;
    @Bind(R.id.input_login)
    TextView mLogin;
    @Bind(R.id.input_surname)
    TextView mSurname;
    @Bind(R.id.input_comapny_phone)
    TextView mCompanyPhone;
    @Bind(R.id.input_company_adress)
    TextView mCompanyAddress;
    @Bind(R.id.input_company_name)
    TextView mCompanyName;
    @Bind(R.id.input_phone)
    TextView mPhone;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        mLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onValidationFailed();
            return;
        }

        mSignupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = mNameText.getText().toString();
        String surname = mSurname.getText().toString();
        String email = mEmailText.getText().toString();
        String phone = mPhone.getText().toString();
        String password = mPasswordText.getText().toString();
        String companyName = mCompanyName.getText().toString();
        String companyPhone = mCompanyPhone.getText().toString();
        String companyAddress = mCompanyAddress.getText().toString();
        String login = mLogin.getText().toString();

        RealmClient.emptyDatabase();
        User user = RealmClient.createUser(name, surname, email, phone);
        Company company = RealmClient.createCompany(companyName, companyAddress, companyPhone);

        TrapezaRestClient.CompanyMethods.create(company, user, login, password, new Callback<SaveCompleteResponse>() {
            @Override
            public void onResponse(Call<SaveCompleteResponse> call, final Response<SaveCompleteResponse> response) {
                Toast toast;
                if (response.body().isSuccess()) {
                    toast = Toast.makeText(SignupActivity.this, "Компания успешно создана", Toast.LENGTH_SHORT);
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
                    onSignupSuccess();
                } else {
                    toast = Toast.makeText(SignupActivity.this, "Произошла ошибка, компания не создана", Toast.LENGTH_SHORT);
                }
                toast.show();
            }

            @Override
            public void onFailure(Call<SaveCompleteResponse> call, Throwable t) {
                Toast.makeText(SignupActivity.this, "Ошибка сохранения компании " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });

        progressDialog.dismiss();
    }


    public void onSignupSuccess() {
        mSignupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onValidationFailed() {
        Toast.makeText(getBaseContext(), "В некоторые поля введены неверные данные", Toast.LENGTH_LONG).show();
        mSignupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = mNameText.getText().toString();
        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            mNameText.setError("at least 3 characters");
            valid = false;
        } else {
            mNameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailText.setError("enter a valid email address");
            valid = false;
        } else {
            mEmailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPasswordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mPasswordText.setError(null);
        }

        return valid;
    }
}