package com.example.intelligentequipmentinspectionsystem;

import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class JWTUtils {
    public static void decoded(String JWTEncoded) throws Exception {
        try {
            String[] split = JWTEncoded.split("\\.");
            Log.d("JWT_DECODED", "Header: " + getJson(split[0]));
            Log.d("JWT_DECODED", "Body: " + getJson(split[1]));
            JSONObject jsonObject = new JSONObject(getJson(split[1]));
            GlobalVariable.userId = jsonObject.getString("user_id");
            System.out.println("GlobalVariable.userId: "+GlobalVariable.userId);
        } catch (UnsupportedEncodingException e) {
            //Error
        }
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }
}
