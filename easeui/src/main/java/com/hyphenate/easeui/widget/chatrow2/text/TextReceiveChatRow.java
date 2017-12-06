package com.hyphenate.easeui.widget.chatrow2.text;

import android.content.Context;
import android.text.Spannable;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.easeui.widget.chatrow2.BaseReceiveChatRow;

/**
 * Created by zhangsong on 17-12-1.
 */

public class TextReceiveChatRow extends BaseReceiveChatRow {
    private TextView contentView;

    public TextReceiveChatRow(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return com.hyphenate.easeui.R.layout.ease_row_received_message;
    }

    @Override
    protected void onViewInflate(View v) {
        super.onViewInflate(v);
        contentView = (TextView) v.findViewById(com.hyphenate.easeui.R.id.tv_chatcontent);
    }

    @Override
    protected void onViewSetup(EMMessage message) {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        Spannable span = EaseSmileUtils.getSmiledText(getContext(), txtBody.getMessage());
        // 设置内容
        contentView.setText(span, TextView.BufferType.SPANNABLE);
    }

    @Override
    protected void onViewUpdate(EMMessage message) {
    }
}
