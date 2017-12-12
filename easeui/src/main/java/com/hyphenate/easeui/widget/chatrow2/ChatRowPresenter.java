package com.hyphenate.easeui.widget.chatrow2;

import android.content.Context;

/**
 * Return a specific {@link BaseChatRow} ChatRow class int the {@link BasePresenter#onCreateChatRow(Context)}
 * abstract onCreateChatRow method.
 * <p>
 * Created by zhangsong on 17-12-11.
 */

public abstract class ChatRowPresenter extends BasePresenter {
    public ChatRowPresenter(Context context) {
        super(context);
    }

    @Override
    protected final BaseChatRow onCreateChatRow(Context context, int layoutId) {
        return null;
    }

    @Override
    protected abstract DefaultChatRow onCreateChatRow(Context context);
}
