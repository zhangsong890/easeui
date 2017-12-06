package com.hyphenate.easeui.widget.chatrow2;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.View;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.chatrow2.BaseChatRow;

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

    private Handler uiThreadExecutor;

    public BasePresenter(Context context) {
        this.mContext = context;
        createChatRow();
        initUiExecutor();
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
                            @Nullable EaseChatMessageList.MessageListItemClickListener listener) {
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
        uiThreadExecutor.post(r);
    }

    /**
     * The sub-class will implements this method and return a specific BaseChatRow.
     *
     * @param context
     * @return
     */
    protected abstract BaseChatRow onCreateChatRow(Context context);

    /**
     * The sub-class will implements this method to handle the received message.
     *
     * @param message
     */
    protected abstract void handleReceiveMessage(EMMessage message);

    /**
     * The sub-class will implements this method to handle send message.
     *
     * @param message
     */
    protected abstract void handleSendMessage(EMMessage message);

    private void createChatRow() {
        chatRow = onCreateChatRow(mContext);
    }

    private void initUiExecutor() {
        uiThreadExecutor = new Handler(Looper.getMainLooper());
    }

    private void handleMessage(EMMessage message) {
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            handleReceiveMessage(message);
        } else {
            handleSendMessage(message);
        }
    }
}
