package com.hyphenate.easeui.widget.chatrow2.voice;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.chatrow.EaseChatRowVoicePlayer;
import com.hyphenate.easeui.widget.chatrow2.BaseSendChatRow;

/**
 * Created by zhangsong on 17-12-5.
 */

public class VoiceSendChatRow extends BaseSendChatRow {
    private static final String TAG = "VoiceSendChatRow";

    private ImageView voiceImageView;
    private TextView voiceLengthView;
    private ImageView readStatusView;

    private AnimationDrawable voiceAnimation;

    public VoiceSendChatRow(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ease_row_sent_voice;
    }

    @Override
    protected void onViewInflate(View v) {
        super.onViewInflate(v);
        voiceImageView = ((ImageView) v.findViewById(R.id.iv_voice));
        voiceLengthView = (TextView) v.findViewById(R.id.tv_length);
        readStatusView = (ImageView) v.findViewById(R.id.iv_unread_voice);
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

        voiceImageView.setImageResource(R.drawable.ease_chatto_voice_playing);

        // To avoid the item is recycled by listview and slide to this item again but the animation is stopped.
        EaseChatRowVoicePlayer voicePlayer = EaseChatRowVoicePlayer.getInstance(getContext());
        if (voicePlayer.isPlaying() && message.getMsgId().equals(voicePlayer.getCurrentPlayingId())) {
            startVoicePlayAnimation();
        }
    }

    @Override
    protected void onViewUpdate(EMMessage message) {
        switch (message.status()) {
            case CREATE:
                progressBar.setVisibility(View.VISIBLE);
                break;
            case INPROGRESS:
                progressBar.setVisibility(View.VISIBLE);
                break;
            case FAIL:
                progressBar.setVisibility(View.INVISIBLE);
                break;
            case SUCCESS:
                progressBar.setVisibility(View.INVISIBLE);
                break;
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
