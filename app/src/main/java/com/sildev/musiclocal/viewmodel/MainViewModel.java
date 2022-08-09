package com.sildev.musiclocal.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.sildev.musiclocal.datalocal.DataManager;
import com.sildev.musiclocal.datalocal.database.FavouriteSongDatabase;
import com.sildev.musiclocal.model.Song;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class MainViewModel extends AndroidViewModel {
    private FavouriteSongDatabase favouriteSongDatabase;

    public MainViewModel(@NonNull Application application) {
        super(application);
        favouriteSongDatabase = FavouriteSongDatabase.getFavouriteSongDatabase(application);
    }

    public Completable addToFavourite(Song song) {

        return favouriteSongDatabase.favouriteSongDAO().addFavourite(song);
    }

    public Completable deleteFromFavourite(Song song) {
        return favouriteSongDatabase.favouriteSongDAO().removeFromFavourite(song);
    }

    public Flowable<Song> getSong(long id) {
        return favouriteSongDatabase.favouriteSongDAO().getFavouriteSong(id);
    }

    public void setCurrentSong(Song song) {
        DataManager.setCurrentSong(song);
    }

    public void setShuffle(boolean isShuffle) {
        DataManager.setShuffle(isShuffle);
    }

    public boolean getShuffle() {
        return DataManager.getShuffle();
    }

    public void setRepeat(boolean isRepeat) {
        DataManager.setRepeat(isRepeat);
    }

    public boolean getRepeat() {
        return DataManager.getRepeat();
    }

    public Song getCurrentSong() {
        return DataManager.getCurrentSong();
    }
}
