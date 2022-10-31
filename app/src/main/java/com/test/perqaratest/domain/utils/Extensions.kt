package com.test.perqaratest.domain.utils

import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

fun ImageView.loadImage(url: String?, cacheStrategy: DiskCacheStrategy = DiskCacheStrategy.NONE) {
    Glide.with(this)
        .load(url)
        .override(480, 320)
        .thumbnail(0.1f)
        .transition(DrawableTransitionOptions.withCrossFade())
        .timeout(20000)
        .diskCacheStrategy(cacheStrategy)
        .into(this)
}

fun RecyclerView.listenForPagination(onPaginate: () -> Unit) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (isLastVisible(this@listenForPagination, true)) {
                onPaginate()
            }
        }
    })
}

fun isLastVisible(rv: RecyclerView, shouldFindLastCompletelyVisible: Boolean = true): Boolean {
    val layoutManager = rv.layoutManager as LinearLayoutManager
    val pos = if (shouldFindLastCompletelyVisible) {
        layoutManager.findLastCompletelyVisibleItemPosition()
    } else {
        layoutManager.findFirstVisibleItemPosition()
    }
    val numItems: Int = rv.adapter?.itemCount ?: 0
    return pos >= numItems - 1
}