package com.hyphenate.easeui.widget.recyclerview;

import android.support.annotation.IntDef;

/**
 * Created by Jack on 2015/10/19.
 */
interface BaseHeaderFooter {

    int STATE_IDLE = 0;
    int STATE_RELEASE_TRIGGER = 1;
    int STATE_LOADING = 2;
    /**
     * This state will be changed to {@link #STATE_IDLE} after the smooth scroll finish.
     */
    int STATE_DONE = 3;

    @IntDef({STATE_IDLE, STATE_RELEASE_TRIGGER, STATE_LOADING, STATE_DONE})
    @interface State {
    }

    void onMove(float delta);

    boolean releaseAction();

    void loadingComplete();

    void setState(@State int state);
}