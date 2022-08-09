package com.sildev.musiclocal.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.sildev.musiclocal.helper.MusicLibraryHelper;
import com.sildev.musiclocal.model.Song;

import java.util.ArrayList;
import java.util.List;

public class SongFragmentViewModel extends AndroidViewModel {
    public List<Song> songList;

    public SongFragmentViewModel(@NonNull Application application) {
        super(application);
        songList = new ArrayList<>();
        getData(application);
    }


    public void getData(Context context) {
        songList = MusicLibraryHelper.fetchSongFromStorage(context);
    }

    public List<Song> searchSong(String key) {
        return MusicLibraryHelper.searchSongByKey(key, songList);
    }

    public List<Song> getSongFromStorage() {
        return songList;
    }

    public List<Song> sortSong(List<Song> songList, int type) {
        return MusicLibraryHelper.sortListSong(songList, type);
    }
}
