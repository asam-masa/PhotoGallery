package com.example.photogallery.gallery

import android.net.Uri
import androidx.recyclerview.widget.DiffUtil

class PhotoGalleryViewModel {
}

data class PhotoGalleryItem(val uri: Uri){
    companion object{
        val DIFF_UTIL = object : DiffUtil.ItemCallback<PhotoGalleryItem>() {
            override fun areItemsTheSame(
                oldItem: PhotoGalleryItem,
                newItem: PhotoGalleryItem
            ): Boolean {
                return oldItem.uri == newItem.uri
            }

            override fun areContentsTheSame(
                oldItem: PhotoGalleryItem,
                newItem: PhotoGalleryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}