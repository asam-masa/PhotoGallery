package com.example.photogallery.gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.photogallery.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoGalleryActivity : AppCompatActivity() {
    private val viewModel: PhotoGalleryViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_gallery)
    }
}