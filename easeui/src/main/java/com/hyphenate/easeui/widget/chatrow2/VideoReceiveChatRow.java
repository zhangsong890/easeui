package com.hyphenate.easeui.widget.chatrow2;

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
import com.hyphenate.util.DateUtils;
import com.hyphenate.util.ImageUtils;
import com.hyphenate.util.TextFormater;

import java.io.File;

/**
 * Created by zhangsong on 17-12-5.
 */

public class VideoReceiveChatRow extends FileReceiveChatRow {
    private static final String TAG = "VideoReceiveChatRow";

    private ImageView imageView;
    private TextView sizeView;
    private TextView timeLengthView;

    public VideoReceiveChatRow(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ease_row_received_video;
    }

    @Override
    protected void onViewInflate(View v) {
        super.onViewInflate(v);
        imageView = ((ImageView) v.findViewById(R.id.chatting_content_iv));
        sizeView = (TextView) v.findViewById(R.id.chatting_size_iv);
        timeLengthView = (TextView) v.findViewById(R.id.chatting_length_iv);
    }

    @Override
    protected void onViewSetup(EMMessage message) {
        EMVideoMessageBody videoBody = (EMVideoMessageBody) message.getBody();
        String localThumb = videoBody.getLocalThumb();

        if (localThumb != null) {
            showVideoThumbView(localThumb, imageView, videoBody.getThumbnailUrl(), message);
        }
        if (videoBody.getDuration() > 0) {
            String time = DateUtils.toTime(videoBody.getDuration());
            timeLengthView.setText(time);
        }
        if (videoBody.getVideoFileLength() > 0) {
            String size = TextFormater.getDataSize(videoBody.getVideoFileLength());
            sizeView.setText(size);
        }
        if (videoBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                videoBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
            imageView.setImageResource(R.drawable.ease_default_image);
        } else {
            // System.err.println("!!!! not back receive, show image directly");
            imageView.setImageResource(R.drawable.ease_default_image);
            if (localThumb != null) {
                showVideoThumbView(localThumb, imageView, videoBody.getThumbnailUrl(), message);
            }
        }
    }

    @Override
    protected void onViewUpdate(EMMessage message) {
        super.onViewUpdate(message);
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
