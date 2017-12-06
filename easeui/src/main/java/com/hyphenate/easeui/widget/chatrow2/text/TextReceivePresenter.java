package com.hyphenate.easeui.widget.chatrow2.text;

import android.content.Context;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow2.BaseChatRow;
import com.hyphenate.easeui.widget.chatrow2.BaseReceivePresenter;

/**
 * Created by zhangsong on 17-11-30.
 */

public class TextReceivePresenter extends BaseReceivePresenter {
    public TextReceivePresenter(Context context) {
        super(context);
    }

    @Override
    protected BaseChatRow onCreateChatRow(Context context) {
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
