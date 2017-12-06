package com.hyphenate.easeui.widget.presenter2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseShowBigImageActivity;
import com.hyphenate.easeui.widget.chatrow2.BaseChatRow;
import com.hyphenate.easeui.widget.chatrow2.ImageSendChatRow;

import java.io.File;

/**
 * Created by zhangsong on 17-12-5.
 */

public class ImageSendPresenter extends BaseSendPresenter {
    private static final String TAG = "ImageSendPresenter";

    public ImageSendPresenter(Context context) {
        super(context);
    }

    @Override
    protected BaseChatRow onCreateChatRow(Context context) {
        return new ImageSendChatRow(context);
    }

    @Override
    public void onBubbleClick(EMMessage message) {
        EMImageMessageBody imgBody = (EMImageMessageBody) message.getBody();
        if(EMClient.getInstance().getOptions().getAutodownloadThumbnail()){
            if(imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.FAILED){
                getChatRow().updateView(message);
                // retry download with click event of user
                EMClient.getInstance().chatManager().downloadThumbnail(message);
            }
        } else{
            if(imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                    imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING ||
                    imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.FAILED){
                // retry download with click event of user
                EMClient.getInstance().chatManager().downloadThumbnail(message);
                getChatRow().updateView(message);
                return;
            }
        }
        Intent intent = new Intent(getContext(), EaseShowBigImageActivity.class);
        File file = new File(imgBody.getLocalUrl());
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            intent.putExtra("uri", uri);
        } else {
            // The local full size pic does not exist yet.
            // ShowBigImage needs to download it from the server
            // first
            String msgId = message.getMsgId();
            intent.putExtra("messageId", msgId);
            intent.putExtra("localUrl", imgBody.getLocalUrl());
        }
        if (message != null && message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked()
                && message.getChatType() == EMMessage.ChatType.Chat) {
            try {
                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        getContext().startActivity(intent);
    }
}
