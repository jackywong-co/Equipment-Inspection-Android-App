package com.example.intelligentequipmentinspectionsystem;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AccessTokenInterceptor implements Interceptor {
    private AccessTokenRepository accessTokenRepository;

    public AccessTokenInterceptor(AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

    public AccessTokenInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String accessToken = GlobalVariable.accessToken;
        Request request = newRequestWithAccessToken(chain.request(), accessToken);
        Response response = chain.proceed(request);

        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            synchronized (this) {
                final String newAccessToken = GlobalVariable.accessToken;
                // Access token is refreshed in another thread.
                if (!accessToken.equals(newAccessToken)) {
                    return chain.proceed(newRequestWithAccessToken(request, newAccessToken));
                }

                // Need to refresh an access token
                final String updatedAccessToken = accessTokenRepository.refreshAccessToken();
                // Retry the request
                return chain.proceed(newRequestWithAccessToken(request, updatedAccessToken));
            }
        }

        return response;
    }

    @NonNull
    private Request newRequestWithAccessToken(@NonNull Request request, @NonNull String accessToken) {
        return request.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();
    }
}
