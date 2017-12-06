package com.hyphenate.easeui.widget.presenter2;

import android.content.Context;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by zhangsong on 17-12-1.
 */

public abstract class BaseReceivePresenter extends BasePresenter {
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

    protected void ackMessage(EMMessage message) {
        if (message == null) return;

        if (message.direct() == EMMessage.Direct.SEND) return;

        if (!message.isAcked() && message.getChatType() == EMMessage.ChatType.Chat) {
            try {
                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
    }
}
