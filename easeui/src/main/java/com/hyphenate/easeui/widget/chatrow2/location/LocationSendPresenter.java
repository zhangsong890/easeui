package com.hyphenate.easeui.widget.chatrow2.location;

import android.content.Context;
import android.content.Intent;

import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseBaiduMapActivity;
import com.hyphenate.easeui.widget.chatrow2.BaseChatRow;
import com.hyphenate.easeui.widget.chatrow2.BaseSendPresenter;

/**
 * Created by zhangsong on 17-12-5.
 */

public class LocationSendPresenter extends BaseSendPresenter {
    private static final String TAG = "LocationSendPresenter";

    public LocationSendPresenter(Context context) {
        super(context);
    }

    @Override
    protected BaseChatRow onCreateChatRow(Context context) {
        return new LocationSendChatRow(context);
    }

    @Override
    public void onBubbleClick(EMMessage message) {
        EMLocationMessageBody locBody = (EMLocationMessageBody) message.getBody();
        Intent intent = new Intent(getContext(), EaseBaiduMapActivity.class);
        intent.putExtra("latitude", locBody.getLatitude());
        intent.putExtra("longitude", locBody.getLongitude());
        intent.putExtra("address", locBody.getAddress());
        getContext().startActivity(intent);
    }
}
