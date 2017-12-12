package com.hyphenate.easeui.widget.chatrow2;

import android.content.Context;

/**
 * A presenter to present two kind of ChatRow.(SEND and RECEIVE)
 * <p>
 * Created by zhangsong on 17-12-12.
 */

public abstract class LayoutPresenter extends BasePresenter {
    public LayoutPresenter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    protected final BaseChatRow onCreateChatRow(Context context) {
        return null;
    }

    @Override
    protected abstract LayoutChatRow onCreateChatRow(Context context, int layoutId);
}
