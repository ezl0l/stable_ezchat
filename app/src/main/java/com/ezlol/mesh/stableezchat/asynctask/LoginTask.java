package com.ezlol.mesh.stableezchat.asynctask;

import android.preference.PreferenceManager;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.ezlol.mesh.stableezchat.api.API;
import com.google.gson.Gson;

public class LoginTask extends APIAndUITask<Void> {
    public interface OnFinishLoginListener {
        void onFinishLogin(API api);
    }

    private final String username, password;

    private OnFinishLoginListener onFinishLoginListener;

    public LoginTask(Fragment fragment, API api, String username, String password) {
        super(fragment, api);
        this.username = username;
        this.password = password;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            api = new API(username, password);
        } catch (API.AuthException e) {
            Log.e(getClass().getSimpleName(), "Auth failed");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        if(api != null)
            PreferenceManager.getDefaultSharedPreferences(fragmentWeakReference.get().getContext())
                    .edit()
                    .putString("accessToken", new Gson().toJson(api.accessToken))
                    .apply();
        if(onFinishLoginListener != null)
            onFinishLoginListener.onFinishLogin(api);
    }

    public void setOnFinishLoginListener(OnFinishLoginListener onFinishLoginListener) {
        this.onFinishLoginListener = onFinishLoginListener;
    }
}
