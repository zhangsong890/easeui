package com.hyphenate.easeui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.widget.presenter2.BasePresenter;
import com.hyphenate.easeui.widget.presenter2.TextReceiveRowPresenter;
import com.hyphenate.easeui.widget.presenter2.TextSendRowPresenter;

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
        holder.getPresenter().setup(messages.get(position));
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
        switch (viewType) {
            case MESSAGE_TYPE_RECV_TXT:
                return new TextReceiveRowPresenter(mContext);
            case MESSAGE_TYPE_SENT_TXT:
                return new TextSendRowPresenter(mContext);
        }
        return null;// invalid
    }

    public static class PresenterHolder extends RecyclerView.ViewHolder {
        private BasePresenter mPresenter;

        public PresenterHolder(BasePresenter presenter) {
            super(presenter.getContentView());
            mPresenter = presenter;
        }

        public BasePresenter getPresenter() {
            return mPresenter;
        }
    }
}
