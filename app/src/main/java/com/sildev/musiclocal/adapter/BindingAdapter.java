package com.sildev.musiclocal.adapter;

import android.net.Uri;
import android.widget.ImageView;

import com.sildev.musiclocal.R;
import com.squareup.picasso.Picasso;

public class BindingAdapter {
    @androidx.databinding.BindingAdapter("android:imgURL")
    public static void setImageURL(ImageView imageView, String url) {
        try {
            Picasso.get()
                    .load(Uri.parse(url))
                    .placeholder(R.drawable.ic_headphone)
                    .into(imageView);
        } catch (Exception e) {
            imageView.setImageResource(R.drawable.ic_headphone);
        }

    }
}
