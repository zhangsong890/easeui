package com.hyphenate.easeui.widget.chatrow2;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMNormalFileMessageBody;
import com.hyphenate.easeui.R;
import com.hyphenate.util.TextFormater;

/**
 * Created by zhangsong on 17-12-1.
 */

public class FileSendChatRow extends BaseSendChatRow {
    private static final String TAG = "FileSendChatRow";

    protected TextView fileNameView;
    protected TextView fileSizeView;
    protected TextView fileStateView;
    protected TextView percentageView;

    public FileSendChatRow(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ease_row_sent_file;
    }

    @Override
    protected void onViewInflate(View v) {
        super.onViewInflate(v);
        fileNameView = (TextView) v.findViewById(R.id.tv_file_name);
        fileSizeView = (TextView) v.findViewById(R.id.tv_file_size);
        fileStateView = (TextView) v.findViewById(R.id.tv_file_state);
        percentageView = (TextView) v.findViewById(R.id.percentage);
    }

    @Override
    protected void onViewSetup(EMMessage message) {
        EMNormalFileMessageBody fileMessageBody = (EMNormalFileMessageBody) message.getBody();
        fileNameView.setText(fileMessageBody.getFileName());
        fileSizeView.setText(TextFormater.getDataSize(fileMessageBody.getFileSize()));
    }

    @Override
    protected void onViewUpdate(EMMessage message) {
        switch (message.status()) {
            case CREATE:
                progressBar.setVisibility(View.VISIBLE);
                percentageView.setVisibility(View.INVISIBLE);
                statusView.setVisibility(View.INVISIBLE);
                break;
            case SUCCESS:
                progressBar.setVisibility(View.INVISIBLE);
                percentageView.setVisibility(View.INVISIBLE);
                statusView.setVisibility(View.INVISIBLE);
                break;
            case FAIL:
                progressBar.setVisibility(View.INVISIBLE);
                percentageView.setVisibility(View.INVISIBLE);
                statusView.setVisibility(View.VISIBLE);
                break;
            case INPROGRESS:
                progressBar.setVisibility(View.VISIBLE);
                percentageView.setVisibility(View.VISIBLE);
                percentageView.setText(message.progress() + "%");
                statusView.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
