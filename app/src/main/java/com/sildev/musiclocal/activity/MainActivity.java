package com.sildev.musiclocal.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sildev.musiclocal.MusicConstants;
import com.sildev.musiclocal.MyApplication;
import com.sildev.musiclocal.service.PlaySongService;
import com.sildev.musiclocal.R;
import com.sildev.musiclocal.dialog.TimerDialog;
import com.sildev.musiclocal.databinding.ActivityMainBinding;
import com.sildev.musiclocal.databinding.PlaySongLayoutBinding;
import com.sildev.musiclocal.fragment.AlbumFragment;
import com.sildev.musiclocal.fragment.ArtistFragment;
import com.sildev.musiclocal.fragment.FavouriteFragment;
import com.sildev.musiclocal.viewmodel.MainViewModel;
import com.sildev.musiclocal.fragment.SettingsFragment;
import com.sildev.musiclocal.fragment.SongFragment;
import com.sildev.musiclocal.helper.MusicLibraryHelper;
import com.sildev.musiclocal.model.Song;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    private BottomSheetDialog bottomSheetDialog;
    private PlaySongLayoutBinding playSongLayoutBinding;
    private MainViewModel mainViewModel;
    private Handler handlerUpdateTime;
    private TimerDialog timerDialog;
    private final BroadcastReceiver playPauseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            new Handler().postDelayed(() -> {
                switch (intent.getAction()) {

                    case MusicConstants.ACTION_PLAY:
                    case MusicConstants.ACTION_NEXT:
                    case MusicConstants.ACTION_PREVIOUS:
                        resetRotate(mainBinding.imageSong);
                        resetRotate(playSongLayoutBinding.imageSong);
                    case MusicConstants.ACTION_PAUSE:
                        updateSong();
                        updatePlay();
                        break;
                }
            }, 100);


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.getRoot().setFitsSystemWindows(true);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        timerDialog = new TimerDialog(this);
        getSupportFragmentManager().beginTransaction().add(R.id.layoutFragment, new SongFragment()).commit();
        mainBinding.bottomNavigationMain.setOnItemSelectedListener(item -> {
            Fragment fragment;
            if (mainBinding.bottomNavigationMain.getSelectedItemId() != item.getItemId()) {
                switch (item.getItemId()) {
                    case R.id.itemMusic:
                        fragment = new SongFragment();
                        break;
                    case R.id.itemSettings:
                        fragment = new SettingsFragment();
                        break;
                    case R.id.itemFavourite:
                        fragment = new FavouriteFragment();
                        break;
                    case R.id.itemAlbum:
                        fragment = new AlbumFragment();
                        break;
                    case R.id.itemArtist:
                        fragment = new ArtistFragment();
                        break;
                    default:
                        fragment = new SongFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.layoutFragment, fragment).commit();
            }


            return true;
        });
        mainBinding.imageNext.setOnClickListener(v -> nextSong());
        mainBinding.imagePrevious.setOnClickListener(v -> previousSong());
        mainBinding.imagePlay.setOnClickListener(v -> playOrPause());
        setUpBottomSheet();
        mainBinding.layoutPlayer.setOnClickListener(v -> showPlaySongLayout());
        handlerUpdateTime = new Handler();
        handlerUpdateTime.postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentTime = MyApplication.playManager.getCurrentTime();
                updateTime(currentTime);
                handlerUpdateTime.postDelayed(this, 1000);
            }
        }, 1);
        playSongLayoutBinding.seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTime(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MyApplication.playManager.seekToMedia(seekBar.getProgress());

            }
        });


    }

    private void showPlaySongLayout() {
        playSongLayoutBinding.setIsRepeat(mainViewModel.getRepeat());
        playSongLayoutBinding.setIsShuffle(mainViewModel.getShuffle());
        bottomSheetDialog.show();
    }

    public void updateTime(int currentTime) {
        playSongLayoutBinding.setCurrentTime(MusicLibraryHelper.formatDuration(currentTime));
        playSongLayoutBinding.seekBarTime.setProgress(currentTime);

    }

    public void updateSong() {
        Song song = mainViewModel.getCurrentSong();
        playSongLayoutBinding.seekBarTime.setMax(song.getDuration());
        mainBinding.setSong(song);
        playSongLayoutBinding.setSong(song);
        playSongLayoutBinding.setIsFavourite(false);
        checkSongIsFavourite(song.getId());
        mainBinding.setIsVisible(true);
        playSongLayoutBinding.tvTotalTime.setText(MusicLibraryHelper.formatDuration(song.getDuration()));
        updatePlay();

    }

    public void checkSongIsFavourite(long id) {
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(mainViewModel.getSong(id)
                .observeOn(Schedulers.computation())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(song -> {
                    playSongLayoutBinding.setIsFavourite(true);
                    disposable.dispose();
                }));

    }

    public void updatePlay() {
        boolean isPlaying = MyApplication.playManager.isPlaying();
        mainBinding.setIsPlaying(isPlaying);
        playSongLayoutBinding.setIsPlaying(isPlaying);
        if (isPlaying) {
            startRotate(mainBinding.imageSong);
            startRotate(playSongLayoutBinding.imageSong);
        } else {
            stopRotate(mainBinding.imageSong);
            stopRotate(playSongLayoutBinding.imageSong);
        }
    }


    private void setUpBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        playSongLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.play_song_layout, findViewById(R.id.layoutMain), false);
        bottomSheetDialog.setContentView(playSongLayoutBinding.getRoot());
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        BottomSheetBehavior behavior = bottomSheetDialog.getBehavior();
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setDraggable(false);

        playSongLayoutBinding.imagePlay.setOnClickListener(v -> {
            playOrPause();
        });
        playSongLayoutBinding.imageNext.setOnClickListener(v -> {
            nextSong();
        });
        playSongLayoutBinding.imagePrevious.setOnClickListener(v -> {
            previousSong();
        });
        playSongLayoutBinding.imageBack.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });
        playSongLayoutBinding.imageShuffle.setOnClickListener(v -> {
            boolean isShuffle = mainViewModel.getShuffle();
            mainViewModel.setShuffle(!isShuffle);
            playSongLayoutBinding.setIsShuffle(!isShuffle);
            if (mainBinding.bottomNavigationMain.getSelectedItemId() == R.id.itemSettings) {
                getSupportFragmentManager().beginTransaction().replace(R.id.layoutFragment, new SettingsFragment()).commit();
            }
        });
        playSongLayoutBinding.imageRepeat.setOnClickListener(v -> {
            boolean isRepeat = mainViewModel.getRepeat();
            playSongLayoutBinding.setIsRepeat(!isRepeat);
            mainViewModel.setRepeat(!isRepeat);
            if (mainBinding.bottomNavigationMain.getSelectedItemId() == R.id.itemSettings) {
                getSupportFragmentManager().beginTransaction().replace(R.id.layoutFragment, new SettingsFragment()).commit();
            }
        });
        playSongLayoutBinding.imageHeart.setOnClickListener(v -> {
            favouriteSong();
        });
        playSongLayoutBinding.imageClock.setOnClickListener(v -> {
            timerDialog.show();
        });
    }

    private void favouriteSong() {
        Song song = mainViewModel.getCurrentSong();
        Boolean isFavourite = playSongLayoutBinding.getIsFavourite();
        playSongLayoutBinding.setIsFavourite(!isFavourite);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        if (isFavourite) {
            compositeDisposable.add(mainViewModel.deleteFromFavourite(song)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(compositeDisposable::dispose));
        } else {
            compositeDisposable.add(mainViewModel.addToFavourite(song)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(compositeDisposable::dispose));
        }
        if (mainBinding.bottomNavigationMain.getSelectedItemId() == R.id.itemFavourite) {
            getSupportFragmentManager().beginTransaction().replace(R.id.layoutFragment, new FavouriteFragment()).commit();
        }
    }


    public void playOrPause() {
        Intent intent = new Intent(MusicConstants.ACTION_PAUSE);
        sendBroadcast(intent);
    }

    public void startRotate(ImageView imageView) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                imageView.animate()
                        .rotationBy(360)
                        .withEndAction(this)
                        .setDuration(40000)
                        .setInterpolator(new LinearInterpolator())
                        .start();
            }
        };
        imageView.animate()
                .rotationBy(360)
                .withEndAction(runnable)
                .setDuration(40000)
                .setInterpolator(new LinearInterpolator())
                .start();
    }

    public void resetRotate(ImageView imageView) {
        imageView.setRotation(0);
    }


    public void stopRotate(ImageView imageView) {
        imageView.animate().cancel();
    }


    public void nextSong() {
        Intent intent = new Intent(MusicConstants.ACTION_NEXT);
        sendBroadcast(intent);
    }

    public void previousSong() {
        Intent intent = new Intent();
        intent.setAction(MusicConstants.ACTION_PREVIOUS);
        sendBroadcast(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Song song = mainViewModel.getCurrentSong();
        if (song == null) {
            mainBinding.setIsVisible(false);
            return;
        }
        updateSong();
        updatePlay();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicConstants.ACTION_PREVIOUS);
        intentFilter.addAction(MusicConstants.ACTION_PLAY);
        intentFilter.addAction(MusicConstants.ACTION_PAUSE);
        intentFilter.addAction(MusicConstants.ACTION_NEXT);
        registerReceiver(playPauseReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(playPauseReceiver);
        if (!MyApplication.playManager.isPlaying()) {
            Intent intent = new Intent(this, PlaySongService.class);
            stopService(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}