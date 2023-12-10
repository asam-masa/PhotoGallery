package com.example.photogallery.gallery

import android.util.Log
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class PhotoGalleryViewModelTest{
    private lateinit var viewModel: PhotoGalleryViewModel

    @Before
    fun setUp(){
//        viewModel = PhotoGalleryViewModel()
    }

    @Test
    fun addition_isCorrect() {
        viewModel.loadPhotoFolderList()
        val result = viewModel.getPhotoFolderItem(0)
        Log.v("result",result.toString())
        Assert.assertEquals(4, 2 + 2)
    }
}