package com.hyphenate.easeui.widget.chatrow2.bigexpression;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.widget.chatrow2.BaseSendChatRow;

/**
 * Created by zhangsong on 17-12-5.
 */

public class BigExpressionSendChatRow extends BaseSendChatRow {
    private static final String TAG = "BigExpressionSendChatRo";

    private ImageView imageView;

    public BigExpressionSendChatRow(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ease_row_sent_bigexpression;
    }

    @Override
    protected void onViewInflate(View v) {
        super.onViewInflate(v);
        imageView = (ImageView) v.findViewById(R.id.image);
    }

    @Override
    protected void onViewSetup(EMMessage message) {
        super.onViewSetup(message);
        String emojIconId = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_EXPRESSION_ID, null);
        EaseEmojicon emojIcon = null;
        if (EaseUI.getInstance().getEmojiconInfoProvider() != null) {
            emojIcon = EaseUI.getInstance().getEmojiconInfoProvider().getEmojiconInfo(emojIconId);
        }
        if (emojIcon != null) {
            if (emojIcon.getBigIcon() != 0) {
                Glide.with(getContext()).load(emojIcon.getBigIcon()).placeholder(R.drawable.ease_default_expression).into(imageView);
            } else if (emojIcon.getBigIconPath() != null) {
                Glide.with(getContext()).load(emojIcon.getBigIconPath()).placeholder(R.drawable.ease_default_expression).into(imageView);
            } else {
                imageView.setImageResource(R.drawable.ease_default_expression);
            }
        }
    }

    @Override
    protected void onViewUpdate(EMMessage message) {
    }
}
