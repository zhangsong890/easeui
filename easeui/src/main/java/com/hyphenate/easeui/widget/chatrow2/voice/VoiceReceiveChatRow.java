package com.hyphenate.easeui.widget.chatrow2.voice;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.chatrow2.BaseReceiveChatRow;
import com.hyphenate.util.EMLog;

/**
 * Created by zhangsong on 17-12-5.
 */

public class VoiceReceiveChatRow extends BaseReceiveChatRow {
    private static final String TAG = "VoiceReceiveChatRow";

    private ImageView voiceImageView;
    private TextView voiceLengthView;
    private ImageView readStatusView;
    protected ProgressBar progressBar;

    private AnimationDrawable voiceAnimation;

    public VoiceReceiveChatRow(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ease_row_received_voice;
    }

    @Override
    protected void onViewInflate(View v) {
        super.onViewInflate(v);
        voiceImageView = ((ImageView) v.findViewById(R.id.iv_voice));
        voiceLengthView = (TextView) v.findViewById(R.id.tv_length);
        readStatusView = (ImageView) v.findViewById(R.id.iv_unread_voice);
        progressBar = (ProgressBar) v.findViewById(com.hyphenate.easeui.R.id.progress_bar);
    }

    @Override
    protected void onViewSetup(EMMessage message) {
        EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) message.getBody();
        int len = voiceBody.getLength();
        if (len > 0) {
            voiceLengthView.setText(voiceBody.getLength() + "\"");
            voiceLengthView.setVisibility(View.VISIBLE);
        } else {
            voiceLengthView.setVisibility(View.INVISIBLE);
        }

        voiceImageView.setImageResource(R.drawable.ease_chatfrom_voice_playing);

        if (message.isListened()) {
            // hide the unread icon
            readStatusView.setVisibility(View.INVISIBLE);
        } else {
            readStatusView.setVisibility(View.VISIBLE);
        }
        EMLog.d(TAG, "it is receive msg");
        if (voiceBody.downloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                voiceBody.downloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
            if (EMClient.getInstance().getOptions().getAutodownloadThumbnail()) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }

        // To avoid the item is recycled by listview and slide to this item again but the animation is stopped.
        EaseVoicePlayer voicePlayer = EaseVoicePlayer.getInstance(getContext());
        if (voicePlayer.isPlaying() && voicePlayer.isCurrentPlayingMessage(message)) {
            startVoicePlayAnimation();
        }
    }

    @Override
    protected void onViewUpdate(EMMessage message) {
        EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) message.getBody();
        if (voiceBody.downloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                voiceBody.downloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void startVoicePlayAnimation() {
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            voiceImageView.setImageResource(R.anim.voice_from_icon);
        } else {
            voiceImageView.setImageResource(R.anim.voice_to_icon);
        }
        voiceAnimation = (AnimationDrawable) voiceImageView.getDrawable();
        voiceAnimation.start();

        // Hide the voice item not listened status view.
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            readStatusView.setVisibility(View.INVISIBLE);
        }
    }

    public void stopVoicePlayAnimation() {
        if (voiceAnimation != null) {
            voiceAnimation.stop();
        }

        if (message.direct() == EMMessage.Direct.RECEIVE) {
            voiceImageView.setImageResource(R.drawable.ease_chatfrom_voice_playing);
        } else {
            voiceImageView.setImageResource(R.drawable.ease_chatto_voice_playing);
        }
    }
}
