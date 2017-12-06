package com.hyphenate.easeui.widget.presenter2;

import android.content.Context;
import android.content.Intent;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseBaiduMapActivity;
import com.hyphenate.easeui.widget.chatrow2.BaseChatRow;
import com.hyphenate.easeui.widget.chatrow2.LocationReceiveChatRow;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by zhangsong on 17-12-5.
 */

public class LocationReceivePresenter extends BaseReceivePresenter {
    private static final String TAG = "LocationSendPresenter";

    public LocationReceivePresenter(Context context) {
        super(context);
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

    @Override
    protected BaseChatRow onCreateChatRow(Context context) {
        return new LocationReceiveChatRow(context);
    }

    @Override
    protected void handleReceiveMessage(EMMessage message) {
        if (!message.isAcked() && message.getChatType() == EMMessage.ChatType.Chat) {
            try {
                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
    }
}
