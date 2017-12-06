package com.hyphenate.easeui.widget.chatrow2;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMNormalFileMessageBody;
import com.hyphenate.easeui.R;
import com.hyphenate.util.TextFormater;

import java.io.File;

/**
 * Created by zhangsong on 17-12-1.
 */

public class FileReceiveChatRow extends BaseReceiveChatRow {
    protected ProgressBar progressBar;

    protected TextView fileNameView;
    protected TextView fileSizeView;
    protected TextView fileStateView;

    public FileReceiveChatRow(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ease_row_received_file;
    }

    @Override
    protected void onViewInflate(View v) {
        super.onViewInflate(v);
        progressBar = (ProgressBar) v.findViewById(com.hyphenate.easeui.R.id.progress_bar);
        fileNameView = (TextView) v.findViewById(R.id.tv_file_name);
        fileSizeView = (TextView) v.findViewById(R.id.tv_file_size);
        fileStateView = (TextView) v.findViewById(R.id.tv_file_state);
    }

    @Override
    protected void onViewSetup(EMMessage message) {
        EMNormalFileMessageBody fileMessageBody = (EMNormalFileMessageBody) message.getBody();
        fileNameView.setText(fileMessageBody.getFileName());
        fileSizeView.setText(TextFormater.getDataSize(fileMessageBody.getFileSize()));

        String filePath = fileMessageBody.getLocalUrl();
        File file = new File(filePath);
        if (file.exists()) {
            fileStateView.setText(R.string.Have_downloaded);
        } else {
            fileStateView.setText(R.string.Did_not_download);
        }
    }

    @Override
    protected void onViewUpdate(EMMessage message) {
        switch (message.status()) {
            case CREATE:
            case INPROGRESS:
                progressBar.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
            case FAIL:
                progressBar.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
