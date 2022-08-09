package com.sildev.musiclocal.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.sildev.musiclocal.helper.MusicLibraryHelper;
import com.sildev.musiclocal.model.Album;
import com.sildev.musiclocal.model.Song;

import java.util.ArrayList;
import java.util.List;

public class AlbumFragmentViewModel extends AndroidViewModel {
    private List<Album> albumList;

    public AlbumFragmentViewModel(@NonNull Application application) {
        super(application);
        albumList = new ArrayList<>();
        getData(application);
    }

    private void getData(Context context) {
        List<Song> songList = MusicLibraryHelper.fetchSongFromStorage(context);
        for (Song song : songList) {
            int position = getPositionAlbum(song);
            if (position == -1) {
                Album album = new Album();
                album.setId(song.getId());
                album.setTitle(song.getAlbum());
                album.getSongList().add(song);
                album.setAlbumArt(song.getAlbumArt());
                albumList.add(album);
            } else {
                Album album = albumList.get(position);
                album.getSongList().add(song);
            }
        }
    }

    public List<Album> getAlbumList() {
        return albumList;
    }

    private int getPositionAlbum(Song song) {
        for (Album album : albumList) {
            if (song.getAlbumId() == album.getId() || song.getAlbum().equalsIgnoreCase(album.getTitle())) {
                return albumList.indexOf(album);
            }
        }
        return -1;
    }

    public List<Album> searchAlbum(String query) {
        return MusicLibraryHelper.searchAlbumByKey(query, albumList);
    }

    public List<Album> sortAlbum(List<Album> albumList, int type) {
        return MusicLibraryHelper.sortListAlbum(albumList, type);
    }
}
