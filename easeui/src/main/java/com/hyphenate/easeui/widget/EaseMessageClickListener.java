package com.hyphenate.easeui.widget;

import com.hyphenate.chat.EMMessage;

/**
 * Created by zhangsong on 17-12-6.
 */

public interface EaseMessageClickListener {
    /**
     * there is default handling when bubble is clicked, if you want handle it, return true
     * another way is you implement in onBubbleClick() of chat row
     *
     * @param message
     * @return
     */
    boolean onBubbleClick(EMMessage message);

    void onBubbleLongClick(EMMessage message);

    void onUserAvatarClick(String username);

    void onUserAvatarLongClick(String username);
}
