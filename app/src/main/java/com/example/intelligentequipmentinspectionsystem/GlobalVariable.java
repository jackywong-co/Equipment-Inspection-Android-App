package com.example.intelligentequipmentinspectionsystem;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariable {
    public static String BASE_URL = "http://10.0.2.2:8000/";
    public static List<Question> globalQuestions;
    public static String userId = "";
    public static String refreshToken = "";
    public static String accessToken = "";
    public static boolean refreshTokenFailed = false;
    public static boolean backPressed = false;
}
