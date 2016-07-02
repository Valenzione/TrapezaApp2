package com.trapezateam.trapeza;

import android.app.ProgressDialog;
import android.content.Intent;
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
            onLoginSuccess(DebugConfig.DEFAULT_TOKEN,0);
        }
    }

    public void login() {


//        if (!validate()) {
//            onLoginFailed();
//            return;
//        }

        mLoginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();

        Log.d(TAG, "Login");

        TrapezaRestClient.authenticate(email, password, new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(Call<AuthenticationResponse> call,
                                   Response<AuthenticationResponse> response) {
                AuthenticationResponse body = response.body();
                if (!body.isSuccess()) {
                    onLoginFailed(body.getMessage());
                }
                Log.d(TAG, body.toString());
                progressDialog.dismiss();
                onLoginSuccess(body.getToken(), body.getId());

            }

            @Override
            public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                t.printStackTrace();
                onLoginFailed("Network error " + t.getMessage());
                progressDialog.dismiss();
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
        // Disable going back to the MainActivity
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

        startActivity(intent);
    }

    public void onLoginSuccess(String token, int userId) {
        int role = 0;
        mLoginButton.setEnabled(true);
        TrapezaRestClient.setToken(token);
        Log.i(TAG, "Token " + token);
        TrapezaRestClient.userInfo(userId, new Callback<List<UserResponse>>() {
            @Override
            public void onResponse(Call<List<UserResponse>> call, Response<List<UserResponse>> response) {
                Log.i(TAG, "Response");
                startCorrectActivity(response.body().get(0).getRole());
            }

            @Override
            public void onFailure(Call<List<UserResponse>> call, Throwable t) {
                Log.i(TAG, "Oh shit "+t.getMessage());
            }

        });
    }

    public void onLoginFailed(String message) {
        Toast.makeText(getBaseContext(), "Login failed: " + message, Toast.LENGTH_LONG).show();
        mLoginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailText.setError("enter a valid email address");
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.input_email));
            valid = false;
        } else {
            mEmailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPasswordText.setError("between 4 and 10 alphanumeric characters");
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.input_password));
            valid = false;
        } else {
            mPasswordText.setError(null);
        }

        return valid;
    }
}
