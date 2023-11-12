package com.example.photogallery.gallery

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PhotoGalleryFragment.newInstance())
                .commitNow()
        }

        // タップされた画像のURIをセットする
        viewModel.onSelect.observe(this,EventObserver{
            setResult(RESULT_OK, Intent().putExtra(INTENT_URI, it.toString()))
            finish()
        })
    }

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