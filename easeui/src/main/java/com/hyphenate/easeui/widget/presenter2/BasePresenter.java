package com.hyphenate.easeui.widget.presenter2;

import android.content.Context;
import android.view.View;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow2.BaseChatRow;

/**
 * Created by zhangsong on 17-11-30.
 */

public abstract class BasePresenter {
    private static final String TAG = "BasePresenter";

    private Context mContext;
    private BaseChatRow chatRow;

    public BasePresenter(Context context) {
        this.mContext = context;
        createChatRow();
    }

    public void createChatRow() {
        chatRow = onCreateChatRow(mContext);
    }

    public void setup(EMMessage message) {
        chatRow.setupView(message);
        handleMessage(message);
    }

    public BaseChatRow getChatRow() {
        return chatRow;
    }

    public View getContentView() {
        return chatRow.getContentView();
    }

    protected abstract BaseChatRow onCreateChatRow(Context context);

    protected abstract void handleReceiveMessage(EMMessage message);

    protected abstract void handleSendMessage(EMMessage message);

    private void handleMessage(EMMessage message) {
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            handleReceiveMessage(message);
        } else {
            handleSendMessage(message);
        }
    }
}
