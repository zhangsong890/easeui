package com.hyphenate.easeui.widget.chatrow2.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.model.EaseImageCache;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.chatrow2.BaseSendChatRow;
import com.hyphenate.util.DateUtils;
import com.hyphenate.util.ImageUtils;
import com.hyphenate.util.TextFormater;

import java.io.File;

/**
 * Created by zhangsong on 17-12-5.
 */

public class VideoSendChatRow extends BaseSendChatRow {
    private static final String TAG = "VideoSendChatRow";

    private ImageView imageView;
    private TextView sizeView;
    private TextView timeLengthView;
    private TextView percentageView;

    public VideoSendChatRow(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ease_row_sent_video;
    }

    @Override
    protected void onViewInflate(View v) {
        super.onViewInflate(v);
        imageView = ((ImageView) v.findViewById(R.id.chatting_content_iv));
        sizeView = (TextView) v.findViewById(R.id.chatting_size_iv);
        timeLengthView = (TextView) v.findViewById(R.id.chatting_length_iv);
        percentageView = (TextView) v.findViewById(R.id.percentage);
    }

    @Override
    protected void onViewSetup(EMMessage message) {
        super.onViewSetup(message);
        EMVideoMessageBody videoBody = (EMVideoMessageBody) message.getBody();
        String localThumb = videoBody.getLocalThumb();

        if (localThumb != null) {
            showVideoThumbView(localThumb, imageView, videoBody.getThumbnailUrl(), message);
        }
        if (videoBody.getDuration() > 0) {
            String time = DateUtils.toTime(videoBody.getDuration());
            timeLengthView.setText(time);
        }

        if (videoBody.getLocalUrl() != null && new File(videoBody.getLocalUrl()).exists()) {
            String size = TextFormater.getDataSize(new File(videoBody.getLocalUrl()).length());
            sizeView.setText(size);
        }

        if (videoBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                videoBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING ||
                videoBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.FAILED) {
            progressBar.setVisibility(View.INVISIBLE);
            percentageView.setVisibility(View.INVISIBLE);
            imageView.setImageResource(R.drawable.ease_default_image);
        } else {
            progressBar.setVisibility(View.GONE);
            percentageView.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.ease_default_image);
            showVideoThumbView(localThumb, imageView, videoBody.getThumbnailUrl(), message);
        }
    }

    /**
     * show video thumbnails
     *
     * @param localThumb   local path for thumbnail
     * @param iv
     * @param thumbnailUrl Url on server for thumbnails
     * @param message
     */
    private void showVideoThumbView(final String localThumb, final ImageView iv, String thumbnailUrl, final EMMessage message) {
        // first check if the thumbnail image already loaded into cache
        Bitmap bitmap = EaseImageCache.getInstance().get(localThumb);
        if (bitmap != null) {
            // thumbnail image is already loaded, reuse the drawable
            iv.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.ease_default_image);
            new AsyncTask<Void, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Void... params) {
                    if (new File(localThumb).exists()) {
                        return ImageUtils.decodeScaleImage(localThumb, 160, 160);
                    } else {
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(Bitmap result) {
                    super.onPostExecute(result);
                    if (result != null) {
                        EaseImageCache.getInstance().put(localThumb, result);
                        iv.setImageBitmap(result);

                    } else {
                        if (message.status() == EMMessage.Status.FAIL) {
                            if (EaseCommonUtils.isNetWorkConnected(getContext())) {
                                EMClient.getInstance().chatManager().downloadThumbnail(message);
                            }
                        }

                    }
                }
            }.execute();
        }
    }
}
