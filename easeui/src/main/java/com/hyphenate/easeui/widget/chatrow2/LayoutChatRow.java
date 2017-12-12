package com.hyphenate.easeui.widget.chatrow2;

import android.content.Context;

/**
 * Created by zhangsong on 17-12-12.
 */

public abstract class LayoutChatRow extends BaseChatRow {
    private LayoutChatRow(Context context) {
        super(context);
    }

    public LayoutChatRow(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    protected final int getLayoutId() {
        return 0;
    }
}
