package com.sildev.musiclocal.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;

import com.sildev.musiclocal.MusicConstants;
import com.sildev.musiclocal.R;
import com.sildev.musiclocal.adapter.ArtistAdapter;
import com.sildev.musiclocal.databinding.FragmentArtistBinding;
import com.sildev.musiclocal.model.Artist;
import com.sildev.musiclocal.presenter.ArtistInterface;
import com.sildev.musiclocal.presenter.ArtistPresenter;

import java.util.List;


public class ArtistFragment extends Fragment implements ArtistInterface {

    private FragmentArtistBinding artistBinding;
    private ArtistAdapter artistAdapter;
    private ArtistPresenter artistPresenter;
    private SearchView searchView;

    public ArtistFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artistAdapter = new ArtistAdapter(getContext());
        artistPresenter = new ArtistPresenter(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        artistBinding = FragmentArtistBinding.inflate(inflater, container, false);

        return artistBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        artistBinding.recyclerViewArtist.setAdapter(artistAdapter);
        artistPresenter.loadData(getContext());
        artistBinding.toolbarSong.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_search:
                        searchView = (SearchView) item.getActionView();
                        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                artistPresenter.searchArtist(query.trim());
                                return true;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                artistPresenter.searchArtist(newText.trim());
                                return true;
                            }
                        });
                        return true;
                    case R.id.item_az:
                        artistPresenter.sortListArtist(artistAdapter.getArtistList(), MusicConstants.SORT_A_Z);
                        return true;
                    case R.id.item_za:
                        artistPresenter.sortListArtist(artistAdapter.getArtistList(), MusicConstants.SORT_Z_A);
                        return true;
                    case R.id.item_default:
                        artistPresenter.sortListArtist(artistAdapter.getArtistList(), MusicConstants.SORT_DEFAULT);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void setArtistList(List<Artist> artistList) {
        artistAdapter.setArtistList(artistList);
    }
}