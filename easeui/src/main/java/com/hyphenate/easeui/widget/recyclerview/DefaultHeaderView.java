package com.hyphenate.easeui.widget.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.R;

/**
 * Created by zhangsong on 17-11-24.
 */

public class DefaultHeaderView extends AbstractHeaderView {
    private static final String TAG = "DefaultHeaderView";

    private static final int ROTATE_ANIM_DURATION = 180;

    private ImageView mArrowImageView;
    private SimpleViewSwitcher mProgressBar;
    private TextView mStatusTextView;
    private TextView mHeaderTimeView;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

    public DefaultHeaderView(Context context) {
        super(context);
    }

    public DefaultHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getHeaderContainer() {
        return R.layout.listview_header;
    }

    @Override
    protected void onViewInflate(View v) {
        mArrowImageView = (ImageView) findViewById(R.id.listview_header_arrow);
        mStatusTextView = (TextView) findViewById(R.id.refresh_status_textview);
        //init the progress view
        mProgressBar = (SimpleViewSwitcher) findViewById(R.id.header_progressbar);
        mHeaderTimeView = (TextView) findViewById(R.id.last_refresh_time);

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    @Override
    protected void onStateChange(int state, String stateText) {
        switch (state) {
            case STATE_IDLE:
                mProgressBar.setVisibility(View.INVISIBLE);
                mArrowImageView.clearAnimation();

                mStatusTextView.setText(stateText);
                break;
            case STATE_RELEASE_TRIGGER:
                mProgressBar.setVisibility(View.INVISIBLE);
                mArrowImageView.clearAnimation();
                mArrowImageView.startAnimation(mRotateUpAnim);

                mStatusTextView.setText(stateText);
                break;
            case STATE_LOADING:
                mArrowImageView.clearAnimation();
                mArrowImageView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);

                mStatusTextView.setText(stateText);
                break;
            case STATE_DONE:
                mArrowImageView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);

                mStatusTextView.setText(stateText);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onProgressSet(View v) {
        if (mProgressBar != null) mProgressBar.setView(v);
    }
}
