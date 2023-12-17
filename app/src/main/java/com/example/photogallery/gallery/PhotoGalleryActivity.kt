package com.example.photogallery.gallery

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import com.example.myphoto.util.EventObserver
import com.example.photogallery.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PhotoGalleryActivity : AppCompatActivity() {
    private val viewModel: PhotoGalleryViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_gallery)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater: MenuInflater = menuInflater
//        inflater.inflate(R.menu.menu_photogallery, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return  when(item.itemId){
//            R.id.rotate_item -> {
//
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//        Log.v("rotate_item_activity", "Activity")
//        return super.onOptionsItemSelected(item)
//    }

    class ResultContract:ActivityResultContract<Unit, Uri?>(){
        override fun createIntent(context: Context, input: Unit) =
            Intent(context,PhotoGalleryActivity::class.java)

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return if (resultCode == RESULT_OK){
                intent?.getStringExtra(INTENT_URI)?.let {
                    Uri.parse(it)
                }
            }else{
                null
            }
        }

    }

    companion object{
        const val INTENT_URI = "Uri"
    }
}