package com.example.photogallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.launch
import androidx.databinding.DataBindingUtil
import com.example.photogallery.databinding.ActivityMainBinding
import com.example.photogallery.gallery.PhotoGalleryActivity

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding

    private val launcher = registerForActivityResult(PhotoGalleryActivity.ResultContract()){
        binding.imageView.setImageURI(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener{
            launcher.launch()
        }
    }
}