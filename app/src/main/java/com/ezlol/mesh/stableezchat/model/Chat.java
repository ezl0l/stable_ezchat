package com.ezlol.mesh.stableezchat.model;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.ezlol.mesh.stableezchat.R;


public class Chat {
    public Integer id;
    public String status;
    public String title;
    public Integer time;
    public User[] members;
    public Message last_message;

    public Chat(Integer id, String status, String name, Integer time, User[] members, Message last_message) {
        this.id = id;
        this.status = status;
        this.title = name;
        this.time = time;
        this.members = members;
        this.last_message = last_message;
    }

    public View getView(Context context) {
        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0,
                (int) context.getResources().getDimension(R.dimen.chatLayoutMarginBottom));
        layout.setLayoutParams(params);

        layout.setOrientation(LinearLayout.HORIZONTAL);

        CardView cardView = new CardView(context);
        cardView.setRadius((int) context.getResources().getDimension(R.dimen.chatCardViewRadius));
        cardView.setElevation((int) context.getResources().getDimension(R.dimen.chatCardViewElevation));

        ImageView avatar = new ImageView(context);
        avatar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        avatar.getLayoutParams().width = (int) context.getResources()
                .getDimension(R.dimen.chatAvatarSize);
        avatar.getLayoutParams().height = (int) context.getResources()
                .getDimension(R.dimen.chatAvatarSize);

        avatar.setBackgroundResource(R.mipmap.ic_launcher); // todo
        cardView.addView(avatar);

        layout.addView(cardView);

        LinearLayout chatInfoLayout = new LinearLayout(context);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins((int) context.getResources()
                .getDimension(R.dimen.chatInfoLayoutMargin), 0, 0, 0);
        chatInfoLayout.setLayoutParams(params);
        chatInfoLayout.setOrientation(LinearLayout.VERTICAL);

        TextView chatNameTextView = new TextView(context);
        chatNameTextView.setTypeface(Typeface.DEFAULT_BOLD);
        chatNameTextView.setText(title == null ? "null" : title); // todo
        chatInfoLayout.addView(chatNameTextView);

        LinearLayout chatLastMessageLayout = new LinearLayout(context);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(0, (int) context.getResources()
                .getDimension(R.dimen.chatLastMessageLayoutMargin), 0, 0);
        chatLastMessageLayout.setLayoutParams(params);
        chatLastMessageLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView chatLastMessageTextView = new TextView(context);
        chatLastMessageTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        chatLastMessageTextView.setEllipsize(TextUtils.TruncateAt.END);
        chatLastMessageTextView.setMaxLines(1);

        TextView chatLastMessageUsernameTextView = new TextView(context);
        chatLastMessageUsernameTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));


        if(last_message != null) {
            String username = null;
            if(members != null && members.length > 2) {
                for (User user : members) {
                    if (user.id == last_message.user_id)
                        username = user.username + ": ";
                }
            }
            chatLastMessageUsernameTextView.setText(username);
            if(last_message.content.length() > 0)
                chatLastMessageTextView.setText(last_message.content);
            else {
                Attachment[] attachments = Attachment.parseMany(last_message.attachment);
                if(attachments.length > 0) {
                    chatLastMessageTextView.setText(R.string.attachment);
                }
            }
        }

        chatLastMessageLayout.addView(chatLastMessageUsernameTextView);
        chatLastMessageLayout.addView(chatLastMessageTextView);
        chatInfoLayout.addView(chatLastMessageLayout);

        layout.addView(chatInfoLayout);

        return layout;
    }
}
