package com.hyphenate.easeui.widget.chatrow2.video;

import android.content.Context;
import android.content.Intent;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.easeui.ui.EaseShowVideoActivity;
import com.hyphenate.easeui.widget.chatrow2.BaseSendPresenter;
import com.hyphenate.easeui.widget.chatrow2.DefaultChatRow;

/**
 * Created by zhangsong on 17-12-5.
 */

public class VideoSendPresenter extends BaseSendPresenter {
    private static final String TAG = "VideoSendPresenter";

    public VideoSendPresenter(Context context) {
        super(context);
    }

    @Override
    protected DefaultChatRow onCreateChatRow(Context context) {
        return new VideoSendChatRow(context);
    }

    @Override
    public void onBubbleClick(EMMessage message) {
        EMVideoMessageBody videoBody = (EMVideoMessageBody) message.getBody();
        if (!EMClient.getInstance().getOptions().getAutodownloadThumbnail()) {
            if (videoBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                    videoBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING ||
                    videoBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.FAILED) {
                // retry download with click event of user
                EMClient.getInstance().chatManager().downloadThumbnail(message);
                return;
            }
        }

        Intent intent = new Intent(getContext(), EaseShowVideoActivity.class);
        intent.putExtra("msg", message);
        getContext().startActivity(intent);
    }
}
