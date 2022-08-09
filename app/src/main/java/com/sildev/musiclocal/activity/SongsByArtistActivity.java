package com.sildev.musiclocal.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.sildev.musiclocal.R;
import com.sildev.musiclocal.adapter.SongsAdapter;
import com.sildev.musiclocal.databinding.ActivitySongsByArtistBinding;
import com.sildev.musiclocal.model.Artist;

import org.json.JSONException;
import org.json.JSONObject;

public class SongsByArtistActivity extends AppCompatActivity {
    private ActivitySongsByArtistBinding songsByArtistBinding;
    private SongsAdapter songsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songsByArtistBinding = DataBindingUtil.setContentView(this, R.layout.activity_songs_by_artist);
        Artist artist = new Artist();
        String strJsonObject = getIntent().getStringExtra("artist");
        JSONObject object = null;
        try {
            object = new JSONObject(strJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        artist = new Gson().fromJson(object.toString(), Artist.class);
        songsAdapter = new SongsAdapter(this);
        songsByArtistBinding.recyclerViewSong.setAdapter(songsAdapter);
        songsByArtistBinding.setArtist(artist);
        songsAdapter.setDataSong(artist.getSongList());
        songsByArtistBinding.toolbarSong.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}