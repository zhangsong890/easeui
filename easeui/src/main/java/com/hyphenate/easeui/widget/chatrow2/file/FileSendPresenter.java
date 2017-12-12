package com.hyphenate.easeui.widget.chatrow2.file;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMNormalFileMessageBody;
import com.hyphenate.easeui.ui.EaseShowNormalFileActivity;
import com.hyphenate.easeui.widget.chatrow2.BaseSendPresenter;
import com.hyphenate.easeui.widget.chatrow2.DefaultChatRow;
import com.hyphenate.util.FileUtils;

import java.io.File;

/**
 * Created by zhangsong on 17-12-5.
 */

public class FileSendPresenter extends BaseSendPresenter {
    private static final String TAG = "FileSendPresenter";

    public FileSendPresenter(Context context) {
        super(context);
    }

    @Override
    protected DefaultChatRow onCreateChatRow(Context context) {
        return new FileSendChatRow(context);
    }

    @Override
    public void onBubbleClick(EMMessage message) {
        EMNormalFileMessageBody fileMessageBody = (EMNormalFileMessageBody) message.getBody();
        String filePath = fileMessageBody.getLocalUrl();
        File file = new File(filePath);
        if (file.exists()) {
            // open files if it exist
            FileUtils.openFile(file, (Activity) getContext());
        } else {
            // download the file
            getContext().startActivity(new Intent(getContext(), EaseShowNormalFileActivity.class).putExtra("msg", message));
        }
    }
}
