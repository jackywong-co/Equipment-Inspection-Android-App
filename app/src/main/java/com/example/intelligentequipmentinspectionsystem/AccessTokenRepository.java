package com.example.intelligentequipmentinspectionsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AccessTokenRepository {


    public AccessTokenRepository() {
    }

    public String getAccessToken() {
        return GlobalVariable.accessToken;
    }

//    public Boolean loginAndSetTokens(String username, String password, Context context) {
//        Boolean login = false;
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .build();
//        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"username\": \"" + username + "\",\r\n    \"password\": \"" + password + "\"\r\n}");
//        Request request = new Request.Builder()
//                .url(GlobalVariable.BASE_URL + "login/")
//                .method("POST", body)
//                .addHeader("Content-Type", "application/json")
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                System.out.println("onFailure");
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                System.out.println("It went in");
//                if (response.isSuccessful()) {
//                    try {
//                        String refreshToken;
//                        String accessToken;
//                        JSONObject json = new JSONObject(response.body().string());
//                        System.out.println("refreshToken: " + json.get("refresh") + "\naccessToken: " + json.get("access"));
//                        refreshToken = json.get("refresh").toString();
//                        accessToken = json.get("access").toString();
//                        saveTokens(refreshToken, accessToken, context);
//                        GlobalVariable.refreshToken = refreshToken;
//                        GlobalVariable.accessToken = accessToken;
//                        Handler handler = new Handler(Looper.getMainLooper());
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//
//                            }
//                        });
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    System.out.println("Wrong username or password");
//                    // needs to be on UI thread to use toast
//                    Handler handler = new Handler(Looper.getMainLooper());
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast toast = Toast.makeText(context, "Wrong username or password", Toast.LENGTH_SHORT);
//                            toast.show();
//                        }
//                    });
//                }
//            }
//        });
//    }

    public String refreshAccessToken() {
        String refreshToken = GlobalVariable.refreshToken;
        String accessToken = "";

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"refresh\": \"" + refreshToken + "\"\r\n}");
        ;
        Request request = new Request.Builder()
                .url(GlobalVariable.BASE_URL +"refresh/")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject object = new JSONObject(response.body().string());
            // Get value from that JSONObject
            accessToken = object.getString("access");
            System.out.println("setting refreshTokenFailed to false");
            GlobalVariable.refreshTokenFailed = false;
        } catch (IOException | JSONException ioException) {
            System.out.println("Refresh Fail: ");
            System.out.println(ioException);
            System.out.println("setting refreshTokenFailed to true");
            GlobalVariable.refreshTokenFailed = true;
        }
        GlobalVariable.accessToken = accessToken;
        return accessToken;
    }

//    private void saveTokens(String refreshToken, String accessToken, Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences("TokenPref", 0);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("refreshToken", refreshToken);
//        editor.putString("accessToken", accessToken);
//        editor.apply();
//    }
}
