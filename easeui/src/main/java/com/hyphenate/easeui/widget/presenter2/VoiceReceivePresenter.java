package com.hyphenate.easeui.widget.presenter2;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.chatrow.EaseChatRowVoicePlayer;
import com.hyphenate.easeui.widget.chatrow2.BaseChatRow;
import com.hyphenate.easeui.widget.chatrow2.VoiceReceiveChatRow;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;

import java.io.File;

/**
 * Created by zhangsong on 17-12-5.
 */

public class VoiceReceivePresenter extends BaseReceivePresenter {
    private static final String TAG = "VoiceReceivePresenter";

    private EaseChatRowVoicePlayer voicePlayer;

    public VoiceReceivePresenter(Context context) {
        super(context);
        voicePlayer = EaseChatRowVoicePlayer.getInstance(context);
    }

    @Override
    protected BaseChatRow onCreateChatRow(Context context) {
        return new VoiceReceiveChatRow(context);
    }

    @Override
    public void onBubbleClick(EMMessage message) {
        String msgId = message.getMsgId();

        if (voicePlayer.isPlaying()) {
            // Stop the voice play first, no matter the playing voice item is this or others.
            voicePlayer.stop();
            // Stop the voice play animation.
            ((VoiceReceiveChatRow) getChatRow()).stopVoicePlayAnimation();

            // If the playing voice item is this item, only need stop play.
            String playingId = voicePlayer.getCurrentPlayingId();
            if (msgId.equals(playingId)) {
                return;
            }
        }

        final String st = getContext().getResources().getString(R.string.Is_download_voice_click_later);
        if (message.status() == EMMessage.Status.SUCCESS) {
            if (EMClient.getInstance().getOptions().getAutodownloadThumbnail()) {
                play(message);
            } else {
                EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) message.getBody();
                EMLog.i(TAG, "Voice body download status: " + voiceBody.downloadStatus());
                switch (voiceBody.downloadStatus()) {
                    case PENDING:// Download not begin
                    case FAILED:// Download failed
                        getChatRow().updateView(message);
                        asyncDownloadVoice(message);
                        break;
                    case DOWNLOADING:// During downloading
                        Toast.makeText(getContext(), st, Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESSED:// Download success
                        play(message);
                        break;
                }
            }
        } else if (message.status() == EMMessage.Status.INPROGRESS) {
            Toast.makeText(getContext(), st, Toast.LENGTH_SHORT).show();
        } else if (message.status() == EMMessage.Status.FAIL) {
            Toast.makeText(getContext(), st, Toast.LENGTH_SHORT).show();
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

    private void play(EMMessage message) {
        String localPath = ((EMVoiceMessageBody) message.getBody()).getLocalUrl();
        File file = new File(localPath);
        if (file.exists() && file.isFile()) {
            ackMessage(message);
            playVoice(message);
            // Start the voice play animation.
            ((VoiceReceiveChatRow) getChatRow()).startVoicePlayAnimation();
        } else {
            EMLog.e(TAG, "file not exist");
        }
    }

    protected void ackMessage(EMMessage message) {
        super.ackMessage(message);

        if (!message.isListened()) {
            EMClient.getInstance().chatManager().setVoiceMessageListened(message);
        }
    }

    private void playVoice(EMMessage msg) {
        voicePlayer.play(msg, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Stop the voice play animation.
                ((VoiceReceiveChatRow) getChatRow()).stopVoicePlayAnimation();
            }
        });
    }
}
