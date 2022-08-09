package com.sildev.musiclocal.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sildev.musiclocal.R;
import com.sildev.musiclocal.databinding.ItemAlbumBinding;
import com.sildev.musiclocal.model.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder> {
    private List<Album> albumList;

    public interface IOnClickItem {
        void onClickItem(Album album);
    }

    private IOnClickItem iOnClickItem;

    public AlbumsAdapter(IOnClickItem iOnClickItem) {
        albumList = new ArrayList<>();
        this.iOnClickItem = iOnClickItem;
    }


    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAlbumBinding itemAlbumBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_album, parent, false);
        return new AlbumViewHolder(itemAlbumBinding);
    }

    public void setAlbumList(List<Album> albumList) {
        this.albumList = albumList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Album album = albumList.get(position);
        holder.setAlbumBinding(album);
        holder.itemView.setOnClickListener(v -> {
            iOnClickItem.onClickItem(album);
        });

    }

    @Override
    public int getItemCount() {
        if (albumList == null) {
            return 0;
        }
        return albumList.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {

        private ItemAlbumBinding itemAlbumBinding;

        public AlbumViewHolder(ItemAlbumBinding itemAlbumBinding) {
            super(itemAlbumBinding.getRoot());
            this.itemAlbumBinding = itemAlbumBinding;
        }

        public void setAlbumBinding(Album album) {
            itemAlbumBinding.setAlbum(album);
        }
    }


    public List<Album> getAlbumList() {
        return albumList;
    }
}
