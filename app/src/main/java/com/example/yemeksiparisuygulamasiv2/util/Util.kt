package com.example.yemeksiparisuygulamasiv2.util

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideContext
import com.bumptech.glide.request.RequestOptions
import com.example.yemeksiparisuygulamasiv2.R

fun ImageView.downloadFromUrl(url: String?, progressDrawable: CircularProgressDrawable){

    val options = RequestOptions()
            .placeholder(progressDrawable)
            .error(R.mipmap.ic_launcher_round)

    Glide.with(context)
            .setDefaultRequestOptions(options)
            .load(url)
            .into(this)


}

fun placeholderProgressBar(context: Context): CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}