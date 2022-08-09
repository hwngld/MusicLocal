package com.sildev.musiclocal.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sildev.musiclocal.R;
import com.sildev.musiclocal.activity.SongsByArtistActivity;
import com.sildev.musiclocal.databinding.ItemArtistBinding;
import com.sildev.musiclocal.model.Artist;

import java.util.ArrayList;
import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {
    private List<Artist> artistList;
    private Context context;

    public ArtistAdapter(Context context) {
        artistList = new ArrayList<>();
        this.context = context;
    }

    public List<Artist> getArtistList() {
        return artistList;
    }

    public void setArtistList(List<Artist> artistList) {
        this.artistList = artistList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemArtistBinding itemArtistBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_artist, parent, false);

        return new ArtistViewHolder(itemArtistBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Artist artist = artistList.get(position);
        holder.setArtist(artist);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SongsByArtistActivity.class);
            JsonObject jsonObject = new Gson().toJsonTree(artist).getAsJsonObject();
            String strJsonArr = jsonObject.toString();
            intent.putExtra("artist", strJsonArr);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder {
        private final ItemArtistBinding artistBinding;

        public ArtistViewHolder(ItemArtistBinding itemArtistBinding) {
            super(itemArtistBinding.getRoot());
            this.artistBinding = itemArtistBinding;
        }

        public void setArtist(Artist artist) {
            artistBinding.setArtist(artist);
        }
    }
}
