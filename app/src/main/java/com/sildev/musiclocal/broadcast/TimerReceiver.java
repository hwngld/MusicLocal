package com.sildev.musiclocal.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sildev.musiclocal.MusicConstants;
import com.sildev.musiclocal.MyApplication;

public class TimerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (MyApplication.playManager.isPlaying()) {
            context.sendBroadcast(new Intent(MusicConstants.ACTION_PAUSE));
        }
    }
}
