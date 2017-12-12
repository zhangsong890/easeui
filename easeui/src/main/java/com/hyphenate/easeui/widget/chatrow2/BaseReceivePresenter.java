package com.hyphenate.easeui.widget.chatrow2;

import android.content.Context;

import com.hyphenate.chat.EMMessage;

/**
 * Created by zhangsong on 17-12-1.
 */

public abstract class BaseReceivePresenter extends ChatRowPresenter {
    public BaseReceivePresenter(Context context) {
        super(context);
    }

    @Override
    protected void handleReceiveMessage(EMMessage message) {
    }

    @Override
    protected final void handleSendMessage(EMMessage message) {
    }

    @Override
    public final void onResendClick(EMMessage message) {
    }

    @Override
    protected void ackMessage(EMMessage message) {
        super.ackMessage(message);
    }
}
