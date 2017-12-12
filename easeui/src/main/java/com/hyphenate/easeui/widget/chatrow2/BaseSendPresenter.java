package com.hyphenate.easeui.widget.chatrow2;

import android.content.Context;

import com.hyphenate.chat.EMMessage;

/**
 * Created by zhangsong on 17-12-1.
 */

public abstract class BaseSendPresenter extends ChatRowPresenter {
    public BaseSendPresenter(Context context) {
        super(context);
    }

    @Override
    public void onResendClick(final EMMessage message) {
        super.onResendClick(message);
    }

    @Override
    protected final void handleReceiveMessage(EMMessage message) {
    }

    @Override
    protected void handleSendMessage(final EMMessage message) {
        super.handleSendMessage(message);
    }

    @Override
    protected final void ackMessage(EMMessage message) {
    }
}
