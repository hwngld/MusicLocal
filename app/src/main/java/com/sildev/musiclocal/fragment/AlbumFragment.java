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
import com.sildev.musiclocal.adapter.AlbumsAdapter;
import com.sildev.musiclocal.adapter.SongsAdapter;
import com.sildev.musiclocal.databinding.FragmentAlbumBinding;
import com.sildev.musiclocal.model.Album;
import com.sildev.musiclocal.viewmodel.AlbumFragmentViewModel;

import java.util.ArrayList;
import java.util.List;


public class AlbumFragment extends Fragment implements AlbumsAdapter.IOnClickItem {
    private FragmentAlbumBinding albumBinding;
    private AlbumFragmentViewModel albumFragmentViewModel;
    private AlbumsAdapter albumsAdapter;
    private SongsAdapter songsAdapter;
    private SearchView searchView;
    private List<Album> albumList;

    public AlbumFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albumFragmentViewModel = new ViewModelProvider(this).get(AlbumFragmentViewModel.class);
        albumsAdapter = new AlbumsAdapter(this);
        songsAdapter = new SongsAdapter(getContext());
        albumList = new ArrayList<>();
        albumList = albumFragmentViewModel.getAlbumList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        albumBinding = FragmentAlbumBinding.inflate(inflater, container, false);
        return albumBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        albumBinding.recyclerViewAlbum.setAdapter(albumsAdapter);
        albumsAdapter.setAlbumList(albumList);
        albumBinding.recyclerViewSong.setAdapter(songsAdapter);

        albumBinding.toolbarSong.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_search:
                        searchView = (SearchView) item.getActionView();
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                searchAlbum(query.trim());
                                return true;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                searchAlbum(newText.trim());
                                return true;
                            }
                        });
                        return true;
                    case R.id.item_az:
                        albumsAdapter.setAlbumList(albumFragmentViewModel.sortAlbum(albumsAdapter.getAlbumList(), MusicConstants.SORT_A_Z));
                        return true;
                    case R.id.item_za:
                        albumsAdapter.setAlbumList(albumFragmentViewModel.sortAlbum(albumsAdapter.getAlbumList(), MusicConstants.SORT_Z_A));
                        return true;
                    case R.id.item_default:
                        albumsAdapter.setAlbumList(albumFragmentViewModel.sortAlbum(albumsAdapter.getAlbumList(), MusicConstants.SORT_DEFAULT));
                        return true;
                }
                return false;
            }
        });
    }

    private void searchAlbum(String query) {
        List<Album> listResult = albumFragmentViewModel.searchAlbum(query);
        albumsAdapter.setAlbumList(listResult);
    }

    @Override
    public void onClickItem(Album album) {
        albumBinding.setAlbum(album);
        albumBinding.setVisible(true);
        songsAdapter.setDataSong(album.getSongList());
    }
}