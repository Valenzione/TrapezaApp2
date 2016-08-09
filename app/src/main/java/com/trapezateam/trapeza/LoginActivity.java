package com.trapezateam.trapeza;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.trapezateam.trapeza.api.TrapezaRestClient;
import com.trapezateam.trapeza.api.models.AuthenticationResponse;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.trapezateam.trapeza.api.models.UserResponse;
import com.trapezateam.trapeza.database.RealmClient;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @Bind(R.id.input_email)
    EditText mEmailText;
    @Bind(R.id.input_password)
    EditText mPasswordText;
    @Bind(R.id.btn_login)
    Button mLoginButton;
    @Bind(R.id.link_signup)
    TextView mSignupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        mSignupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });


        if (DebugConfig.USE_DEFAULT_TOKEN) {
            TrapezaRestClient.setToken(DebugConfig.DEFAULT_TOKEN);
            onLoginSuccess(DebugConfig.DEFAULT_TOKEN, 0);
        }
        mEmailText.setText("cashier");
        mPasswordText.setText("cashier");
    }

    public void login() {


        mLoginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();


        String email = mEmailText.getText().toString();
        final String password = mPasswordText.getText().toString();

        Log.d(TAG, "Login");

        TrapezaRestClient.authenticate(email, password, new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(Call<AuthenticationResponse> call,
                                   Response<AuthenticationResponse> response) {
                AuthenticationResponse body = response.body();
                if (!body.isSuccess()) {
                    onLoginFailed(body.getMessage());
                } else {
                    onLoginSuccess(body.getToken(), body.getId());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                t.printStackTrace();

                android.os.Handler h = new android.os.Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 1500);

                onLoginFailed("Network error " + t.getMessage());
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void startCorrectActivity(int role) {
        Intent intent = null;
        switch (role) {
            case 0:
                intent = new Intent(this, CashierActivity.class);
                break;
            case 1:
                intent = new Intent(this, AdministratorActivity.class);
                break;
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void onLoginSuccess(String token, int userId) {
        int role = 0;
        mLoginButton.setEnabled(true);
        TrapezaRestClient.setToken(token);
        Log.i(TAG, "Token " + token);
        TrapezaRestClient.UserMethods.get(userId, new Callback<List<UserResponse>>() {
            @Override
            public void onResponse(Call<List<UserResponse>> call, Response<List<UserResponse>> response) {
                TrapezaApplication.setCompany(response.body().get(0).getCompany());
                RealmClient.updateDatabase(TrapezaApplication.getCompany());
                startCorrectActivity(response.body().get(0).getRole());
            }

            @Override
            public void onFailure(Call<List<UserResponse>> call, Throwable t) {
                Log.i(TAG, "Error Getting User Info. Error " + t.getMessage());
            }

        });
    }

    public void onLoginFailed(String message) {
        Toast.makeText(getBaseContext(), "Login failed: " + message, Toast.LENGTH_LONG).show();
        mLoginButton.setEnabled(true);
    }

}
