package com.ezlol.mesh.stableezchat.asynctask;

import android.os.AsyncTask;

import androidx.fragment.app.Fragment;

import com.ezlol.mesh.stableezchat.api.API;

import java.lang.ref.WeakReference;

public abstract class APIAndUITask<T> extends AsyncTask<Void, Void, T> {
    protected final WeakReference<Fragment> fragmentWeakReference;
    protected API api;

    public APIAndUITask(Fragment fragment, API api) {
        this.fragmentWeakReference = new WeakReference<>(fragment);
        this.api = api;
    }

    public API getApi() {
        return api;
    }

    public WeakReference<Fragment> getFragmentWeakReference() {
        return fragmentWeakReference;
    }
}
