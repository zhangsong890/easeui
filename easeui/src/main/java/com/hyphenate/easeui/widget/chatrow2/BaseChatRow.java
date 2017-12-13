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
import com.hyphenate.easeui.widget.EaseMessageClickListener;
import com.hyphenate.util.DateUtils;

import java.util.Date;

/**
 * A ChatRow is a message list item view.
 * <p>
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
    protected EaseMessageClickListener itemClickListener;

    public BaseChatRow(Context context) {
        this.context = context;
        initExecutor();
        inflateView();
        initBaseViews(contentView);
        onViewInflate(contentView);
    }

    public BaseChatRow(Context context, @LayoutRes int layoutId) {
        this.context = context;
        initExecutor();
        inflateView(layoutId);
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

    /**
     * If setup the ChatRow views first time, must call this method to setup the base views.
     * This base views will not be updated.
     *
     * @param message       The message for ChatRow to setup.
     * @param showTimeStamp If true ChatRow will show the timestamp view according to the message time.
     */
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

    /**
     * Update ChatRow views by message contents, base views will not be updated.
     *
     * @param message The message for ChatRow to update.
     */
    public void updateView(final EMMessage message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onViewUpdate(message);
            }
        });
    }

    /**
     * This is only for presenter, do not call from other place.
     *
     * @param listener
     */
    void setActionListener(@NonNull ItemActionListener listener) {
        this.itemActionListener = listener;
    }

    public void setItemClickListener(EaseMessageClickListener listener) {
        this.itemClickListener = listener;
    }

    public View getContentView() {
        return contentView;
    }

    public Context getContext() {
        return context;
    }

    public void runOnUiThread(Runnable runnable) {
        uiThreadExecutor.post(runnable);
    }

    /**
     * Every ChatRow must provide a layout resource to inflate.
     *
     * @return Layout resource id.
     */
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * The content view is inflated, you can get your specific views
     * by {@link View#findViewById(int)}
     * <p>
     * NOTE: ChatRow is not a view, find view be like this:
     * View view = v.findViewById(id);
     *
     * @param v
     */
    protected abstract void onViewInflate(View v);

    /**
     * Init view by message contents.
     *
     * @param message
     */
    protected abstract void onViewSetup(EMMessage message);

    /**
     * Update views by message contents.
     *
     * @param message
     */
    protected abstract void onViewUpdate(EMMessage message);

    private void inflateView() {
        contentView = LayoutInflater.from(getContext()).inflate(getLayoutId(), null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        contentView.setLayoutParams(params);
        contentView.addOnAttachStateChangeListener(this);
    }

    private void inflateView(@LayoutRes int layoutId) {
        contentView = LayoutInflater.from(getContext()).inflate(layoutId, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        contentView.setLayoutParams(params);
        contentView.addOnAttachStateChangeListener(this);
    }

    private void initBaseViews(View v) {
        timeStampView = (TextView) v.findViewById(com.hyphenate.easeui.R.id.timestamp);
        userAvatarView = (ImageView) v.findViewById(com.hyphenate.easeui.R.id.iv_userhead);
        bubbleLayout = v.findViewById(com.hyphenate.easeui.R.id.bubble);

        if (bubbleLayout != null) {
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
    }

    private void initExecutor() {
        uiThreadExecutor = new Handler(Looper.getMainLooper());
    }

    private void setupBaseView(EMMessage message, boolean showTimeStamp) {
        if (timeStampView != null) {
            if (showTimeStamp) {
                timeStampView.setVisibility(View.VISIBLE);
                timeStampView.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
            } else {
                timeStampView.setVisibility(View.GONE);
            }
        }
    }
}
