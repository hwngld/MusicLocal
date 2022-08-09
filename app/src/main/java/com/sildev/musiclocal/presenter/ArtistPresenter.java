package com.sildev.musiclocal.presenter;

import android.content.Context;

import com.sildev.musiclocal.MusicConstants;
import com.sildev.musiclocal.helper.MusicLibraryHelper;
import com.sildev.musiclocal.model.Artist;
import com.sildev.musiclocal.model.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ArtistPresenter {
    private ArtistInterface artistInterface;
    private List<Artist> artistList;

    public ArtistPresenter(ArtistInterface artistInterface) {
        this.artistInterface = artistInterface;
    }

    public void loadData(Context context) {
        List<Song> songList = MusicLibraryHelper.fetchSongFromStorage(context);
        artistList = new ArrayList<>();
        for (Song song : songList) {
            int position = getPositionArtist(song.getSinger());
            if (position == -1) {
                Artist artist = new Artist();
                artist.setName(song.getSinger());
                artist.getSongList().add(song);
                artistList.add(artist);
            } else {
                artistList.get(position).getSongList().add(song);
            }
        }
        artistInterface.setArtistList(artistList);
    }

    public void searchArtist(String key) {
        List<Artist> listResult = new ArrayList<>();
        for (Artist artist : artistList) {
            if (MusicLibraryHelper.removeAccent(artist.getName()).contains(key)) {
                listResult.add(artist);
            }
        }
        artistInterface.setArtistList(listResult);
    }

    public int getPositionArtist(String artist) {
        for (Artist art : artistList) {
            if (art.getName().equalsIgnoreCase(artist)) {
                return artistList.indexOf(art);
            }
        }
        return -1;
    }

    public void sortListArtist(List<Artist> artistList, int type) {
        Collections.sort(artistList, new Comparator<Artist>() {
            @Override
            public int compare(Artist o1, Artist o2) {
                switch (type) {
                    case MusicConstants.SORT_A_Z:
                        return o1.getName().compareTo(o2.getName());
                    case MusicConstants.SORT_Z_A:
                        return o2.getName().compareTo(o1.getName());
                    default:
                        return Long.compare(o1.getSongList().get(0).getId(), o2.getSongList().get(0).getId());
                }
            }
        });
        artistInterface.setArtistList(artistList);
    }
}
