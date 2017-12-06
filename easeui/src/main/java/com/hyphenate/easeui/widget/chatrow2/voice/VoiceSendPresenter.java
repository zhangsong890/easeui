package com.hyphenate.easeui.widget.chatrow2.voice;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.easeui.widget.chatrow.EaseChatRowVoicePlayer;
import com.hyphenate.easeui.widget.chatrow2.BaseChatRow;
import com.hyphenate.easeui.widget.chatrow2.BaseSendPresenter;

import java.io.File;

/**
 * Created by zhangsong on 17-12-5.
 */

public class VoiceSendPresenter extends BaseSendPresenter {
    private static final String TAG = "VoiceSendPresenter";

    private EaseChatRowVoicePlayer voicePlayer;

    public VoiceSendPresenter(Context context) {
        super(context);
        voicePlayer = EaseChatRowVoicePlayer.getInstance(context);
    }

    @Override
    protected BaseChatRow onCreateChatRow(Context context) {
        return new VoiceSendChatRow(context);
    }

    @Override
    public void onBubbleClick(EMMessage message) {
        String msgId = message.getMsgId();

        if (voicePlayer.isPlaying()) {
            // Stop the voice play first, no matter the playing voice item is this or others.
            voicePlayer.stop();
            // Stop the voice play animation.
            ((VoiceSendChatRow) getChatRow()).stopVoicePlayAnimation();

            // If the playing voice item is this item, only need stop play.
            String playingId = voicePlayer.getCurrentPlayingId();
            if (msgId.equals(playingId)) {
                return;
            }
        }

        // Play the voice
        String localPath = ((EMVoiceMessageBody) message.getBody()).getLocalUrl();
        File file = new File(localPath);
        if (file.exists() && file.isFile()) {
            playVoice(message);
            // Start the voice play animation.
            ((VoiceSendChatRow) getChatRow()).startVoicePlayAnimation();
        } else {
            asyncDownloadVoice(message);
        }
    }

    @Override
    public void onViewDetachedFromWindow() {
        super.onViewDetachedFromWindow();
        if (voicePlayer.isPlaying()) {
            voicePlayer.stop();
        }
    }

    private void asyncDownloadVoice(final EMMessage message) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                EMClient.getInstance().chatManager().downloadAttachment(message);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                getChatRow().updateView(message);
            }
        }.execute();
    }

    private void playVoice(EMMessage msg) {
        voicePlayer.play(msg, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Stop the voice play animation.
                ((VoiceSendChatRow) getChatRow()).stopVoicePlayAnimation();
            }
        });
    }
}
