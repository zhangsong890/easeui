package com.hyphenate.easeui.widget.chatrow2;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;

/**
 * Created by zhangsong on 17-11-30.
 */

public abstract class BaseChatRow {
    private static final String TAG = "BaseChatRow";

    private View contentView;

    private Context context;
    /**
     * This view to show the message time, it will show every per 30s.
     */
    protected TextView timeStampView;
    protected ImageView userAvatarView;
    protected View bubbleLayout;

    private Handler uiThreadExecutor;

    public BaseChatRow(Context context) {
        this.context = context;
        initExecutor();
        inflateView();
        initBaseViews(contentView);
        onViewInflate(contentView);
    }

    public void setupView(final EMMessage message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO: Set up the base views
                onViewSetup(message);
            }
        });
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
    }

    private void initBaseViews(View v) {
        timeStampView = (TextView) v.findViewById(com.hyphenate.easeui.R.id.timestamp);
        userAvatarView = (ImageView) v.findViewById(com.hyphenate.easeui.R.id.iv_userhead);
        bubbleLayout = v.findViewById(com.hyphenate.easeui.R.id.bubble);
    }

    private void initExecutor() {
        uiThreadExecutor = new Handler(Looper.getMainLooper());
    }

    private void runOnUiThread(Runnable runnable) {
        uiThreadExecutor.post(runnable);
    }
}
