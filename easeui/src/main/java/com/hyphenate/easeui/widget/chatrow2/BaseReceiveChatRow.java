package com.hyphenate.easeui.widget.chatrow2;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Created by zhangsong on 17-12-1.
 */

public abstract class BaseReceiveChatRow extends DefaultChatRow {
    protected TextView userNickView;

    public BaseReceiveChatRow(Context context) {
        super(context);
    }

    @Override
    protected void onViewInflate(View v) {
        userNickView = (TextView) v.findViewById(com.hyphenate.easeui.R.id.tv_userid);

        if (userAvatarView == null) return;

        userAvatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.onUserAvatarClick(message.getFrom());
            }
        });

        userAvatarView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.onUserAvatarLongClick(message.getFrom());
                return true;
            }
        });
    }
}
