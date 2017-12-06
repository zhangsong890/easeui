package com.hyphenate.easeui.widget.chatrow2;

import android.content.Context;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.utils.EaseSmileUtils;

/**
 * Created by zhangsong on 17-12-1.
 */

public class TextSendChatRow extends BaseSendChatRow {
    private static final String TAG = "TextSendChatRow";

    private TextView contentView;

    public TextSendChatRow(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ease_row_sent_message;
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
        switch (message.status()) {
            case CREATE:
                progressBar.setVisibility(View.VISIBLE);
                statusView.setVisibility(View.GONE);
                break;
            case SUCCESS:
                progressBar.setVisibility(View.GONE);
                statusView.setVisibility(View.GONE);
                break;
            case FAIL:
                progressBar.setVisibility(View.GONE);
                statusView.setVisibility(View.VISIBLE);
                break;
            case INPROGRESS:
                progressBar.setVisibility(View.VISIBLE);
                statusView.setVisibility(View.GONE);
                break;
        }
    }
}
