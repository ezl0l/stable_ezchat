package com.ezlol.mesh.stableezchat.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ezlol.mesh.stableezchat.R;
import com.ezlol.mesh.stableezchat.api.API;
import com.ezlol.mesh.stableezchat.asynctask.ChatsTask;
import com.ezlol.mesh.stableezchat.model.AccessToken;
import com.ezlol.mesh.stableezchat.model.Chat;
import com.google.gson.Gson;

public class ChatsFragment extends Fragment implements ChatsTask.OnChatClickListener {
    private API api;

    protected ChatsTask.OnChatClickListener onChatClickListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle == null)
            throw new IllegalStateException();

        api = new API(new Gson().fromJson(bundle.getString("accessToken"), AccessToken.class));

        ChatsTask chatsTask = new ChatsTask(this, api);
        chatsTask.setOnChatClickListener(this);
        chatsTask.execute();

        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onChatClick(Chat chat, View view) {
        if(onChatClickListener != null)
            onChatClickListener.onChatClick(chat, view);
    }

    public void setOnChatClickListener(ChatsTask.OnChatClickListener onChatClickListener) {
        this.onChatClickListener = onChatClickListener;
    }
}
