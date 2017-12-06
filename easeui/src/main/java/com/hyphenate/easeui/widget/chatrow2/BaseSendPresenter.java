package com.hyphenate.easeui.widget.chatrow2;

import android.content.Context;
import android.os.Bundle;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.EaseAlertDialog;

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
        // Update the view according to the message current status.
        getChatRow().updateView(message);

        EMMessage.Status status = message.status();

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

    @Override
    public void onResendClick(final EMMessage message) {
        new EaseAlertDialog(getContext(), R.string.resend, R.string.confirm_resend, null, new EaseAlertDialog.AlertDialogUser() {
            @Override
            public void onResult(boolean confirmed, Bundle bundle) {
                if (!confirmed) {
                    return;
                }
                message.setStatus(EMMessage.Status.CREATE);
                handleSendMessage(message);
            }
        }, true).show();
    }
}
