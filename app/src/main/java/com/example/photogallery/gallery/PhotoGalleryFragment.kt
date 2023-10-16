package com.example.photogallery.gallery

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.PermissionChecker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.LifecycleOwner
import com.example.photogallery.R
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalStateException


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let{
            val result = PermissionChecker.checkSelfPermission(it, REQ_PERMISSION)
            if (result == PermissionChecker.PERMISSION_GRANTED){
                viewModel.isPermissionGranted.value = true
            }else{
                permissionRequest.launch(REQ_PERMISSION)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.isPermissionGranted.value == true){
            viewModel.loadPhotoList()
        }
    }

    companion object{
        private const val REQ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private fun showRationaleDialog() {
        RationalDialog().show(childFragmentManager,viewLifecycleOwner){
            if (it == RationalDialog.RESULT_OK){
                permissionRequest.launch(REQ_PERMISSION)
            }else{
                viewModel.isPermissionDinied.value = true
            }
        }
    }

    class RationalDialog:DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                AlertDialog.Builder(it)
                    .setMessage("デバイス内の写真を表示するにはアクセスを許可してください。")
                    .setPositiveButton("OK"){_, _ ->
                        setFragmentResult(REQUEST_KEY, bundleOf(Pair(RESULT_KEY, RESULT_OK)))
                    }
                    .setNegativeButton("Cancel"){_, _ ->
                        setFragmentResult(REQUEST_KEY, bundleOf(Pair(RESULT_KEY, RESULT_CANCEL)))
                    }
                    .create()
            }?:throw IllegalStateException("Activity cannot be null")
        }
        fun show(manager: FragmentManager, lifecycleOwner: LifecycleOwner,callback:(Int) -> Unit){
            val listener = FragmentResultListener{_,result ->
                val resultValue = result.getInt(RESULT_KEY)
                callback(resultValue)
            }
            manager.setFragmentResultListener(RESULT_KEY, lifecycleOwner,listener)
            show(manager, TAG)
        }
        companion object{
            private const val TAG = "TAG_RATIONALE_DIALOG"
            private const val REQUEST_KEY = "RATIONALE_DIALOG_REQUEST_KEY"
            private const val RESULT_KEY = "RATIONALE_DIALOG_RESULT_KEY"
            const val RESULT_OK = 1
            const val RESULT_CANCEL = -1
        }
    }
}