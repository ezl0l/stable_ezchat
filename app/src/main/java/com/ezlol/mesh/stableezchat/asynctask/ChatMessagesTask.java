package com.ezlol.mesh.stableezchat.asynctask;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;

import com.ezlol.mesh.stableezchat.R;
import com.ezlol.mesh.stableezchat.api.API;
import com.ezlol.mesh.stableezchat.model.Message;

public class ChatMessagesTask extends APIAndUITask<Message[]> {
    private final int chatId;

    public ChatMessagesTask(Fragment fragment, API api, int chatId) {
        super(fragment, api);
        this.chatId = chatId;
    }

    @Override
    protected Message[] doInBackground(Void... voids) {
        Message[] messages = null;
        for(int i = 0; i < 5 && messages == null; i++) {
            messages = api.getMessagesByChatId(chatId);  // костыль, там почему-то окхттп кидает исключение с "unexpected end of stream" ;/
        }
        return messages;
    }

    @Override
    protected void onPostExecute(Message[] messages) {
        super.onPostExecute(messages);
        if(messages == null)
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

        ViewGroup messagesLayout = fragmentView.findViewById(R.id.dialogMessagesLayout);

        View messageView;
        for(Message message : messages) {
            messageView = message.getView(fragment.getContext(),
                    message.user_id == api.accessToken.user_id);
            if(messageView == null)
                continue;
            messagesLayout.addView(messageView);
        }
        ScrollView scrollView = fragmentView.findViewById(R.id.dialogMessagesScrollView);
        if(scrollView == null)
            return;

        scrollView.post(() -> {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            messagesLayout.setVisibility(View.VISIBLE);
        });
    }
}
