package com.sildev.musiclocal;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.sildev.musiclocal.MediaPlayer.PlayManager;
import com.sildev.musiclocal.datalocal.DataManager;
import com.sildev.musiclocal.model.Song;

public class MyApplication extends Application {
    public static final String CHANNEL_ID = "CHANNEL_PLAY_MUSIC";
    @SuppressLint("StaticFieldLeak")
    public static PlayManager playManager;

    @Override
    public void onCreate() {
        super.onCreate();
        DataManager.initManager(getApplicationContext());
        playManager = new PlayManager(getApplicationContext());
        Song currentSong = DataManager.getCurrentSong();
        if (currentSong != null) {
            playManager.setSongList(DataManager.getListSong());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("Music player");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

}
