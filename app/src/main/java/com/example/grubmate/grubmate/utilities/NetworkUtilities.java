package com.example.grubmate.grubmate.utilities;

import android.util.Log;

import java.io.IOException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by tianhangliu on 10/1/17.
 *
 * This class encapsulate OkHttp to provide easy way to send/request data
 */

public class NetworkUtilities {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static OkHttpClient client = new OkHttpClient();

    // Send a GET request to address specified by url, return raw response text
    public static String get(String url) throws IOException {
        if(url==null||url.length() == 0) {
            return null;
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();

    }

    // Send a POST request to address specified by url, return raw response text
    public static String post(String url, String jsonBody) throws IOException {
        if(url==null||url.length() == 0) {
            return null;
        }
        RequestBody body = RequestBody.create(JSON, jsonBody);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();

    }

    // Send a PUT request to address specified by url, return raw response text
    public static String put(String url, String jsonBody) throws  IOException {
        if(url==null||url.length() == 0) {
            return null;
        }
        RequestBody body = RequestBody.create(JSON, jsonBody);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    // Send a DELETE request to address specified by url, return raw response text
    public static String delete(String url, String jsonBody) throws  IOException {
        if(url==null||url.length() == 0) {
            return null;
        }
        RequestBody body = jsonBody == null?null:RequestBody.create(JSON, jsonBody);
        Request request = new Request.Builder()
                .url(url)
                .delete(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
