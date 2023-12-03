package com.example.photogallery.gallery

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.photogallery.R
import com.example.photogallery.databinding.FragmentPhotoGalleryBinding
import com.example.photogallery.databinding.FragmentPhotoGalleryImageBinding
import dagger.hilt.android.AndroidEntryPoint


class PhotoGalleryImageFragment : Fragment() {
    private var _binding: FragmentPhotoGalleryImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotoGalleryImageBinding.inflate(
            inflater,container,false
        )
        return binding.root
    //        return inflater.inflate(R.layout.fragment_photo_gallery_image, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: PhotoGalleryImageFragmentArgs by navArgs()
        Log.v("imageViewPhotoGalleryImage","imageViewPhotoGalleryImage")
        binding.imageViewPhotoGalleryImage.setImageURI(Uri.parse(args.photoURI))
    }

}