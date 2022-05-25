package com.ezlol.mesh.stableezchat.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ezlol.mesh.stableezchat.R;
import com.ezlol.mesh.stableezchat.Utils;
import com.ezlol.mesh.stableezchat.api.API;
import com.ezlol.mesh.stableezchat.asynctask.ChatMessagesTask;
import com.ezlol.mesh.stableezchat.asynctask.SendMessageTask;
import com.ezlol.mesh.stableezchat.model.AccessToken;
import com.ezlol.mesh.stableezchat.model.Message;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;

import java.util.List;

public class DialogFragment extends Fragment implements View.OnClickListener, GalleryAdapter.PhotoListener {
    private ViewGroup messagesLayout;
    private View uploadFileButton, sendMessageButton, toolbarBackButton;
    private EditText messageEditText;

    private LinearLayout uploadFileBottomSheet;
    private BottomSheetBehavior<LinearLayout> uploadFileBottomSheetBehavior;
    private RecyclerView uploadFileBottomSheetRecyclerView;

    private API api;
    private int chatId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle == null)
            throw new IllegalStateException();

        api = new API(new Gson().fromJson(bundle.getString("accessToken"), AccessToken.class));
        chatId = bundle.getInt("chatId");

        View v = inflater.inflate(R.layout.fragment_dialog, container, false);

        messagesLayout = v.findViewById(R.id.dialogMessagesLayout);
        uploadFileButton = v.findViewById(R.id.uploadFileButton);
        messageEditText = v.findViewById(R.id.messageEditText);
        sendMessageButton = v.findViewById(R.id.sendMessageButton);
        toolbarBackButton = v.findViewById(R.id.toolbar_back);

        uploadFileBottomSheet = v.findViewById(R.id.upload_file_bottom_sheet);
        uploadFileBottomSheetBehavior = BottomSheetBehavior.from(uploadFileBottomSheet);

        uploadFileBottomSheetRecyclerView = v.findViewById(R.id.upload_file_bottom_sheet_recycler_view);

        uploadFileBottomSheetBehavior.setHideable(true);
        uploadFileBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        uploadFileButton.setOnClickListener(this);
        sendMessageButton.setOnClickListener(this);
        toolbarBackButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        new ChatMessagesTask(this, api, chatId).execute();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.sendMessageButton:
                String messageContent = messageEditText.getText().toString();
                if(messageContent.length() > 0) {
                    Message message = new Message(null, null, chatId, messageContent, null, null, (int) System.currentTimeMillis() / 1000);
                    SendMessageTask sendMessageTask = new SendMessageTask(this, api, message);
                    sendMessageTask.execute();

                    messageEditText.setText(null);
                }
                break;
            case R.id.uploadFileButton:
                List<String> images = Utils.listOfImages(getContext());
                GalleryAdapter galleryAdapter = new GalleryAdapter(getContext(), images, this);

                uploadFileBottomSheetRecyclerView.setAdapter(galleryAdapter);
                uploadFileBottomSheetRecyclerView.setHasFixedSize(true);
                uploadFileBottomSheetRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

                uploadFileBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                break;
            case R.id.toolbar_back:
                FragmentActivity activity = getActivity();
                if(activity != null)
                    activity.onBackPressed();
                break;
        }
    }

    @Override
    public void onPhotoClick(String path) {

    }
}
