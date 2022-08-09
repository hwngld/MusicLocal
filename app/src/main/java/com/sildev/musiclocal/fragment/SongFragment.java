package com.sildev.musiclocal.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sildev.musiclocal.MusicConstants;
import com.sildev.musiclocal.R;
import com.sildev.musiclocal.adapter.SongsAdapter;
import com.sildev.musiclocal.databinding.FragmentSongBinding;
import com.sildev.musiclocal.model.Song;
import com.sildev.musiclocal.viewmodel.SongFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

public class SongFragment extends Fragment {
    private FragmentSongBinding fragmentSongBinding;
    private SongFragmentViewModel songFragmentViewModel;
    private SongsAdapter songsAdapter;
    private List<Song> songList;
    private SearchView searchView;

    public SongFragment() {
        fragmentSongBinding = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songFragmentViewModel = new ViewModelProvider(this).get(SongFragmentViewModel.class);
        songsAdapter = new SongsAdapter(getContext());
        songList = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        songList = songFragmentViewModel.getSongFromStorage();
        fragmentSongBinding.recyclerViewSong.setAdapter(songsAdapter);
        songsAdapter.setDataSong(songList);

        fragmentSongBinding.setIsEmpty(songList.size() < 1);

        fragmentSongBinding.toolbarSong.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_search:
                        searchView = (SearchView) item.getActionView();
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                searchSong(query.trim());
                                return true;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                searchSong(newText.trim());
                                return true;
                            }
                        });
                        return true;
                    case R.id.item_az:
                        songsAdapter.setDataSong(songFragmentViewModel.sortSong(songsAdapter.getSongs(), MusicConstants.SORT_A_Z));
                        return true;
                    case R.id.item_za:
                        songsAdapter.setDataSong(songFragmentViewModel.sortSong(songsAdapter.getSongs(), MusicConstants.SORT_Z_A));
                        return true;
                    case R.id.item_default:
                        songsAdapter.setDataSong(songFragmentViewModel.sortSong(songsAdapter.getSongs(), MusicConstants.SORT_DEFAULT));
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentSongBinding = FragmentSongBinding.inflate(inflater, container, false);
        return fragmentSongBinding.getRoot();
    }

    public void searchSong(String key) {
        List<Song> listResult = songFragmentViewModel.searchSong(key);
        songsAdapter.setDataSong(listResult);
        if (listResult.size() < 1) {
            fragmentSongBinding.setIsEmpty(true);
        } else {
            fragmentSongBinding.setIsEmpty(false);
        }
    }


}