package com.trapezateam.trapeza;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.trapezateam.trapeza.api.TrapezaRestClient;
import com.trapezateam.trapeza.api.models.AuthenticationResponse;
import com.trapezateam.trapeza.api.models.UserResponse;
import com.trapezateam.trapeza.database.RealmClient;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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

        SharedPreferencesHelper helper = new SharedPreferencesHelper(this);
        if (helper.hasSavedProfile()) {
            TrapezaRestClient.setToken(helper.getToken());
            onLoginSuccess(helper.getToken(), helper.getUserId());
            return;
        }


        if (DebugConfig.USE_DEFAULT_TOKEN) {
            TrapezaRestClient.setToken(DebugConfig.DEFAULT_TOKEN);
            onLoginSuccess(DebugConfig.DEFAULT_TOKEN, 0);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

                Handler h = new Handler();
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

    public void onLoginSuccess(final String token, final int userId) {
        mLoginButton.setEnabled(true);
        TrapezaRestClient.setToken(token);
        Log.i(TAG, "Token " + token);
        final SharedPreferencesHelper helper = new SharedPreferencesHelper(this);
        TrapezaRestClient.UserMethods.get(userId, new Callback<List<UserResponse>>() {
            @Override
            public void onResponse(Call<List<UserResponse>> call, Response<List<UserResponse>> response) {
                if (response.body().size() == 0) {
                    onFailure(call, new Throwable("Response body has no rows"));
                    return;
                }
                TrapezaApplication.setCompany(response.body().get(0).getCompany());
                RealmClient.updateDatabase(TrapezaApplication.getCompany());
                int role = response.body().get(0).getRole();
                helper.saveRole(role);
                helper.saveToken(token);
                helper.saveUserId(userId);
                startCorrectActivity(role);
            }

            @Override
            public void onFailure(Call<List<UserResponse>> call, Throwable t) {
                Log.i(TAG, "Error Getting User Info. Error " + t.getMessage());
                helper.removeToken();
            }

        });
    }

    public void onLoginFailed(String message) {
        Toast.makeText(getBaseContext(), "Login failed: " + message, Toast.LENGTH_LONG).show();
        mLoginButton.setEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.trapezateam.trapeza/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.trapezateam.trapeza/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
