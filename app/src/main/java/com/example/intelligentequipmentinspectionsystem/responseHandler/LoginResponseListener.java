package com.example.intelligentequipmentinspectionsystem.responseHandler;

public class LoginResponseListener implements  ResponseHandler{
    @Override
    public void onError() {

    }

    @Override
    public Boolean onResponse(Boolean login) {
            return login;
    }
}
