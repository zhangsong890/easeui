package com.hyphenate.easeui.widget.chatrow2;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

/**
 * Created by zhangsong on 17-12-1.
 */

public abstract class BaseSendChatRow extends BaseChatRow {
    protected ImageView statusView;
    protected TextView ackView;
    protected TextView deliveredView;
    protected ProgressBar progressBar;

    public BaseSendChatRow(Context context) {
        super(context);
    }

    @Override
    protected void onViewInflate(View v) {
        statusView = (ImageView) v.findViewById(com.hyphenate.easeui.R.id.msg_status);
        ackView = (TextView) v.findViewById(com.hyphenate.easeui.R.id.tv_ack);
        deliveredView = (TextView) v.findViewById(com.hyphenate.easeui.R.id.tv_delivered);
        progressBar = (ProgressBar) v.findViewById(com.hyphenate.easeui.R.id.progress_bar);

        userAvatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.onUserAvatarClick(EMClient.getInstance().getCurrentUser());
            }
        });

        userAvatarView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.onUserAvatarLongClick(EMClient.getInstance().getCurrentUser());
                return true;
            }
        });

        statusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemActionListener.onResendClick(message);
            }
        });
    }
}
