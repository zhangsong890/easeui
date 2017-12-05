package com.hyphenate.easeui.widget.chatrow2;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import com.hyphenate.chat.EMMessage;

/**
 * Created by zhangsong on 17-12-1.
 */

public class FileReceiveChatRow extends BaseReceiveChatRow {
    protected ProgressBar progressBar;

    public FileReceiveChatRow(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void onViewInflate(View v) {
        super.onViewInflate(v);
        progressBar = (ProgressBar) v.findViewById(com.hyphenate.easeui.R.id.progress_bar);
    }

    @Override
    protected void onViewSetup(EMMessage message) {

    }

    @Override
    protected void onViewUpdate(EMMessage message) {

    }
}
