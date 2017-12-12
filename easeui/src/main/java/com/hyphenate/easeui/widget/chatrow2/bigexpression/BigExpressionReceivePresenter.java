package com.hyphenate.easeui.widget.chatrow2.bigexpression;

import android.content.Context;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow2.DefaultChatRow;
import com.hyphenate.easeui.widget.chatrow2.text.TextReceivePresenter;

/**
 * Created by zhangsong on 17-12-5.
 */

public class BigExpressionReceivePresenter extends TextReceivePresenter {
    private static final String TAG = "BigExpressionSendPresen";

    public BigExpressionReceivePresenter(Context context) {
        super(context);
    }

    @Override
    protected DefaultChatRow onCreateChatRow(Context context) {
        return new BigExpressionReceiveChatRow(context);
    }

    @Override
    public void onBubbleClick(EMMessage message) {
    }
}
