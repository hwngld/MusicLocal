package com.sildev.musiclocal.helper;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.sildev.musiclocal.MusicConstants;
import com.sildev.musiclocal.model.Album;
import com.sildev.musiclocal.model.Song;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class MusicLibraryHelper {

    public static List<Song> fetchSongFromStorage(Context context) {
        List<Song> songList = new ArrayList<>();
        String[] proj = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID
        };
        Cursor audioCursor = context.getContentResolver()
                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        , proj
                        , null
                        , null
                        , null);
        if (audioCursor != null) {
            if (audioCursor.moveToFirst()) {
                do {
                    int audioId = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
                    int audioTitle = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
                    int audioartist = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
                    int audioduration = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
                    int audiodata = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                    int albumid = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);
                    int album = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
                    int duration = 0;
                    if (audioCursor.getString(audioduration) != null) {
                        duration = Integer.parseInt(audioCursor.getString(audioduration));
                        if (duration > 15000) {
                            Song song = new Song();
                            song.setId(audioCursor.getLong(audioId));
                            song.setName(audioCursor.getString(audioTitle));
                            song.setSinger(audioCursor.getString(audioartist));
                            song.setPath(audioCursor.getString(audiodata));
                            song.setDuration(duration);
                            song.setAlbumId(audioCursor.getLong(albumid));
                            song.setAlbum(audioCursor.getString(album));
                            song.setAlbumArt(ContentUris.withAppendedId(Uri.parse(MusicConstants.ALBUM_ART_DIR), song.getAlbumId()));
                            songList.add(song);
                        }
                    }

                } while (audioCursor.moveToNext());
            }
        }
        return songList;

    }

    public static String formatDuration(long duration) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        String s = simpleDateFormat.format(new Date(duration));
        return s;
    }


    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").toLowerCase();
    }

    public static List<Song> searchSongByKey(String key, List<Song> songList) {
        List<Song> listResult = new ArrayList<>();
        for (Song song : songList) {
            if (removeAccent(song.getName()).contains(key) || removeAccent(song.getSinger()).contains(key)) {
                listResult.add(song);
            }
        }
        return listResult;
    }

    public static List<Album> searchAlbumByKey(String key, List<Album> albumList) {
        List<Album> listResult = new ArrayList<>();
        for (Album album : albumList) {
            if (removeAccent(album.getTitle()).contains(key)) {
                listResult.add(album);
            }
        }
        return listResult;
    }

    public static List<Song> sortListSong(List<Song> songList, int type) {
        Collections.sort(songList, new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {
                switch (type) {
                    case MusicConstants.SORT_A_Z:
                        return o1.getName().compareTo(o2.getName());
                    case MusicConstants.SORT_Z_A:
                        return o2.getName().compareTo(o1.getName());
                    default:
                        return Long.compare(o1.getId(), o2.getId());
                }
            }
        });
        return songList;
    }

    public static List<Album> sortListAlbum(List<Album> albumList, int type) {
        Collections.sort(albumList, new Comparator<Album>() {
            @Override
            public int compare(Album o1, Album o2) {
                switch (type) {
                    case MusicConstants.SORT_A_Z:
                        return o1.getTitle().compareTo(o2.getTitle());
                    case MusicConstants.SORT_Z_A:
                        return o2.getTitle().compareTo(o1.getTitle());
                    default:
                        return Long.compare(o1.getId(), o2.getId());
                }
            }
        });
        return albumList;
    }

}
