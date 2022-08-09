package com.sildev.musiclocal.datalocal.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sildev.musiclocal.model.Song;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface FavouriteSongDAO {
    @Query("SELECT * FROM favouriteSongTable")
    Flowable<List<Song>> getFavouriteSongList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addFavourite(Song song);

    @Delete
    Completable removeFromFavourite(Song song);

    @Query("SELECT * FROM favouriteSongTable WHERE id = :songId")
    Flowable<Song> getFavouriteSong(long songId);
}
