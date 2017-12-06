package com.hyphenate.easeui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.easeui.widget.presenter2.BasePresenter;
import com.hyphenate.easeui.widget.presenter2.BigExpressionReceivePresenter;
import com.hyphenate.easeui.widget.presenter2.BigExpressionSendPresenter;
import com.hyphenate.easeui.widget.presenter2.FileReceivePresenter;
import com.hyphenate.easeui.widget.presenter2.FileSendPresenter;
import com.hyphenate.easeui.widget.presenter2.ImageReceivePresenter;
import com.hyphenate.easeui.widget.presenter2.ImageSendPresenter;
import com.hyphenate.easeui.widget.presenter2.LocationReceivePresenter;
import com.hyphenate.easeui.widget.presenter2.LocationSendPresenter;
import com.hyphenate.easeui.widget.presenter2.TextReceivePresenter;
import com.hyphenate.easeui.widget.presenter2.TextSendPresenter;
import com.hyphenate.easeui.widget.presenter2.VideoReceivePresenter;
import com.hyphenate.easeui.widget.presenter2.VideoSendPresenter;
import com.hyphenate.easeui.widget.presenter2.VoiceReceivePresenter;
import com.hyphenate.easeui.widget.presenter2.VoiceSendPresenter;
import com.hyphenate.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangsong on 17-11-30.
 */

public class EaseChatAdapter extends RecyclerView.Adapter<EaseChatAdapter.PresenterHolder> {
    private static final String TAG = "EaseChatAdapter";

    private static final int MSG_REFRESH_LIST = 0;
    private static final int MSG_SELECT_LAST = 1;
    private static final int MSG_SEEK_TO = 2;

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
    private EaseChatMessageList.MessageListItemClickListener itemClickListener;
//    private EMConversation conversation;

//    private Handler handler;

    public EaseChatAdapter(Context context, String toUsername, int chatType) {
        this.mContext = context;

//        this.conversation = EMClient.getInstance().chatManager().getConversation(toUsername,
//                EaseCommonUtils.getConversationType(chatType), true);

//        initHandler();
    }

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
    public void setItemClickListener(EaseChatMessageList.MessageListItemClickListener listener) {
        this.itemClickListener = listener;
    }

    //    public abstract void scrollToPosition(int position);

//    public void refresh() {
//        Log.i(TAG, "refresh: ");
//        if (handler.hasMessages(MSG_REFRESH_LIST)) {
//            return;
//        }
//        android.os.Message msg = handler.obtainMessage(MSG_REFRESH_LIST);
//        handler.sendMessage(msg);
//    }
//
//    /**
//     * refresh and select the last
//     */
//    public void refreshSelectLast() {
//        Log.i(TAG, "refreshSelectLast: ");
//        handler.removeMessages(MSG_REFRESH_LIST);
//        handler.removeMessages(MSG_SELECT_LAST);
//        handler.sendEmptyMessage(MSG_REFRESH_LIST);
//        handler.sendEmptyMessage(MSG_SELECT_LAST);
//    }
//
//    /**
//     * refresh and seek to the position
//     */
//    public void refreshSeekTo(int position) {
//        handler.removeMessages(MSG_REFRESH_LIST);
//        handler.removeMessages(MSG_SEEK_TO);
//
//        handler.sendEmptyMessage(MSG_REFRESH_LIST);
//        Message message = handler.obtainMessage(MSG_SEEK_TO);
//        message.arg1 = position;
//        message.sendToTarget();
//    }

//    private void initHandler() {
//        handler = new Handler(Looper.getMainLooper()) {
//            private void refreshList() {
//                // you should not call getAllMessages() in UI thread
//                // otherwise there is problem when refreshing UI and there is new message arrive
//                messages.clear();
//                messages.addAll(conversation.getAllMessages());
//                conversation.markAllMessagesAsRead();
//                notifyDataSetChanged();
//            }
//
//            @Override
//            public void handleMessage(Message msg) {
//                switch (msg.what) {
//                    case MSG_REFRESH_LIST:
//                        refreshList();
//                        break;
//                    case MSG_SELECT_LAST:
//                        scrollToPosition(messages.size() - 1);
//                        break;
//                    case MSG_SEEK_TO:
//                        int position = msg.arg1;
//                        // TODO: scroll the view to a special position
//                        scrollToPosition(position);
//                        break;
//                    default:
//                        super.handleMessage(msg);
//                        break;
//                }
//            }
//        };
//    }

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

        public void setup(EMMessage message, boolean showTimeStamp, @Nullable EaseChatMessageList.MessageListItemClickListener listener) {
            mPresenter.setup(message, showTimeStamp, listener);
        }

        public BasePresenter getPresenter() {
            return mPresenter;
        }
    }
}
