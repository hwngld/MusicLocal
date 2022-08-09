package com.sildev.musiclocal.MediaPlayer;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.sildev.musiclocal.MusicConstants;
import com.sildev.musiclocal.datalocal.DataManager;
import com.sildev.musiclocal.model.Song;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class PlayManager {
    private MediaPlayer mediaPlayer;
    private final Context context;
    private List<Song> songList;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public PlayManager(Context context) {
        this.context = context;
        mediaPlayer = new MediaPlayer();
    }


    public void playSong(int position) {
        songList = DataManager.getListSong();
        this.position = position;
        Song song = songList.get(position);
        try {
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(song.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Toast.makeText(context, "The file isn't support", Toast.LENGTH_SHORT).show();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (DataManager.getRepeat()) {
                    playSong(position);
                    context.sendBroadcast(new Intent(MusicConstants.ACTION_PLAY));
                } else {
                    nextSong();
                    context.sendBroadcast(new Intent(MusicConstants.ACTION_NEXT));
                }
            }
        });

    }


    public int getCurrentTime() {
        if (mediaPlayer == null) {
            return 0;
        }
        return mediaPlayer.getCurrentPosition();
    }

    public void seekToMedia(int time) {
        mediaPlayer.seekTo(time);
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public void nextSong() {

        if (DataManager.getShuffle()) {
            position = randomSong(position);
        } else {
            position++;
            if (position >= songList.size()) {
                position = 0;
            }
        }
        Log.e("position", position + "");
        DataManager.setCurrentSong(songList.get(position));
        playSong(position);

    }

    public void previousSong() {
        if (DataManager.getShuffle()) {
            position = randomSong(position);
        } else {
            position--;
            if (position < 0) {
                position = songList.size() - 1;
            }
        }
        Log.e("position", position + "");
        DataManager.setCurrentSong(songList.get(position));
        playSong(position);


    }

    public int randomSong(int position) {
        int size = songList.size() - 1;
        int mPosition;
        do {
            mPosition = new Random().nextInt(size);
        } while (mPosition == position);
        return mPosition;
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    public List<Song> getSongList() {
        return songList;
    }

}
