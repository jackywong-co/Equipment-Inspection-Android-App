package com.example.intelligentequipmentinspectionsystem.responseHandler;

public interface ResponseHandler {
    void onError();

    Boolean onResponse(Boolean login);
}
