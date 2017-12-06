package com.hyphenate.easeui.widget.presenter2;

import android.content.Context;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow2.BaseChatRow;
import com.hyphenate.easeui.widget.chatrow2.TextReceiveChatRow;
import com.hyphenate.exceptions.HyphenateException;

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
        if (!message.isAcked() && message.getChatType() == EMMessage.ChatType.Chat) {
            try {
                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
    }
}
