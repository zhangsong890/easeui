package com.hyphenate.easeui.widget.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hyphenate.easeui.R;

/**
 * Created by zhangsong on 17-11-28.
 */

public class ProgressHeaderView extends AbstractHeaderView {
    private static final String TAG = "ProgressHeaderView";

    private SimpleViewSwitcher mProgressBar;

    public ProgressHeaderView(Context context) {
        super(context);
    }

    public ProgressHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getHeaderContainer() {
        return R.layout.view_header_progress;
    }

    @Override
    public void setStateText(int state, int stringId) {
    }

    @Override
    protected void onViewInflate(View v) {
        mProgressBar = (SimpleViewSwitcher) findViewById(R.id.header_progressbar);
        Log.i(TAG, "onViewInflate: " + mMeasuredHeight);
    }

    @Override
    protected void onStateChange(int state, @Nullable String stateText) {
        switch (state) {
            case STATE_IDLE:
            case STATE_RELEASE_TRIGGER:
            case STATE_LOADING:
                mProgressBar.setVisibility(VISIBLE);
                break;
            case STATE_DONE:
                mProgressBar.setVisibility(INVISIBLE);
                break;
        }
    }

    @Override
    protected void onProgressSet(View v) {
        mProgressBar.setView(v);
    }
}
