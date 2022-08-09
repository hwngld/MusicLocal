package com.sildev.musiclocal.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sildev.musiclocal.MusicConstants;
import com.sildev.musiclocal.MyApplication;
import com.sildev.musiclocal.service.PlaySongService;
import com.sildev.musiclocal.R;
import com.sildev.musiclocal.databinding.ItemSongBinding;
import com.sildev.musiclocal.datalocal.DataManager;
import com.sildev.musiclocal.model.Song;

import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongViewHolder> {
    private List<Song> songs;
    private Context context;


    public SongsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSongBinding itemSongBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_song, parent, false);
        return new SongViewHolder(itemSongBinding);
    }

    public void setDataSong(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.setItemSongBinding(song);
        holder.itemSongBinding.getRoot().setOnClickListener(v -> {
            DataManager.setCurrentSong(song);
            MyApplication.playManager.setSongList(songs);
            MyApplication.playManager.setPosition(position);
            DataManager.setListSong(songs);
            Intent intent = new Intent();
            intent.setAction(MusicConstants.ACTION_PLAY);
            context.sendBroadcast(intent);
            Intent intentService = new Intent(context, PlaySongService.class);
            context.startService(intentService);
        });
    }

    @Override
    public int getItemCount() {
        if (songs != null) {
            return songs.size();
        }
        return 0;
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {

        ItemSongBinding itemSongBinding;

        public SongViewHolder(ItemSongBinding itemSongBinding) {
            super(itemSongBinding.getRoot());
            this.itemSongBinding = itemSongBinding;
        }

        public void setItemSongBinding(Song song) {
            itemSongBinding.setSong(song);
        }
    }

    public List<Song> getSongs() {
        return songs;
    }
}
