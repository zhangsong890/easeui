package com.hyphenate.easeui.widget.chatrow2.bigexpression;

import android.content.Context;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow2.DefaultChatRow;
import com.hyphenate.easeui.widget.chatrow2.text.TextSendPresenter;

/**
 * Created by zhangsong on 17-12-5.
 */

public class BigExpressionSendPresenter extends TextSendPresenter {
    private static final String TAG = "BigExpressionSendPresen";

    public BigExpressionSendPresenter(Context context) {
        super(context);
    }

    @Override
    protected DefaultChatRow onCreateChatRow(Context context) {
        return new BigExpressionSendChatRow(context);
    }

    @Override
    public void onBubbleClick(EMMessage message) {
    }
}
