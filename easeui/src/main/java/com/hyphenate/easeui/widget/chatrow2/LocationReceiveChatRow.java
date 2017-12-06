package com.hyphenate.easeui.widget.chatrow2;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.R;

/**
 * Created by zhangsong on 17-12-5.
 */

public class LocationReceiveChatRow extends BaseReceiveChatRow {
    private static final String TAG = "LocationReceiveChatRow";

    private TextView locationView;

    public LocationReceiveChatRow(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ease_row_received_location;
    }

    @Override
    protected void onViewInflate(View v) {
        super.onViewInflate(v);
        locationView = (TextView) v.findViewById(R.id.tv_location);
    }

    @Override
    protected void onViewSetup(EMMessage message) {
        EMLocationMessageBody locBody = (EMLocationMessageBody) message.getBody();
        locationView.setText(locBody.getAddress());
    }

    @Override
    protected void onViewUpdate(EMMessage message) {
    }
}
