package com.ezlol.mesh.stableezchat.asynctask;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;

import com.ezlol.mesh.stableezchat.R;
import com.ezlol.mesh.stableezchat.api.API;
import com.ezlol.mesh.stableezchat.model.Message;

public class SendMessageTask extends APIAndUITask<Message> {
    private final Message message;

    public SendMessageTask(Fragment fragment, API api, Message message) {
        super(fragment, api);
        this.message = message;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
        messagesLayout.addView(message.getView(fragment.getContext(), true));
    }

    @Override
    protected Message doInBackground(Void... voids) {
        return api.messagesSend(message.chat_id, message.content, message.attachment);
    }

    @Override
    protected void onPostExecute(Message message) {
        super.onPostExecute(message);

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

        ScrollView scrollView = fragmentView.findViewById(R.id.dialogMessagesScrollView);
        ViewGroup messagesLayout = fragmentView.findViewById(R.id.dialogMessagesLayout);
        if(scrollView == null || messagesLayout == null)
            return;

        scrollView.post(() -> {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            messagesLayout.setVisibility(View.VISIBLE);
        });
    }
}
