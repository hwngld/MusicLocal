package com.sildev.musiclocal.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.sildev.musiclocal.datalocal.database.FavouriteSongDatabase;
import com.sildev.musiclocal.model.Song;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class FavouriteSongViewModel extends AndroidViewModel {

    private FavouriteSongDatabase favouriteSongDatabase;

    public FavouriteSongViewModel(@NonNull Application application) {
        super(application);
        favouriteSongDatabase = FavouriteSongDatabase.getFavouriteSongDatabase(application);
    }

    public Flowable<List<Song>> getFavouriteSongList() {

        return favouriteSongDatabase.favouriteSongDAO().getFavouriteSongList();
    }

    public Completable deleteFavouriteSong(Song song) {

        return favouriteSongDatabase.favouriteSongDAO().removeFromFavourite(song);

    }
}
