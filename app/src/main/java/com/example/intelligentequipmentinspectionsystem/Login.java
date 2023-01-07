package com.example.intelligentequipmentinspectionsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    EditText username, password, serverURL;
    Button loginButton;
    String refreshToken = "";
    String accessToken = "";
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("In login" + getRefreshToken());
        if (getSeverURL() != ""){
            GlobalVariable.BASE_URL = getSeverURL();
        }
        if (getRefreshToken() != ""){
            System.out.println("Already have token");
            GlobalVariable.refreshToken = getRefreshToken();
            GlobalVariable.accessToken = getAccessToken();
            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);
            Login.this.finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.login_button);
        serverURL = (EditText) findViewById(R.id.serverURL);
        serverURL.setText(GlobalVariable.BASE_URL);

        if (!checkPermission()) {
            System.out.println("requestPermission");
            requestPermission();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariable.BASE_URL = serverURL.getText().toString();
                saveServerURL(serverURL.getText().toString());
                if (username.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(Login.this, "Please Enter Username and Password", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    try {
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("application/json");
                        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"username\": \"" + username.getText().toString() + "\",\r\n    \"password\": \"" + password.getText().toString() + "\"\r\n}");
                        Request request = new Request.Builder()
                                .url(GlobalVariable.BASE_URL + "login/")
                                .method("POST", body)
                                .addHeader("Content-Type", "application/json")
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                System.out.println("onFailure");
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                System.out.println("It went in");
                                if (response.isSuccessful()) {
                                    try {
                                        json = new JSONObject(response.body().string());
                                        System.out.println("refreshToken: " + json.get("refresh") + "\naccessToken: " + json.get("access"));
                                        refreshToken = json.get("refresh").toString();
                                        accessToken = json.get("access").toString();
                                        saveTokens(refreshToken, accessToken);
                                        GlobalVariable.refreshToken = refreshToken;
                                        GlobalVariable.accessToken = accessToken;
                                        saveUsername(username.getText().toString());
//                                    finish();
                                        Intent i = new Intent(Login.this, MainActivity.class);
                                        startActivity(i);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    System.out.println("Wrong username or password");

                                    // needs to be on UI thread to use toast
                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast toast = Toast.makeText(Login.this, "Wrong username or password", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    });
                                }
                            }
                        });

                    } catch (Exception e) {
                        Toast toast = Toast.makeText(Login.this, "Please check serverURL", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }
            }
        });

    }

    private void saveTokens(String refreshToken, String accessToken) {
        SharedPreferences sharedPreferences = getSharedPreferences("TokenPref", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("refreshToken", refreshToken);
        editor.putString("accessToken", accessToken);
        editor.apply();
    }

    private void saveUsername(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("UsernamePref", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    private void saveServerURL(String serverURL) {
        SharedPreferences sharedPreferences = getSharedPreferences("ServerURLPref", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("serverURL", serverURL);
        editor.apply();
    }

    private String getSeverURL() {
        SharedPreferences sharedPreferences = getSharedPreferences("ServerURLPref", 0);
        String severURL = sharedPreferences.getString("serverURL", "");
        return severURL;
    }

    // close keyboard when tap outside
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
    }

    private String getRefreshToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("TokenPref", 0);
        String refreshToken = sharedPreferences.getString("refreshToken", "");
        return refreshToken;
    }

    private String getAccessToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("TokenPref", 0);
        String accessToken = sharedPreferences.getString("accessToken", "");
        return accessToken;
    }
}