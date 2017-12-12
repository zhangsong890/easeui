package com.hyphenate.easeui.widget.chatrow2.text;

import android.content.Context;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow2.BaseReceivePresenter;
import com.hyphenate.easeui.widget.chatrow2.DefaultChatRow;

/**
 * Created by zhangsong on 17-11-30.
 */

public class TextReceivePresenter extends BaseReceivePresenter {
    public TextReceivePresenter(Context context) {
        super(context);
    }

    @Override
    protected DefaultChatRow onCreateChatRow(Context context) {
        return new TextReceiveChatRow(context);
    }

    @Override
    public void onBubbleClick(EMMessage message) {
    }

    @Override
    protected void handleReceiveMessage(EMMessage message) {
        ackMessage(message);
    }
}
