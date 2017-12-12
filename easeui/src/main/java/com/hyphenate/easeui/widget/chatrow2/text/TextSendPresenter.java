package com.hyphenate.easeui.widget.chatrow2.text;

import android.content.Context;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow2.BaseSendPresenter;
import com.hyphenate.easeui.widget.chatrow2.DefaultChatRow;

/**
 * Created by zhangsong on 17-12-1.
 */

public class TextSendPresenter extends BaseSendPresenter {
    public TextSendPresenter(Context context) {
        super(context);
    }

    @Override
    protected DefaultChatRow onCreateChatRow(Context context) {
        return new TextSendChatRow(context);
    }

    @Override
    public void onBubbleClick(EMMessage message) {
    }
}
