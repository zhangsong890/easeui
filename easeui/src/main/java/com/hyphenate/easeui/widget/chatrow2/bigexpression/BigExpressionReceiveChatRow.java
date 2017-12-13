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
import com.hyphenate.easeui.widget.chatrow2.BaseReceiveChatRow;

/**
 * Created by zhangsong on 17-12-5.
 */

public class BigExpressionReceiveChatRow extends BaseReceiveChatRow {
    private static final String TAG = "BigExpressionReceiveCha";

    private ImageView imageView;

    public BigExpressionReceiveChatRow(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ease_row_received_bigexpression;
    }

    @Override
    protected void onViewInflate(View v) {
        super.onViewInflate(v);
        imageView = (ImageView) v.findViewById(R.id.image);
    }

    @Override
    protected void onViewSetup(EMMessage message) {
        String emojiconId = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_EXPRESSION_ID, null);
        EaseEmojicon emojicon = null;
        if (EaseUI.getInstance().getEmojiconInfoProvider() != null) {
            emojicon = EaseUI.getInstance().getEmojiconInfoProvider().getEmojiconInfo(emojiconId);
        }
        if (emojicon != null) {
            if (emojicon.getBigIcon() != 0) {
                Glide.with(getContext()).load(emojicon.getBigIcon()).placeholder(R.drawable.ease_default_expression).into(imageView);
            } else if (emojicon.getBigIconPath() != null) {
                Glide.with(getContext()).load(emojicon.getBigIconPath()).placeholder(R.drawable.ease_default_expression).into(imageView);
            } else {
                imageView.setImageResource(R.drawable.ease_default_expression);
            }
        }
    }

    @Override
    protected void onViewUpdate(EMMessage message) {
    }
}
