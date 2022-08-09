package com.sildev.musiclocal.viewmodel;

import androidx.lifecycle.ViewModel;

import com.sildev.musiclocal.datalocal.DataManager;

public class SettingsFragmentViewModel extends ViewModel {
    public SettingsFragmentViewModel() {

    }

    public void setRepeat(boolean isRepeat) {
        DataManager.setRepeat(isRepeat);
    }

    public boolean getRepeat() {
        return DataManager.getRepeat();
    }

    public void setShuffle(boolean isShuffle) {
        DataManager.setShuffle(isShuffle);
    }

    public boolean getShuffle() {
        return DataManager.getShuffle();
    }

}
