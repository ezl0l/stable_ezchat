package com.ezlol.mesh.stableezchat.model;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class Attachment {
    public interface OnAttachmentOpening {
        void onOpening(Attachment attachment, String attachmentFile);
    }

    public int id;
    public int user_id;
    public String orig_name;
    public String type;
    public String hash;
    public String url;
    public int time;

    private String attachmentFile;

    private OnAttachmentOpening listener;

    public Attachment(int id, int user_id, String orig_name, String type, String hash, int time) {
        this.id = id;
        this.user_id = user_id;
        this.orig_name = orig_name;
        this.type = type;
        this.hash = hash;
        this.time = time;
    }

    public static Attachment[] parseMany(String attachments) {
        List<Attachment> attachmentList = new ArrayList<>();

        int attachmentId, attachmentUserId;
        String attachmentType;
        String[] attachmentSplitted;

        for(String attachment : attachments.split(",")) {
            attachmentSplitted = attachment.split("_");

            attachmentType = attachmentSplitted[0];
            attachmentUserId = Integer.parseInt(attachmentSplitted[1]);
            attachmentId = Integer.parseInt(attachmentSplitted[2]);

            attachmentList.add(new Attachment(attachmentId, attachmentUserId, null, attachmentType, null, 0));
        }
        return attachmentList.toArray(new Attachment[]{});
    }

    public static Attachment parseOne(String attachment) {
        return parseMany(attachment)[0];
    }

    public View getPlaceholderView(Context context) {
        switch(type) {
            case "photo": {
                ImageView imageView = new ImageView(context);
                imageView.setBackgroundColor(Color.WHITE);

                return imageView;
            }

            default: {
                throw new IllegalStateException("Yet not implemented");
            }
        }
    }

    public View getView(Context context, String attachmentFile, OnAttachmentOpening onAttachmentOpeningListener) {
        this.attachmentFile = attachmentFile;
        this.listener = onAttachmentOpeningListener;

        View view = null;
        switch(type) {
            case "audio": {
                LinearLayout layout = (LinearLayout) getPlaceholderView(context);
                layout.setOnClickListener(view1 -> listener.onOpening(Attachment.this, Attachment.this.attachmentFile));

                view = layout;
                break;
            }

            case "photo": {
                ImageView imageView = (ImageView) getPlaceholderView(context);
                imageView.setImageBitmap(BitmapFactory.decodeFile(attachmentFile));
                imageView.setOnClickListener(view1 -> listener.onOpening(Attachment.this, Attachment.this.attachmentFile));

                view = imageView;
                break;
            }

            default: {
                throw new IllegalStateException("Yet not implemented");
            }
        }

        return view;
    }
}
