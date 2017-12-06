package com.hyphenate.easeui.widget.chatrow2;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.util.DateUtils;
import com.hyphenate.util.EMLog;

import java.util.Date;

/**
 * Created by zhangsong on 17-11-30.
 */

public abstract class BaseChatRow implements View.OnAttachStateChangeListener {
    public interface ItemActionListener {
        /**
         * To resend message, this method is triggered by {@link BaseSendChatRow#statusView} statusView's
         * click listener.
         *
         * @param message
         */
        void onResendClick(EMMessage message);

        /**
         * The message item view click method
         *
         * @param message
         */
        void onBubbleClick(EMMessage message);

        void onViewDetachedFromWindow();
    }

    private static final String TAG = "BaseChatRow";

    private View contentView;

    private Context context;
    /**
     * This view to show the message time, it will show every per 30s.
     */
    protected TextView timeStampView;
    protected ImageView userAvatarView;
    protected View bubbleLayout;

    protected EMMessage message;

    private Handler uiThreadExecutor;

    protected ItemActionListener itemActionListener;
    protected EaseChatMessageList.MessageListItemClickListener itemClickListener;

    public BaseChatRow(Context context) {
        this.context = context;
        initExecutor();
        inflateView();
        initBaseViews(contentView);
        onViewInflate(contentView);
    }

    @Override
    public void onViewAttachedToWindow(View v) {
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        contentView.removeOnAttachStateChangeListener(this);
        itemActionListener.onViewDetachedFromWindow();
    }

    public final void setupView(final EMMessage message, final boolean showTimeStamp) {
        this.message = message;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Set up the base views
                setupBaseView(message, showTimeStamp);
                onViewSetup(message);
            }
        });
    }

    public void setActionListener(@NonNull ItemActionListener listener) {
        this.itemActionListener = listener;
    }

    public void setItemClickListener(EaseChatMessageList.MessageListItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void updateView(final EMMessage message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onViewUpdate(message);
            }
        });
    }

    public View getContentView() {
        return contentView;
    }

    protected Context getContext() {
        return context;
    }

    protected void runOnUiThread(Runnable runnable) {
        uiThreadExecutor.post(runnable);
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void onViewInflate(View v);

    protected abstract void onViewSetup(EMMessage message);

    protected abstract void onViewUpdate(EMMessage message);

    private void inflateView() {
        contentView = LayoutInflater.from(getContext()).inflate(getLayoutId(), null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        contentView.setLayoutParams(params);
        contentView.addOnAttachStateChangeListener(this);
    }

    private void initBaseViews(View v) {
        timeStampView = (TextView) v.findViewById(com.hyphenate.easeui.R.id.timestamp);
        userAvatarView = (ImageView) v.findViewById(com.hyphenate.easeui.R.id.iv_userhead);
        bubbleLayout = v.findViewById(com.hyphenate.easeui.R.id.bubble);

        bubbleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null && itemClickListener.onBubbleClick(message))
                    return;

                itemActionListener.onBubbleClick(message);
            }
        });

        bubbleLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.onBubbleLongClick(message);
                return true;
            }
        });
    }

    private void initExecutor() {
        uiThreadExecutor = new Handler(Looper.getMainLooper());
    }

    private void setupBaseView(EMMessage message, boolean showTimeStamp) {
        if (showTimeStamp) {
            timeStampView.setVisibility(View.VISIBLE);
            timeStampView.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
        } else {
            timeStampView.setVisibility(View.GONE);
        }
    }
}
