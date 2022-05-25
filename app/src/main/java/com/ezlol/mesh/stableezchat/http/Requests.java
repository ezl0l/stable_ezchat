package com.ezlol.mesh.stableezchat.http;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class Requests {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static Response get(String url, Map<String, String> headers) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS).build();
            Headers headersBuild = Headers.of(headers);
            Request request = new Request.Builder().url(url).headers(headersBuild).build();
            okhttp3.Response response = client.newCall(request).execute();

            ResponseBody responseBody = response.body();
            if(responseBody != null)
                return new Response(responseBody.string(), response.code(), response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Response post(String url, String json, Map<String, String> headers){
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(30, TimeUnit.SECONDS).build();

            RequestBody body = RequestBody.create(json, JSON);
            Headers headersBuild = Headers.of(headers);
            Request request = new Request.Builder().url(url).headers(headersBuild).post(body).build();
            okhttp3.Response response = client.newCall(request).execute();

            ResponseBody responseBody = response.body();
            if(responseBody != null)
                return new Response(responseBody.string(), response.code(), response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Response put(String url, String json, Map<String, String> headers) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(30, TimeUnit.SECONDS).build();

            RequestBody body = RequestBody.create(json, JSON);
            Headers headersBuild = Headers.of(headers);
            Request request = new Request.Builder().url(url).headers(headersBuild).put(body).build();
            okhttp3.Response response = client.newCall(request).execute();

            ResponseBody responseBody = response.body();
            if(responseBody != null)
                return new Response(responseBody.string(), response.code(), response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Response post(String url, String json){
        return post(url, json, new HashMap<>());
    }

    public static Response put(String url, String json){
        return put(url, json, new HashMap<>());
    }

    public static Response get(String url){
        return get(url, new HashMap<>());
    }

    public static Response uploadFile(String url, File file, String mimeType, Map<String, String> headers) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(30, TimeUnit.SECONDS).build();

            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(),
                            RequestBody.create(file, MediaType.parse(mimeType)))
                    .build();

            Headers headersBuild = Headers.of(headers);

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .headers(headersBuild)
                    .build();

            okhttp3.Response response = client.newCall(request).execute();

            ResponseBody responseBody = response.body();
            if(responseBody != null)
                return new Response(responseBody.string(), response.code(), response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static okhttp3.Response rawGet(String url) {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url(url)
                .addHeader("Connection", "close")
                .build();

        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

