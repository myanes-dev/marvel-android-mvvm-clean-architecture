package dev.myanes.marvelheroes.presentation.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.squareup.picasso.Picasso


fun ImageView.loadURL(url: String) {
    if (url.isEmpty()) return
    Picasso.get()
        .load(url)
        .centerCrop()
        .fit()
        .into(this);
}

fun Context.toggleKeyBoard() {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS)
}
