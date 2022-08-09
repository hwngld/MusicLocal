package com.sildev.musiclocal.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.android.schedulers.AndroidSchedulers;

import com.sildev.musiclocal.R;
import com.sildev.musiclocal.adapter.SongsAdapter;
import com.sildev.musiclocal.databinding.FragmentFavouriteBinding;
import com.sildev.musiclocal.viewmodel.FavouriteSongViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FavouriteFragment extends Fragment {

    private FragmentFavouriteBinding favouriteBinding;
    private SongsAdapter songsAdapter;
    private FavouriteSongViewModel favouriteSongViewModel;

    public FavouriteFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songsAdapter = new SongsAdapter(getContext());
        favouriteSongViewModel = new ViewModelProvider(this).get(FavouriteSongViewModel.class);
        loadFavouriteList();
    }

    private void loadFavouriteList() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(favouriteSongViewModel.getFavouriteSongList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(favouriteList -> {
                    songsAdapter.setDataSong(favouriteList);
                    compositeDisposable.dispose();
                })
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        favouriteBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite, container, false);
        return favouriteBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        favouriteBinding.listFavouriteSong.setAdapter(songsAdapter);


    }
}