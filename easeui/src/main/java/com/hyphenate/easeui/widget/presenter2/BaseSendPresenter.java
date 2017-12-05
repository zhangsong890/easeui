package com.hyphenate.easeui.widget.presenter2;

import android.content.Context;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

/**
 * Created by zhangsong on 17-12-1.
 */

public abstract class BaseSendPresenter extends BasePresenter {
    public BaseSendPresenter(Context context) {
        super(context);
    }

    @Override
    protected final void handleReceiveMessage(EMMessage message) {
    }

    @Override
    protected void handleSendMessage(final EMMessage message) {
        EMMessage.Status status = message.status();

        // Update the view according to the message current status.
        getChatRow().updateView(message);

        if (status == EMMessage.Status.SUCCESS || status == EMMessage.Status.FAIL) {
            return;
        }

        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                getChatRow().updateView(message);
            }

            @Override
            public void onError(int code, String error) {
                Log.i("EaseChatRowPresenter", "onError: " + code + ", error: " + error);
                getChatRow().updateView(message);
            }

            @Override
            public void onProgress(int progress, String status) {
                getChatRow().updateView(message);
            }
        });

        // Already in progress, do not send again
        if (status == EMMessage.Status.INPROGRESS) {
            return;
        }

        // Send the message
        EMClient.getInstance().chatManager().sendMessage(message);
    }
}
