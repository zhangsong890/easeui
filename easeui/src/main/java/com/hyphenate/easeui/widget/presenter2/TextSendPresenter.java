package com.hyphenate.easeui.widget.presenter2;

import android.content.Context;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow2.BaseChatRow;
import com.hyphenate.easeui.widget.chatrow2.TextSendChatRow;

/**
 * Created by zhangsong on 17-12-1.
 */

public class TextSendPresenter extends BaseSendPresenter {
    public TextSendPresenter(Context context) {
        super(context);
    }

    @Override
    protected BaseChatRow onCreateChatRow(Context context) {
        return new TextSendChatRow(context);
    }

    @Override
    public void onBubbleClick(EMMessage message) {
    }
}
