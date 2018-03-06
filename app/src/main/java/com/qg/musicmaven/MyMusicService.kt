package com.qg.musicmaven

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.media.AudioManager
import android.os.Bundle
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaBrowserServiceCompat
import android.support.v4.media.session.MediaButtonReceiver
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.MediaController
import com.mobile.utils.showToast

import java.util.ArrayList
import kotlin.concurrent.thread

/**
 * This class provides a MediaBrowser through a service. It exposes the media library to a browsing
 * client, through the onGetRoot and onLoadChildren methods. It also creates a MediaSession and
 * exposes it through its MediaSession.Token, which allows the client to create a MediaController
 * that connects to and send control commands to the MediaSession remotely. This is useful for
 * user interfaces that need to interact with your media session, like Android Auto. You can
 * (should) also use the same service from your app's UI, which gives a seamless playback
 * experience to the user.
 *
 *
 * To implement a MediaBrowserService, you need to:
 *
 *
 *
 *
 *
 *  *  Extend [MediaBrowserServiceCompat], implementing the media browsing
 * related methods [MediaBrowserServiceCompat.onGetRoot] and
 * [MediaBrowserServiceCompat.onLoadChildren];
 *  *  In onCreate, start a new [MediaSessionCompat] and notify its parent
 * with the session's token [MediaBrowserServiceCompat.setSessionToken];
 *
 *
 *  *  Set a callback on the [MediaSessionCompat.setCallback].
 * The callback will receive all the user's actions, like play, pause, etc;
 *
 *
 *  *  Handle all the actual music playing using any method your app prefers (for example,
 * [android.media.MediaPlayer])
 *
 *
 *  *  Update playbackState, "now playing" metadata and queue, using MediaSession proper methods
 * [MediaSessionCompat.setPlaybackState]
 * [MediaSessionCompat.setMetadata] and
 * [MediaSessionCompat.setQueue])
 *
 *
 *  *  Declare and export the service in AndroidManifest with an intent receiver for the action
 * android.media.browse.MediaBrowserService
 *
 *
 *
 *
 *
 * To make your app compatible with Android Auto, you also need to:
 *
 *
 *
 *
 *
 *  *  Declare a meta-data tag in AndroidManifest.xml linking to a xml resource
 * with a &lt;automotiveApp&gt; root element. For a media app, this must include
 * an &lt;uses name="media"/&gt; element as a child.
 * For example, in AndroidManifest.xml:
 * &lt;meta-data android:name="com.google.android.gms.car.application"
 * android:resource="@xml/automotive_app_desc"/&gt;
 * And in res/values/automotive_app_desc.xml:
 * &lt;automotiveApp&gt;
 * &lt;uses name="media"/&gt;
 * &lt;/automotiveApp&gt;
 *
 *
 *
 */
class MyMusicService : MediaBrowserServiceCompat() {
    companion object {
        var url: String = ""
        var singer: String = ""
        var songName: String = ""
        var imageUrl: String = ""
    }

    private var mMediaSessionCompat: MediaSessionCompat? = null
    private val notificationManager: MediaNotificationManager by lazy { MediaNotificationManager(this) }
    //耳机拔掉暂停播放
    private val mNoisyReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (App.player.isPlaying) {
                if(App.instance.musicController!=null){
                    App.instance.musicController?.transportControls?.pause()
                } else App.player.pause()
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MediaButtonReceiver.handleIntent(mMediaSessionCompat, intent)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        initMediaSession()
        initNoisyReceiver()
        App.instance.musicController = MediaControllerCompat(this, sessionToken!!)
    }

    private fun initMediaSession() {
        val mediaButtonReceiver = ComponentName(applicationContext, MediaButtonReceiver::class.java)
        mMediaSessionCompat = MediaSessionCompat(applicationContext, "Tag", mediaButtonReceiver, null)
        mMediaSessionCompat!!.setCallback(MediaSessionCallback())
        mMediaSessionCompat!!.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
        val mediaButtonIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
        mediaButtonIntent.setClass(this, MediaButtonReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, mediaButtonIntent, 0)
        mMediaSessionCompat!!.setMediaButtonReceiver(pendingIntent)
        sessionToken = mMediaSessionCompat!!.sessionToken
    }

    private fun initNoisyReceiver() {
        val filter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        registerReceiver(mNoisyReceiver, filter)
    }

    override fun onDestroy() {
        mMediaSessionCompat!!.release()
    }

    override fun onGetRoot(clientPackageName: String,
                           clientUid: Int,
                           rootHints: Bundle?): MediaBrowserServiceCompat.BrowserRoot? {
        return MediaBrowserServiceCompat.BrowserRoot("root", null)
    }

    override fun onLoadChildren(parentMediaId: String,
                                result: MediaBrowserServiceCompat.Result<List<MediaItem>>) {
        result.sendResult(ArrayList())
    }

    private inner class MediaSessionCallback : MediaSessionCompat.Callback() {
        override fun onPlay() {
            thread {
                try {
                    App.player.reset()
                    App.player.setDataSource(url)
                    App.player.prepare()
                    App.player.start()
                    App.player.setOnCompletionListener {
                        App.instance.musicController?.transportControls?.stop()
                    }
                    setMediaPlaybackState(PlaybackStateCompat.STATE_PLAYING)
                    startForeground(
                            MediaNotificationManager.NOTIFICATION_ID,
                            notificationManager.getNotification(
                                    songName,
                                    singer,
                                    imageUrl,
                                    sessionToken,
                                    PlaybackStateCompat.STATE_PLAYING))
                } catch (e: Exception) {
                    showToast("播放地址无效")
                    App.instance.musicController?.transportControls?.stop()
                }

            }
        }

        override fun onStop() {
            App.player.stop()
            stopForeground(true)
        }

        override fun onPause() {
            App.player.pause()
            setMediaPlaybackState(PlaybackStateCompat.STATE_PAUSED)
            thread {
                NotificationManagerCompat.from(this@MyMusicService).notify(
                        MediaNotificationManager.NOTIFICATION_ID,
                        notificationManager.getNotification(
                                songName,
                                singer,
                                imageUrl,
                                sessionToken,
                                PlaybackStateCompat.STATE_PAUSED))
            }

            super.onPause()
        }
    }

    private fun setMediaPlaybackState(state: Int) {
        val playbackstateBuilder = PlaybackStateCompat.Builder()
        if (state == PlaybackStateCompat.STATE_PLAYING) {
            playbackstateBuilder.setActions(PlaybackStateCompat.ACTION_STOP or PlaybackStateCompat.ACTION_PAUSE)
        } else {
            playbackstateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_PLAY)
        }
        playbackstateBuilder.setState(state, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0f)
        mMediaSessionCompat!!.setPlaybackState(playbackstateBuilder.build())
    }

}
