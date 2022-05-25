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
import com.ezlol.mesh.stableezchat.asynctask.OnChatClickListener;
import com.ezlol.mesh.stableezchat.model.AccessToken;
import com.ezlol.mesh.stableezchat.model.Chat;
import com.google.gson.Gson;

public class ChatsFragment extends Fragment implements OnChatClickListener, View.OnClickListener {
    private API api;

    protected OnChatClickListener onChatClickListener;
    protected OnToolbarClickListener onToolbarClickListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle == null)
            throw new IllegalStateException();

        api = new API(new Gson().fromJson(bundle.getString("accessToken"), AccessToken.class));

        View v = inflater.inflate(R.layout.fragment_chats, container, false);

        View toolbarUserSearchButton = v.findViewById(R.id.userSearchToolbarButton);
        toolbarUserSearchButton.setOnClickListener(this);

        ChatsTask chatsTask = new ChatsTask(this, api);
        chatsTask.setOnChatClickListener(this);
        chatsTask.execute();

        return v;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.userSearchToolbarButton && onToolbarClickListener != null)
            onToolbarClickListener.onUserSearchButtonClick(view);
    }

    @Override
    public void onChatClick(Chat chat, View view) {
        if(onChatClickListener != null)
            onChatClickListener.onChatClick(chat, view);
    }

    public void setOnChatClickListener(OnChatClickListener onChatClickListener) {
        this.onChatClickListener = onChatClickListener;
    }

    public void setOnToolbarClickListener(OnToolbarClickListener onToolbarClickListener) {
        this.onToolbarClickListener = onToolbarClickListener;
    }
}
