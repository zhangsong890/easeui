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

public abstract class BaseSendChatRow extends DefaultChatRow {
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

        if (userAvatarView != null) {
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
        }

        if (statusView != null) {
            statusView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemActionListener.onResendClick(message);
                }
            });
        }
    }

    @Override
    protected void onViewSetup(EMMessage message) {
        if (EMClient.getInstance().getOptions().getRequireDeliveryAck()) {
            if (deliveredView != null) {
                if (message.isDelivered()) {
                    deliveredView.setVisibility(View.VISIBLE);
                } else {
                    deliveredView.setVisibility(View.INVISIBLE);
                }
            }
        }
        if (EMClient.getInstance().getOptions().getRequireAck()) {
            if (message.isAcked()) {
                if (deliveredView != null) {
                    deliveredView.setVisibility(View.INVISIBLE);
                }
                if (ackView != null) {
                    ackView.setVisibility(View.VISIBLE);
                }
            } else {
                if (ackView != null) {
                    ackView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}
