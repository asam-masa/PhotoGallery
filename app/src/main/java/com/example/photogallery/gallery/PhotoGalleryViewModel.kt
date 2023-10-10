package com.example.photogallery.gallery

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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

// MediaStoreAPIでContextが必要なためAndroidViewModelを継承する
class PhotoGalleryViewModel(app:Application):AndroidViewModel(app){
    val photoList = MutableLiveData<List<PhotoGalleryItem>>()

    fun loadPhotoList(){
        viewModelScope.launch(Dispatchers.IO){
            val list = mutableListOf<PhotoGalleryItem>()

            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_ADDED
            )
            val selection = null
            val selectionArgs = null
            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

            getApplication<Application>().contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,selection, selectionArgs, sortOrder
            )?.use{cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                while (cursor.moveToNext()){
                    val id = cursor.getLong(idColumn)
                    val uri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,id
                    )
                    list.add(PhotoGalleryItem(uri))
                }
            }
            photoList.postValue(list)
        }
    }

    fun getPhotoItem(index: Int) = photoList.value?.getOrNull(index)
}