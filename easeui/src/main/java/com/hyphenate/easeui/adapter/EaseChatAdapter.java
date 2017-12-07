package com.hyphenate.easeui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.widget.EaseMessageClickListener;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.easeui.widget.chatrow2.BasePresenter;
import com.hyphenate.easeui.widget.chatrow2.bigexpression.BigExpressionReceivePresenter;
import com.hyphenate.easeui.widget.chatrow2.bigexpression.BigExpressionSendPresenter;
import com.hyphenate.easeui.widget.chatrow2.file.FileReceivePresenter;
import com.hyphenate.easeui.widget.chatrow2.file.FileSendPresenter;
import com.hyphenate.easeui.widget.chatrow2.image.ImageReceivePresenter;
import com.hyphenate.easeui.widget.chatrow2.image.ImageSendPresenter;
import com.hyphenate.easeui.widget.chatrow2.location.LocationReceivePresenter;
import com.hyphenate.easeui.widget.chatrow2.location.LocationSendPresenter;
import com.hyphenate.easeui.widget.chatrow2.text.TextReceivePresenter;
import com.hyphenate.easeui.widget.chatrow2.text.TextSendPresenter;
import com.hyphenate.easeui.widget.chatrow2.video.VideoReceivePresenter;
import com.hyphenate.easeui.widget.chatrow2.video.VideoSendPresenter;
import com.hyphenate.easeui.widget.chatrow2.voice.VoiceReceivePresenter;
import com.hyphenate.easeui.widget.chatrow2.voice.VoiceSendPresenter;
import com.hyphenate.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangsong on 17-11-30.
 */

public class EaseChatAdapter extends RecyclerView.Adapter<EaseChatAdapter.PresenterHolder> {
    private static final String TAG = "EaseChatAdapter";

    private static final int MESSAGE_TYPE_INVALID = -1;
    private static final int MESSAGE_TYPE_RECV_TXT = 0;
    private static final int MESSAGE_TYPE_SENT_TXT = 1;
    private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
    private static final int MESSAGE_TYPE_SENT_LOCATION = 3;
    private static final int MESSAGE_TYPE_RECV_LOCATION = 4;
    private static final int MESSAGE_TYPE_RECV_IMAGE = 5;
    private static final int MESSAGE_TYPE_SENT_VOICE = 6;
    private static final int MESSAGE_TYPE_RECV_VOICE = 7;
    private static final int MESSAGE_TYPE_SENT_VIDEO = 8;
    private static final int MESSAGE_TYPE_RECV_VIDEO = 9;
    private static final int MESSAGE_TYPE_SENT_FILE = 10;
    private static final int MESSAGE_TYPE_RECV_FILE = 11;
    private static final int MESSAGE_TYPE_SENT_EXPRESSION = 12;
    private static final int MESSAGE_TYPE_RECV_EXPRESSION = 13;

    private Context mContext;

    private List<EMMessage> messages = new ArrayList<>();

    private EaseCustomChatRowProvider customRowProvider;
    private EaseMessageClickListener itemClickListener;

    public EaseChatAdapter(Context context, List<EMMessage> messages) {
        this.mContext = context;
        this.messages = messages;
    }

    @Override
    public PresenterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create the Presenter by viewType.
        BasePresenter presenter = createPresenterByViewType(viewType);
        return new PresenterHolder(presenter);
    }

    @Override
    public void onBindViewHolder(PresenterHolder holder, int position) {
        // Set up the view by message.
        EMMessage currentMessage = messages.get(position);

        boolean showTimeStamp = false;
        if (position == 0) {
            showTimeStamp = true;
        } else {
            EMMessage prevMessage = messages.get(position - 1);
            if (prevMessage != null && DateUtils.isCloseEnough(currentMessage.getMsgTime(), prevMessage.getMsgTime())) {
                showTimeStamp = false;
            } else {
                showTimeStamp = true;
            }
        }

        holder.setup(currentMessage, showTimeStamp, itemClickListener);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Return the layout id?
        EMMessage message = messages.get(position);

        // TODO: CustomRowProvider?
        if (customRowProvider != null && customRowProvider.getCustomChatRowType(message) > 0) {
            return customRowProvider.getCustomChatRowType(message) + 13;
        }

        if (message.getType() == EMMessage.Type.TXT) {
            if (message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_EXPRESSION : MESSAGE_TYPE_SENT_EXPRESSION;
            }
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TXT : MESSAGE_TYPE_SENT_TXT;
        }
        if (message.getType() == EMMessage.Type.IMAGE) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_IMAGE : MESSAGE_TYPE_SENT_IMAGE;
        }
        if (message.getType() == EMMessage.Type.LOCATION) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_LOCATION : MESSAGE_TYPE_SENT_LOCATION;
        }
        if (message.getType() == EMMessage.Type.VOICE) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE : MESSAGE_TYPE_SENT_VOICE;
        }
        if (message.getType() == EMMessage.Type.VIDEO) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO : MESSAGE_TYPE_SENT_VIDEO;
        }
        if (message.getType() == EMMessage.Type.FILE) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_FILE : MESSAGE_TYPE_SENT_FILE;
        }
        return MESSAGE_TYPE_INVALID;// invalid
    }

    public void setCustomChatRowProvider(EaseCustomChatRowProvider rowProvider) {
        customRowProvider = rowProvider;
    }

    /**
     * set click listener
     *
     * @param listener
     */
    public void setItemClickListener(EaseMessageClickListener listener) {
        this.itemClickListener = listener;
    }

    private BasePresenter createPresenterByViewType(int viewType) {
//        if(customRowProvider != null && customRowProvider.getCustomChatRow(message, position, this) != null){
//            return customRowProvider.getCustomChatRow(message, position, this);
//        }

        switch (viewType) {
            case MESSAGE_TYPE_RECV_TXT:
                return new TextReceivePresenter(mContext);
            case MESSAGE_TYPE_SENT_TXT:
                return new TextSendPresenter(mContext);
            case MESSAGE_TYPE_RECV_EXPRESSION:
                return new BigExpressionReceivePresenter(mContext);
            case MESSAGE_TYPE_SENT_EXPRESSION:
                return new BigExpressionSendPresenter(mContext);
            case MESSAGE_TYPE_RECV_FILE:
                return new FileReceivePresenter(mContext);
            case MESSAGE_TYPE_SENT_FILE:
                return new FileSendPresenter(mContext);
            case MESSAGE_TYPE_RECV_LOCATION:
                return new LocationReceivePresenter(mContext);
            case MESSAGE_TYPE_SENT_LOCATION:
                return new LocationSendPresenter(mContext);
            case MESSAGE_TYPE_RECV_IMAGE:
                return new ImageReceivePresenter(mContext);
            case MESSAGE_TYPE_SENT_IMAGE:
                return new ImageSendPresenter(mContext);
            case MESSAGE_TYPE_RECV_VOICE:
                return new VoiceReceivePresenter(mContext);
            case MESSAGE_TYPE_SENT_VOICE:
                return new VoiceSendPresenter(mContext);
            case MESSAGE_TYPE_RECV_VIDEO:
                return new VideoReceivePresenter(mContext);
            case MESSAGE_TYPE_SENT_VIDEO:
                return new VideoSendPresenter(mContext);
        }
        return null;// invalid
    }

    public static class PresenterHolder extends RecyclerView.ViewHolder {
        private BasePresenter mPresenter;

        public PresenterHolder(BasePresenter presenter) {
            super(presenter.getContentView());
            mPresenter = presenter;
        }

        public void setup(EMMessage message, boolean showTimeStamp, @Nullable EaseMessageClickListener listener) {
            mPresenter.setup(message, showTimeStamp, listener);
        }

        public BasePresenter getPresenter() {
            return mPresenter;
        }
    }
}
