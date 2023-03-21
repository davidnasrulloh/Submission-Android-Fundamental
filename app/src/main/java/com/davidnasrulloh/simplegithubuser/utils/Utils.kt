package com.davidnasrulloh.simplegithubuser.utils

import android.content.Context
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.davidnasrulloh.simplegithubuser.BuildConfig
import com.davidnasrulloh.simplegithubuser.R
import de.hdodenhof.circleimageview.CircleImageView

class Utils {
    companion object {
        const val TOKEN = BuildConfig.API_KEY

        fun CircleImageView.setImageGlide(context: Context, url: String) {
            Glide
                .with(context)
                .load(url)
                .placeholder(R.drawable.profile_placeholder)
                .into(this)
        }


        fun TextView.setAndVisible(text: String?) {
            if (!text.isNullOrBlank()) {
                this.text = text
                this.visibility = View.VISIBLE
            }
        }
    }
}