package com.ezlol.mesh.stableezchat.asynctask;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ezlol.mesh.stableezchat.R;
import com.ezlol.mesh.stableezchat.api.API;
import com.ezlol.mesh.stableezchat.model.Chat;

public class ChatInfoTask extends APIAndUITask<Chat> {
    private final int chatId;

    public ChatInfoTask(Fragment fragment, API api, int chatId) {
        super(fragment, api);
        this.chatId = chatId;
    }

    @Override
    protected Chat doInBackground(Void... voids) {
        return api.getChatById(chatId);
    }

    @Override
    protected void onPostExecute(Chat chat) {
        super.onPostExecute(chat);
        if(chat == null)
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

        TextView toolbarDialogTitle = fragmentView.findViewById(R.id.toolbar_dialog_name);
        toolbarDialogTitle.setText(chat.title);
    }
}
