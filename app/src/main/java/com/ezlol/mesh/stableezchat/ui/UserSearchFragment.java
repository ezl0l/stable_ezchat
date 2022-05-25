package com.ezlol.mesh.stableezchat.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ezlol.mesh.stableezchat.R;
import com.ezlol.mesh.stableezchat.api.API;
import com.ezlol.mesh.stableezchat.asynctask.OnChatClickListener;
import com.ezlol.mesh.stableezchat.asynctask.UserSearchTask;
import com.ezlol.mesh.stableezchat.model.AccessToken;
import com.ezlol.mesh.stableezchat.model.Chat;
import com.google.gson.Gson;

public class UserSearchFragment extends Fragment implements TextWatcher, OnChatClickListener {
    private EditText searchLineEditText;
    private ViewGroup resultsLayout;
    private TextView titleNothingFoundTextView;

    private API api;

    private OnChatClickListener onChatClickListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle == null)
            throw new IllegalStateException();

        api = new API(new Gson().fromJson(bundle.getString("accessToken"), AccessToken.class));

        View v = inflater.inflate(R.layout.fragment_user_search, container, false);

        searchLineEditText = v.findViewById(R.id.userSearchLineEditText);
        resultsLayout = v.findViewById(R.id.userSearchResultsLayout);
        titleNothingFoundTextView = v.findViewById(R.id.title_nothing_found_textview);

        searchLineEditText.addTextChangedListener(this);

        return v;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        resultsLayout.removeAllViews();
        titleNothingFoundTextView.setVisibility(View.GONE);
        if(charSequence.length() > 2) {
            UserSearchTask userSearchTask = new UserSearchTask(this, api, charSequence.toString());
            userSearchTask.setOnChatClickListener(this);
            userSearchTask.execute();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onChatClick(Chat chat, View view) {
        Log.d("chat click", "*click* and " + (onChatClickListener == null));
        if(onChatClickListener != null)
            onChatClickListener.onChatClick(chat, view);
    }

    public void setOnChatClickListener(OnChatClickListener onChatClickListener) {
        this.onChatClickListener = onChatClickListener;
    }
}
