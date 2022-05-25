package com.ezlol.mesh.stableezchat.ui;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.ezlol.mesh.stableezchat.api.API;
import com.google.gson.Gson;

public class FragmentHelper {
    public static Fragment createInstance(Fragment fragment, API api) {
        Bundle bundle = new Bundle();
        bundle.putString("accessToken", new Gson().toJson(api.accessToken));

        fragment.setArguments(bundle);

        return fragment;
    }

    public static Fragment createInstance(Fragment fragment, Context context) {
        Bundle bundle = new Bundle();
        bundle.putString("accessToken", PreferenceManager.getDefaultSharedPreferences(context)
                .getString("accessToken", null));

        fragment.setArguments(bundle);

        return fragment;
    }
}
