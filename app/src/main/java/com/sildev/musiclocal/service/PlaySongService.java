package com.sildev.musiclocal.service;


import static com.sildev.musiclocal.MyApplication.playManager;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.MediaMetadata;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.media.app.NotificationCompat;

import com.sildev.musiclocal.MusicConstants;
import com.sildev.musiclocal.MyApplication;
import com.sildev.musiclocal.R;
import com.sildev.musiclocal.activity.MainActivity;
import com.sildev.musiclocal.datalocal.DataManager;
import com.sildev.musiclocal.model.Song;

public class PlaySongService extends Service {
    private BroadcastReceiver broadcastReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        playManager.playSong(playManager.getPosition());
        showNotification();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (intent.getAction()) {
                            case MusicConstants.ACTION_NEXT:
                                playManager.nextSong();
                                break;
                            case MusicConstants.ACTION_PAUSE:
                                playManager.pause();
                                break;
                            case MusicConstants.ACTION_PREVIOUS:
                                playManager.previousSong();
                                break;
                        }
                        updateNotification();
                    }
                }, 50);
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicConstants.ACTION_PREVIOUS);
        intentFilter.addAction(MusicConstants.ACTION_PLAY);
        intentFilter.addAction(MusicConstants.ACTION_PAUSE);
        intentFilter.addAction(MusicConstants.ACTION_NEXT);
        registerReceiver(broadcastReceiver, intentFilter);

        return START_NOT_STICKY;
    }

    private void updateNotification() {
        Song song = DataManager.getCurrentSong();
        androidx.core.app.NotificationCompat.Builder mBuilder = new androidx.core.app.NotificationCompat.Builder(this, MyApplication.CHANNEL_ID);
        MediaSessionCompat mMediaSessionCompat = new MediaSessionCompat(this, "12313");
        mMediaSessionCompat.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadata.METADATA_KEY_TITLE, song.getName())
                .putString(MediaMetadata.METADATA_KEY_ARTIST, song.getSinger())
                .build());
        Intent resumeIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingResumeIntent = PendingIntent.getBroadcast(this, 12345, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        int ic_pause_play = playManager.isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play;
        mBuilder.addAction(R.drawable.ic_previous, "rw30", pendingIntentPrevious())
                .addAction(ic_pause_play, "Pause", pendingIntentPause())
                .addAction(R.drawable.ic_next, "ff30", pendingIntentNext())
                .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", null)
                .setContentTitle(song.getName())
                .setContentText(song.getSinger())
                .setStyle(new NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                        .setMediaSession(mMediaSessionCompat.getSessionToken()))
                .setSmallIcon(R.drawable.ic_disc)
                .setLargeIcon(BitmapFactory.decodeFile(song.getAlbumArt()))
                .setPriority(Notification.PRIORITY_LOW)
                .setContentIntent(pendingResumeIntent);
        NotificationManagerCompat.from(this).notify(12345, mBuilder.build());

    }

    private void showNotification() {
        Song song = DataManager.getCurrentSong();
        Intent resumeIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingResumeIntent = PendingIntent.getBroadcast(this, 12345, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        androidx.core.app.NotificationCompat.Builder mBuilder = new androidx.core.app.NotificationCompat.Builder(this, MyApplication.CHANNEL_ID);
        MediaSessionCompat mMediaSessionCompat = new MediaSessionCompat(this, "12313");
        mMediaSessionCompat.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadata.METADATA_KEY_TITLE, song.getName())
                .putString(MediaMetadata.METADATA_KEY_ARTIST, song.getSinger())
                .build());

        int ic_pause_play = playManager.isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play;
        mBuilder.addAction(R.drawable.ic_previous, "rw30", pendingIntentPrevious())
                .addAction(ic_pause_play, "Pause", pendingIntentPause())
                .addAction(R.drawable.ic_next, "ff30", pendingIntentNext())
                .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", null)
                .setStyle(new NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                        .setMediaSession(mMediaSessionCompat.getSessionToken()))
                .setContentTitle(song.getName())
                .setContentText(song.getSinger())
                .setSmallIcon(R.drawable.ic_disc)
                .setLargeIcon(BitmapFactory.decodeFile(song.getAlbumArt()))
                .setPriority(Notification.PRIORITY_LOW)
                .setContentIntent(pendingResumeIntent);
        startForeground(12345, mBuilder.build());
    }

    public PendingIntent pendingIntentNext() {
        Intent intent = new Intent();
        intent.setAction(MusicConstants.ACTION_NEXT);
        return PendingIntent.getBroadcast(this, 12, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public PendingIntent pendingIntentPrevious() {
        Intent intent = new Intent();
        intent.setAction(MusicConstants.ACTION_PREVIOUS);
        return PendingIntent.getBroadcast(this, 123, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public PendingIntent pendingIntentPause() {
        Intent intent = new Intent();
        intent.setAction(MusicConstants.ACTION_PAUSE);
        return PendingIntent.getBroadcast(this, 1233, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        DataManager.deleteData();
    }
}
