package com.ezlol.mesh.stableezchat.asynctask;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ezlol.mesh.stableezchat.R;
import com.ezlol.mesh.stableezchat.api.API;
import com.ezlol.mesh.stableezchat.model.Chat;


public class ChatsTask extends APIAndUITask<Chat[]> {
    protected OnChatClickListener onChatClickListener;

    public ChatsTask(Fragment fragment, API api) {
        super(fragment, api);
        Log.d(getClass().getSimpleName(), "constructor");
    }

    @Override
    protected Chat[] doInBackground(Void... voids) {
        Chat[] chats = null;
        for(int i = 0; i < 5 && chats == null; i++) {
            chats = getApi().getChats();
        }
        return chats;
    }

    @Override
    protected void onPostExecute(Chat[] chats) {
        super.onPostExecute(chats);
        if(chats == null)
            return;  // todo

        Fragment fragment = getFragmentWeakReference().get();
        if(fragment == null) {
            Log.e(getClass().getSimpleName(), "Fragment weak reference is null");
            return;
        }

        View fragmentView = fragment.getView();
        if(fragmentView == null) {
            Log.e(getClass().getSimpleName(), "Fragment view weak reference is null");
            return;
        }

        ViewGroup chatsLayout = fragmentView.findViewById(R.id.chats);

        View view;
        for(Chat chat : chats) {
            view = chat.getView(fragment.getContext());
            if(view == null)
                continue;

            if(onChatClickListener != null)
                view.setOnClickListener((chatView) -> onChatClickListener.onChatClick(chat, chatView));

            chatsLayout.addView(view);
        }
    }

    public void setOnChatClickListener(OnChatClickListener onChatClickListener) {
        this.onChatClickListener = onChatClickListener;
    }
}
