package com.ezlol.mesh.stableezchat.http;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class Response {
    private final String string;
    private final int statusCode;
    private final okhttp3.Response responseBody;

    public Response(String string, int statusCode, okhttp3.Response responseBody) {
        this.string = string;
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public okhttp3.Response getResponseBody() {
        return responseBody;
    }

    public JSONObject json() throws JSONException {
        return new JSONObject(string);
    }

    public int getStatusCode() {
        return statusCode;
    }

    @NonNull
    @Override
    public String toString() {
        return string;
    }
}
