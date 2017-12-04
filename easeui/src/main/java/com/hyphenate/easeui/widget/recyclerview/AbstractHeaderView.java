package com.hyphenate.easeui.widget.recyclerview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.recyclerview.progressindicator.AVLoadingIndicatorView;

import java.util.HashMap;

/**
 * @author zhangsong
 */
public abstract class AbstractHeaderView extends LinearLayout implements BaseHeaderFooter {
    public interface IScrollListener {
        void onScrollFinish();
    }

    public interface HeaderListener {
        void onLoading();
    }

    private View mHeaderView;
    private int mState = STATE_IDLE;
    // 获取header view的最大高度
    protected int mMeasuredHeight;

    private HashMap<Integer, String> stateTextMap = new HashMap<>();

    public AbstractHeaderView(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public AbstractHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public AbstractHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        parseStyle(attrs);
    }

    @Override
    public void onMove(float delta) {
        if (getVisibleHeight() > 0 || delta > 0) {
            setVisibleHeight((int) delta + getVisibleHeight());

            if (mState <= STATE_RELEASE_TRIGGER) {
                if (getVisibleHeight() > mMeasuredHeight) {
                    // 未处于刷新状态，更新箭头
                    setState(STATE_RELEASE_TRIGGER);
                } else {
                    // 未达到header view的默认高度,不触发刷新事件
                    setState(STATE_IDLE);
                }
            }
        }
    }

    @Override
    public boolean releaseAction() {
        boolean isOnRefresh = false;
        int height = getVisibleHeight();
        if (height == 0) {
            // not visible.
            isOnRefresh = false;
        }

        if (getVisibleHeight() > mMeasuredHeight && mState < STATE_LOADING) {
            setState(STATE_LOADING);
            isOnRefresh = true;
        }
        // refreshing and header isn't shown fully. do nothing.
        if (mState == STATE_LOADING && height <= mMeasuredHeight) {
            //return;
        }
        // default: scroll back to dismiss header.
        int destHeight = 0;
        // is refreshing, just scroll back to show all the header.
        if (mState == STATE_LOADING) {
            destHeight = mMeasuredHeight;
        }
        smoothScrollTo(destHeight, null);

        return isOnRefresh;
    }

    @Override
    public void loadingComplete() {
        setState(STATE_DONE);
        reset();
    }

    @Override
    public void setState(@State int state) {
        if (state == mState) return;
        mState = state;
        onStateChange(state, getStateText(state));
    }

    public void setProgressStyle(@Progress.Style int style) {
        View progressBar = null;
        if (style == Progress.SysProgress) {
            progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyle);
        } else {
            AVLoadingIndicatorView progressView = new AVLoadingIndicatorView(this.getContext());
            progressView.setIndicatorColor(0xffB5B5B5);
            progressView.setIndicatorId(style);
            progressBar = progressView;
        }

        onProgressSet(progressBar);
    }

    public int getState() {
        return mState;
    }

    public void setVisibleHeight(int height) {
        if (height < 0) height = 0;
        LayoutParams lp = (LayoutParams) mHeaderView.getLayoutParams();
        lp.height = height;
        mHeaderView.setLayoutParams(lp);
    }

    public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) mHeaderView.getLayoutParams();
        return lp.height;
    }

    public void reset() {
        smoothScrollTo(0, new IScrollListener() {
            @Override
            public void onScrollFinish() {
                setState(STATE_IDLE);
            }
        });
    }

    @LayoutRes
    protected abstract int getHeaderContainer();

    protected abstract void onViewInflate(View v);

    protected abstract void onStateChange(@State int state, @Nullable String stateText);

    protected abstract void onProgressSet(View v);

    public void setStateText(@State int state, @StringRes int stringId) {
        stateTextMap.put(state, getContext().getString(stringId));
    }

    protected void smoothScrollTo(int destHeight, final IScrollListener listener) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });

        if (listener != null) {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    listener.onScrollFinish();
                }
            });
        }

        animator.start();
    }

    private void initView() {
        // 初始情况，设置下拉刷新view高度为0
        mHeaderView = LayoutInflater.from(getContext()).inflate(getHeaderContainer(), null);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        this.setLayoutParams(lp);
        this.setPadding(0, 0, 0, 0);
        addView(mHeaderView, new LayoutParams(LayoutParams.MATCH_PARENT, 0));
        setGravity(Gravity.BOTTOM);

        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredHeight();

        onViewInflate(mHeaderView);
    }

    private void parseStyle(AttributeSet attrs) {
        if (attrs == null) return;

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.HeaderView);
        stateTextMap.put(STATE_IDLE, ta.getString(R.styleable.HeaderView_idle_text));
        stateTextMap.put(STATE_RELEASE_TRIGGER, ta.getString(R.styleable.HeaderView_trigger_text));
        stateTextMap.put(STATE_LOADING, ta.getString(R.styleable.HeaderView_loading_text));
        stateTextMap.put(STATE_DONE, ta.getString(R.styleable.HeaderView_done_text));
        ta.recycle();
    }

    private String getStateText(@State int state) {
        return stateTextMap.get(state);
    }
}