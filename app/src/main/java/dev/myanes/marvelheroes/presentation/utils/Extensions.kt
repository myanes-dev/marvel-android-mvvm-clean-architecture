package dev.myanes.marvelheroes.presentation.utils

import android.widget.ImageView
import com.squareup.picasso.Picasso


fun ImageView.loadURL(url: String) {
    Picasso.get()
        .load(url)
        .centerCrop()
        .fit()
        .into(this);
}