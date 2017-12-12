package com.hyphenate.easeui.widget.chatrow2;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.easeui.widget.EaseMessageClickListener;
import com.hyphenate.exceptions.HyphenateException;

/**
 * A presenter is a holder of a BaseChatRow, all ChatRow's view update logic will be in
 * presenter and update the ChatRow's views by presenter.
 * <p>
 * Created by zhangsong on 17-11-30.
 */

public abstract class BasePresenter implements BaseChatRow.ItemActionListener {
    private static final String TAG = "BasePresenter";

    private Context mContext;
    private BaseChatRow chatRow;
    @LayoutRes
    private int layoutId;

    public BasePresenter(Context context) {
        this.mContext = context;
        createChatRow();
    }

    public BasePresenter(Context context, @LayoutRes int layoutId) {
        this.mContext = context;
        this.layoutId = layoutId;
        createChatRow(layoutId);
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

    /**
     * When Fragment or Activity is finished, this method will be call.
     * <p>
     * You can do something like:
     * Stop the voice play when back button pressed.
     */
    @Override
    public void onViewDetachedFromWindow() {
    }

    /**
     * Setup the chat row according to the message and other configs
     *
     * @param message       The target message to set to the chat row.
     * @param showTimeStamp If true, the chat row timestamp view will show the message's time.
     * @param listener      User can set a listener to handle the click actions.
     */
    public final void setup(EMMessage message, boolean showTimeStamp,
                            @Nullable EaseMessageClickListener listener) {
        chatRow.setupView(message, showTimeStamp);
        chatRow.setActionListener(this);
        if (listener != null) {
            chatRow.setItemClickListener(listener);
        }
        handleMessage(message);
    }

    public View getContentView() {
        return chatRow.getContentView();
    }

    protected BaseChatRow getChatRow() {
        return chatRow;
    }

    protected Context getContext() {
        return mContext;
    }

    protected void runOnUiThread(Runnable r) {
        chatRow.runOnUiThread(r);
    }

    @LayoutRes
    protected final int getLayoutId() {
        return layoutId;
    }

    /**
     * The sub-class will implements this method to handle the received message.
     *
     * @param message
     */
    protected void handleReceiveMessage(EMMessage message) {
    }

    /**
     * Send the message actually.
     *
     * @param message
     */
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

    /**
     * Send the ack to the message sender.
     *
     * @param message
     */
    protected void ackMessage(EMMessage message) {
        if (message == null) return;

        if (message.direct() == EMMessage.Direct.SEND) return;

        if (!message.isAcked() && message.getChatType() == EMMessage.ChatType.Chat) {
            try {
                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The sub-class will implements this method and return a specific BaseChatRow.
     *
     * @param context
     * @return
     */
    protected abstract BaseChatRow onCreateChatRow(Context context);

    protected abstract BaseChatRow onCreateChatRow(Context context, @LayoutRes int layoutId);

    private void createChatRow() {
        chatRow = onCreateChatRow(mContext);
    }

    private void createChatRow(@LayoutRes int layoutId) {
        chatRow = onCreateChatRow(mContext, layoutId);
    }

    private void handleMessage(EMMessage message) {
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            handleReceiveMessage(message);
        } else {
            handleSendMessage(message);
        }
    }
}
