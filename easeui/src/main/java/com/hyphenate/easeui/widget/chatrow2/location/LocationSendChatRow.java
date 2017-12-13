package com.hyphenate.easeui.widget.chatrow2.location;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.chatrow2.BaseSendChatRow;

/**
 * Created by zhangsong on 17-12-5.
 */

public class LocationSendChatRow extends BaseSendChatRow {
    private static final String TAG = "LocationSendChatRow";

    private TextView locationView;

    public LocationSendChatRow(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ease_row_sent_location;
    }

    @Override
    protected void onViewInflate(View v) {
        super.onViewInflate(v);
        locationView = (TextView) v.findViewById(R.id.tv_location);
    }

    @Override
    protected void onViewSetup(EMMessage message) {
        super.onViewSetup(message);
        EMLocationMessageBody locBody = (EMLocationMessageBody) message.getBody();
        locationView.setText(locBody.getAddress());
    }
}
