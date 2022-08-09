package com.sildev.musiclocal.datalocal.database;


import android.content.Context;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sildev.musiclocal.datalocal.dao.FavouriteSongDAO;
import com.sildev.musiclocal.model.Song;

@Database(entities = Song.class, version = 1, exportSchema = false)
public abstract class FavouriteSongDatabase extends RoomDatabase {
    private static FavouriteSongDatabase favouriteSongDatabase;

    public static synchronized FavouriteSongDatabase getFavouriteSongDatabase(Context context) {
        if (favouriteSongDatabase == null) {
            favouriteSongDatabase = Room.databaseBuilder(
                            context,
                            FavouriteSongDatabase.class,
                            "favourite_song_db")
                    .build();
        }
        return favouriteSongDatabase;

    }


    public abstract FavouriteSongDAO favouriteSongDAO();

}
