package com.hyphenate.easeui.widget.chatrow2;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Created by zhangsong on 17-12-1.
 */

public abstract class BaseReceiveChatRow extends BaseChatRow {
    protected TextView userNickView;

    public BaseReceiveChatRow(Context context) {
        super(context);
    }

    @Override
    protected void onViewInflate(View v) {
        userNickView = (TextView) v.findViewById(com.hyphenate.easeui.R.id.tv_userid);
    }
}
