package com.hyphenate.easeui.widget.presenter2;

import android.content.Context;
import android.content.Intent;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.easeui.ui.EaseShowVideoActivity;
import com.hyphenate.easeui.widget.chatrow2.BaseChatRow;
import com.hyphenate.easeui.widget.chatrow2.VideoReceiveChatRow;

/**
 * Created by zhangsong on 17-12-5.
 */

public class VideoReceivePresenter extends BaseReceivePresenter {
    private static final String TAG = "VideoSendPresenter";

    public VideoReceivePresenter(Context context) {
        super(context);
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

        if (!message.isAcked() && message.getChatType() == EMMessage.ChatType.Chat) {
            try {
                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Intent intent = new Intent(getContext(), EaseShowVideoActivity.class);
        intent.putExtra("msg", message);
        getContext().startActivity(intent);
    }

    @Override
    protected BaseChatRow onCreateChatRow(Context context) {
        return new VideoReceiveChatRow(context);
    }
}
