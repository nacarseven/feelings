package com.nacarseven.feelings.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadImage(imageUrl: String) {

    Glide.with(this.context)
        .load(imageUrl)
        .apply(RequestOptions.circleCropTransform())
        .into(this)
}
