package com.ezlol.mesh.stableezchat.asynctask;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ezlol.mesh.stableezchat.R;
import com.ezlol.mesh.stableezchat.api.API;
import com.ezlol.mesh.stableezchat.model.Chat;
import com.ezlol.mesh.stableezchat.model.User;

public class UserSearchTask extends APIAndUITask<User[]> {
    private final String username;

    private OnChatClickListener onChatClickListener;

    public UserSearchTask(Fragment fragment, API api, String username) {
        super(fragment, api);
        this.username = username;
    }

    @Override
    protected User[] doInBackground(Void... voids) {
        User[] users = null;
        for(int i = 0; i < 5 && users == null; i++) {
            users = api.usersSearch(username);
        }
        return users;
    }

    @Override
    protected void onPostExecute(User[] users) {
        super.onPostExecute(users);
        if(users == null)
            return;

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

        if(users.length == 0) {
            View titleNothing = fragmentView.findViewById(R.id.title_nothing_found_textview);
            if(titleNothing != null)
                titleNothing.setVisibility(View.VISIBLE);
            return;
        }

        ViewGroup resultsLayout = fragmentView.findViewById(R.id.userSearchResultsLayout);
        resultsLayout.removeAllViews();

        for(User user : users) {
            Chat chat = new Chat(null, null, user.username, null, null, null);
            View chatView = chat.getView(fragment.getContext());
            if(chatView == null)
                continue;

            if(onChatClickListener != null)
                chatView.setOnClickListener((view) -> onChatClickListener.onChatClick(chat, chatView));

            resultsLayout.addView(chatView);
        }
    }

    public void setOnChatClickListener(OnChatClickListener onChatClickListener) {
        this.onChatClickListener = onChatClickListener;
    }
}
