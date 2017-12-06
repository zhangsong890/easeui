package com.hyphenate.easeui.widget.presenter2;

import android.content.Context;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow2.BaseChatRow;
import com.hyphenate.easeui.widget.chatrow2.BigExpressionReceiveChatRow;

/**
 * Created by zhangsong on 17-12-5.
 */

public class BigExpressionReceivePresenter extends TextReceivePresenter {
    private static final String TAG = "BigExpressionSendPresen";

    public BigExpressionReceivePresenter(Context context) {
        super(context);
    }

    @Override
    public void onBubbleClick(EMMessage message) {
    }

    @Override
    protected BaseChatRow onCreateChatRow(Context context) {
        return new BigExpressionReceiveChatRow(context);
    }
}
