package com.example.photogallery.gallery

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.example.photogallery.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PhotoGalleryFragment : Fragment() {
    // Avtivityと同じインスタンスを共有する場合はactivityViewModels()を使う
    // https://developer.android.com/kotlin/ktx?hl=ja#fragment
    private val viewModel: PhotoGalleryViewModel by activityViewModels()

    private val permissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){granted ->
        if (granted){
            // 承認された
            viewModel.isPermissionGranted.value = true
        }else{
            // 拒否された
            if (shouldShowRequestPermissionRationale(REQ_PERMISSION)){
                showRationaleDialog()
            }else{
                // 最終確認も拒否
                viewModel.isPermissionDinied.value = true
            }
        }
    }

    private fun showRationaleDialog() {
        TODO("Not yet implemented")
    }

    companion object{
        private const val REQ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
    }
}