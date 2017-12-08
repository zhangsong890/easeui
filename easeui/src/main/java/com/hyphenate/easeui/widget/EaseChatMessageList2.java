package com.hyphenate.easeui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.adapter.EaseChatAdapter;
import com.hyphenate.easeui.model.styles.EaseMessageListItemStyle;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.easeui.widget.recyclerview.AbstractHeaderView;
import com.hyphenate.easeui.widget.recyclerview.DefaultRecyclerView;
import com.hyphenate.easeui.widget.recyclerview.Progress;
import com.hyphenate.easeui.widget.recyclerview.ProgressHeaderView;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EaseChatMessageList2 extends RelativeLayout implements EMMessageListener {
    protected static final String TAG = "EaseChatMessageList";

    protected DefaultRecyclerView recyclerView;

    protected EMConversation conversation;
    protected int chatType;
    protected String toChatUsername;
    protected EaseChatAdapter chatAdapter;

    protected List<EMMessage> loadedMessages = new ArrayList<>();
    protected EaseMessageListItemStyle itemStyle;
    private ExecutorService fetchQueue;
    private boolean isRoaming = false;
    protected boolean haveMoreData = true;
    protected int pagesize = 20;

    private Handler handler = new Handler();

    public EaseChatMessageList2(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public EaseChatMessageList2(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseStyle(context, attrs);
        init(context);
    }

    public EaseChatMessageList2(Context context) {
        super(context);
        init(context);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        recyclerView.setOnTouchListener(l);
    }

    // implement methods in EMMessageListener
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        for (EMMessage message : messages) {
            String username = null;
            // group message
            if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                username = message.getTo();
            } else {
                // single chat message
                username = message.getFrom();
            }

            // if the message is for current conversation
            if (username.equals(toChatUsername) || message.getTo().equals(toChatUsername) || message.conversationId().equals(toChatUsername)) {
                refreshSelectLast();
                EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
                conversation.markMessageAsRead(message.getMsgId());
            } else {
                EaseUI.getInstance().getNotifier().onNewMsg(message);
            }
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {
        refresh();
    }

    @Override
    public void onMessageDelivered(List<EMMessage> messages) {
        refresh();
    }

    @Override
    public void onMessageRecalled(List<EMMessage> messages) {
        refresh();
    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object change) {
        refresh();
    }

    /**
     * init widget
     *
     * @param toChatUsername
     * @param chatType
     * @param customChatRowProvider
     */
    public void init(String toChatUsername, int chatType, EaseCustomChatRowProvider customChatRowProvider) {
        this.chatType = chatType;
        this.toChatUsername = toChatUsername;

        chatAdapter = new EaseChatAdapter(getContext(), loadedMessages);
        // TODO
//        chatAdapter.setItemStyle(itemStyle);
//        messageAdapter.setCustomChatRowProvider(customChatRowProvider);
        // set message adapter
        recyclerView.setAdapter(chatAdapter);

        refresh();
    }

    public void initConversation() {
        conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(chatType), true);
        conversation.markAllMessagesAsRead();
        // the number of messages loaded into conversation is getChatOptions().getNumberOfMessagesLoaded
        // you can change this number

        if (!isRoaming) {
            final List<EMMessage> msgs = conversation.getAllMessages();
            int msgCount = msgs != null ? msgs.size() : 0;
            if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
                String msgId = null;
                if (msgs != null && msgs.size() > 0) {
                    msgId = msgs.get(0).getMsgId();
                }
                conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
            }
            refreshSelectLast();
        } else {
            fetchQueue.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().chatManager().fetchHistoryMessages(
                                toChatUsername, EaseCommonUtils.getConversationType(chatType), pagesize, "");
                        final List<EMMessage> msgs = conversation.getAllMessages();
                        int msgCount = msgs != null ? msgs.size() : 0;
                        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
                            String msgId = null;
                            if (msgs != null && msgs.size() > 0) {
                                msgId = msgs.get(0).getMsgId();
                            }
                            conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
                        }
                        refreshSelectLast();
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public EMConversation getConversition() {
        return conversation;
    }

    /**
     * set click listener
     *
     * @param listener
     */
    public void setItemClickListener(EaseMessageClickListener listener) {
        chatAdapter.setItemClickListener(listener);
    }

    /**
     * set chat row provider
     *
     * @param rowProvider
     */
    public void setCustomChatRowProvider(EaseCustomChatRowProvider rowProvider) {
        chatAdapter.setCustomChatRowProvider(rowProvider);
    }

    public void setRoaming(boolean isRoaming) {
        this.isRoaming = isRoaming;

        if (isRoaming) {
            fetchQueue = Executors.newSingleThreadExecutor();
        }
    }

    public void setShowUserNick(boolean show) {
        itemStyle.setShowUserNick(show);
    }

    public void emptyHistory() {
        String msg = getResources().getString(R.string.Whether_to_empty_all_chats);
        new EaseAlertDialog(getContext(), null, msg, null, new EaseAlertDialog.AlertDialogUser() {

            @Override
            public void onResult(boolean confirmed, Bundle bundle) {
                if (confirmed) {
                    if (conversation != null) {
                        conversation.clearAllMessages();
                    }
                    refresh();
                }
            }
        }, true).show();
    }

    public void scrollToLast() {
        recyclerView.scrollToPosition(loadedMessages.size() - 1);
    }

    public void scrollToPosition(int position) {
        recyclerView.scrollToPosition(position);
    }

    public void refresh() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                loadAndNotify();
            }
        });
    }

    public void refreshSelectLast() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                loadAndNotify();

                recyclerView.scrollToPosition(loadedMessages.size() - 1);
            }
        });
    }

    public void refreshSeekTo(final int position) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                loadAndNotify();

                recyclerView.scrollToPosition(position);
            }
        });
    }

    private void loadAndNotify() {
        loadedMessages.clear();
        loadedMessages.addAll(conversation.getAllMessages());
        chatAdapter.notifyDataSetChanged();
    }

    protected void parseStyle(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EaseChatMessageList);
        EaseMessageListItemStyle.Builder builder = new EaseMessageListItemStyle.Builder();
        builder.showAvatar(ta.getBoolean(R.styleable.EaseChatMessageList_msgListShowUserAvatar, true))
                .showUserNick(ta.getBoolean(R.styleable.EaseChatMessageList_msgListShowUserNick, false))
                .myBubbleBg(ta.getDrawable(R.styleable.EaseChatMessageList_msgListMyBubbleBackground))
                .otherBuddleBg(ta.getDrawable(R.styleable.EaseChatMessageList_msgListMyBubbleBackground));

        itemStyle = builder.build();
        ta.recycle();
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.ease_chat_message_list2, this);
        recyclerView = (DefaultRecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHeaderView(new ProgressHeaderView(getContext()), Progress.BallSpinFadeLoader);
        recyclerView.setHeaderListener(new AbstractHeaderView.HeaderListener() {
            @Override
            public void onLoading() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isRoaming) {
                            loadMoreLocalMessage();
                        } else {
                            loadMoreRoamingMessages();
                        }
                    }
                }, 500);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private synchronized void loadMoreLocalMessage() {
        if (haveMoreData) {
            List<EMMessage> messages;
            try {
                messages = conversation.loadMoreMsgFromDB(conversation.getAllMessages().size() == 0 ? "" : conversation.getAllMessages().get(0).getMsgId(),
                        pagesize);
            } catch (Exception e1) {
                recyclerView.finishLoading();
                return;
            }
            Log.i(TAG, "loadMoreLocalMessage: " + messages.size());
            if (messages.size() > 0) {
                refreshSeekTo(messages.size() - 1);
                if (messages.size() != pagesize) {
                    haveMoreData = false;
                }
            } else {
                haveMoreData = false;
            }
        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.no_more_messages),
                    Toast.LENGTH_SHORT).show();
        }

        recyclerView.finishLoading();
    }

    private synchronized void loadMoreRoamingMessages() {
        if (!haveMoreData) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_more_messages),
                    Toast.LENGTH_SHORT).show();
            recyclerView.finishLoading();
            return;
        }

        if (fetchQueue != null) {
            fetchQueue.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<EMMessage> messages = conversation.getAllMessages();
                        EMClient.getInstance().chatManager().fetchHistoryMessages(
                                toChatUsername, EaseCommonUtils.getConversationType(chatType), pagesize,
                                (messages != null && messages.size() > 0) ? messages.get(0).getMsgId() : "");
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    } finally {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                loadMoreLocalMessage();
                            }
                        });
                    }
                }
            });
        }
    }
}
