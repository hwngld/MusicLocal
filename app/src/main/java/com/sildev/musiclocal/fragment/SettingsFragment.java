package com.sildev.musiclocal.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sildev.musiclocal.dialog.TimerDialog;
import com.sildev.musiclocal.databinding.FragmentSettingsBinding;
import com.sildev.musiclocal.datalocal.DataManager;
import com.sildev.musiclocal.viewmodel.SettingsFragmentViewModel;


public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding fragmentSettingsBinding;
    private SettingsFragmentViewModel settingsFragmentViewModel;

    public SettingsFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsFragmentViewModel = new ViewModelProvider(this).get(SettingsFragmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater, container, false);
        fragmentSettingsBinding.setIsRepeat(settingsFragmentViewModel.getRepeat());
        fragmentSettingsBinding.setIsShuffle(settingsFragmentViewModel.getShuffle());
        return fragmentSettingsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentSettingsBinding.layoutRepeat.setOnClickListener(v -> setRepeat());
        fragmentSettingsBinding.switchRepeat.setOnClickListener(v -> setRepeat());

        fragmentSettingsBinding.layoutShuffle.setOnClickListener(v -> setShuffle());
        fragmentSettingsBinding.switchShuffle.setOnClickListener(v -> setShuffle());
        fragmentSettingsBinding.layoutTimer.setOnClickListener(v -> {
            new TimerDialog(getContext()).show();
        });
    }

    public void setRepeat() {
        boolean isRepeat = settingsFragmentViewModel.getRepeat();
        fragmentSettingsBinding.setIsRepeat(!isRepeat);
        settingsFragmentViewModel.setRepeat(!isRepeat);
        Log.e("1233", DataManager.getRepeat() + " r,s " + DataManager.getShuffle());
    }

    public void setShuffle() {
        boolean isShuffle = settingsFragmentViewModel.getShuffle();
        fragmentSettingsBinding.setIsShuffle(!isShuffle);
        settingsFragmentViewModel.setShuffle(!isShuffle);
        Log.e("1233", DataManager.getRepeat() + " r,s " + DataManager.getShuffle());
    }
}