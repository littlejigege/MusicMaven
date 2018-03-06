package com.qg.musicmaven;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.media.app.NotificationCompat.MediaStyle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.mobile.utils.ToastUtilsKt;
import com.qg.musicmaven.ui.mainpage.TestMainActivity;

import java.util.concurrent.ExecutionException;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

/**
 * Created by jimiji on 2018/3/3.
 */

public class MediaNotificationManager {
    public static final int NOTIFICATION_ID = 412;
    private final MyMusicService mService;
    private final NotificationManager mNotificationManager;
    private static final String CHANNEL_ID = "com.qg.maven.channel";
    private static final int REQUEST_CODE = 501;
    private final NotificationCompat.Action mPauseAction;
    private final NotificationCompat.Action mPlayAction;
    private final NotificationCompat.Action mStopAction;

    public NotificationManager getmNotificationManager() {
        return mNotificationManager;
    }

    public MediaNotificationManager(MyMusicService service) {
        mService = service;
        mNotificationManager = (NotificationManager) mService.getSystemService(Context.NOTIFICATION_SERVICE);

        mPauseAction = new NotificationCompat.Action(
                R.drawable.ic_pause_black_24dp,
                "pause",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                        mService,
                        PlaybackStateCompat.ACTION_PAUSE));
        mPlayAction = new NotificationCompat.Action(
                R.drawable.ic_play_arrow_black_24dp,
                "play",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                        mService,
                        PlaybackStateCompat.ACTION_PLAY));
        mStopAction = new NotificationCompat.Action(
                R.drawable.ic_stop_black_24dp,
                "stop",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                        mService,
                        PlaybackStateCompat.ACTION_STOP));

        mNotificationManager.cancelAll();
    }

    public Notification getNotification(String songName, String singer, String imageUrl, MediaSessionCompat.Token token, int state) {
        NotificationCompat.Builder builder = buildNotification(songName, singer, imageUrl, token, state);
        return builder.build();
    }

    private NotificationCompat.Builder buildNotification(String songName, String singer, final String imageUrl, MediaSessionCompat.Token token, int state) {
        if (isAndroidOOrHigher()) {
            createChannel();
        }
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(mService, CHANNEL_ID);
        try {
            builder.setStyle(new MediaStyle()
                    .setMediaSession(token)
                    .setShowActionsInCompactView(0, 1)
                    .setShowCancelButton(true)
                    .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(
                            mService,
                            PlaybackStateCompat.ACTION_STOP)))
                    .setColor(ContextCompat.getColor(mService, R.color.notification_bg))
                    .setSmallIcon(R.drawable.ic_music)
                    //设置点击通知效果
                    //.setContentIntent(createContentIntent())
                    .setContentTitle(songName)
                    .setContentText(singer)
                    //通过Glide获取图片
                    .setLargeIcon(BitmapFactory.decodeFile(Glide.with(mService).load(imageUrl).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get().getPath()))
                    .setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(
                            mService, PlaybackStateCompat.ACTION_STOP))
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (state == PlaybackStateCompat.STATE_PAUSED) {
            builder.addAction(mPlayAction);
        } else {
            builder.addAction(mPauseAction);

        }
        builder.addAction(mStopAction);


        return builder;
    }

    private boolean isAndroidOOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel() {
        if (mNotificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            // The user-visible name of the channel.
            CharSequence name = "MediaSession";
            // The user-visible description of the channel.
            String description = "MediaSession and MediaPlayer";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(
                    new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);

        } else {

        }
    }


}
