package com.example.photogallery.gallery

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.PermissionChecker
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.photogallery.R
import com.example.photogallery.databinding.FragmentPhotoGalleryBinding
import com.example.photogallery.databinding.ViewPhotoGalleryImageBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalStateException


@AndroidEntryPoint
class PhotoGalleryFragment : Fragment() {
    // Avtivityと同じインスタンスを共有する場合はactivityViewModels()を使う
    // https://developer.android.com/kotlin/ktx?hl=ja#fragment
    private val viewModel: PhotoGalleryViewModel by activityViewModels()

    private var _binding:FragmentPhotoGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_photo_gallery,container,false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageAdapter = ImageAdapter()

        viewModel.photoList.observe(viewLifecycleOwner, Observer {
            imageAdapter.submitList(it)
        })
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@PhotoGalleryFragment.context, SPAN_COUNT)
            adapter = imageAdapter
        }
    }

    inner class ImageAdapter:ListAdapter<PhotoGalleryItem,ImageViewHolder>(PhotoGalleryItem.DIFF_UTIL) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<ViewPhotoGalleryImageBinding>(
                layoutInflater, R.layout.view_photo_gallery_image, parent, false
            )
            return ImageViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            val item = viewModel.getPhotoItem(position)


            holder.binding.viewModel = viewModel
            holder.binding.item = item
            // スクロール時のView再利用時に表示がおかしくなることがあるためViewにすぐ反映する
            holder.binding.executePendingBindings()

            item?.let {
                Picasso.get().load(it.uri)
                    .fit()
                    .centerCrop()
                    .into(holder.binding.imageView)

            }
        }
    }

    inner class ImageViewHolder(val binding: ViewPhotoGalleryImageBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onResume() {
        super.onResume()
        if (viewModel.isPermissionGranted.value == true){
            val args: PhotoGalleryFragmentArgs by navArgs()
            Log.v("onResume_args.folderName","args.folderName")
            viewModel.loadPhotoList(args.bucketId)
        }
    }

    companion object{
        fun newInstance() = PhotoGalleryFragment()

        private const val REQ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
        private const val SPAN_COUNT = 3
    }
}