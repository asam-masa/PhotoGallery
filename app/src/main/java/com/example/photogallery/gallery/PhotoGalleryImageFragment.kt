package com.example.photogallery.gallery

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
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
    ): View {
        _binding = FragmentPhotoGalleryImageBinding.inflate(
            inflater,container,false
        )

        activity?.addMenuProvider(object :MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                val inflater: MenuInflater = menuInflater
                inflater.inflate(R.menu.menu_photogallery, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId){
                    R.id.rotate_item -> {
                        Log.v("rotate_item", "rotate_item")
                        binding.imageViewPhotoGalleryImage.rotation += 90f
                        binding.imageViewPhotoGalleryImage.scaleType = ImageView.ScaleType.FIT_CENTER
                        binding.imageViewPhotoGalleryImage.layoutParams =
                            RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.MATCH_PARENT
                            )
                        true
                    }
                    else -> true
                }
            }
        },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: PhotoGalleryImageFragmentArgs by navArgs()
        binding.imageViewPhotoGalleryImage.setImageURI(Uri.parse(args.photoURI))
    }



}