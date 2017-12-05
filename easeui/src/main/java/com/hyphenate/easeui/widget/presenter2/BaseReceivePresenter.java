package com.hyphenate.easeui.widget.presenter2;

import android.content.Context;

import com.hyphenate.chat.EMMessage;

/**
 * Created by zhangsong on 17-12-1.
 */

public abstract class BaseReceivePresenter extends BasePresenter {
    public BaseReceivePresenter(Context context) {
        super(context);
    }

    @Override
    protected final void handleSendMessage(EMMessage message) {
    }
}
