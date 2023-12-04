package com.example.photogallery.gallery

import android.app.Application
import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.myphoto.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


data class PhotoGalleryItem(val uri: Uri, val folder: String){
    companion object{
        val DIFF_UTIL = object : DiffUtil.ItemCallback<PhotoGalleryItem>() {
            override fun areItemsTheSame(
                oldItem: PhotoGalleryItem,
                newItem: PhotoGalleryItem
            ): Boolean {
                return oldItem.uri == newItem.uri
            }

            override fun areContentsTheSame(
                oldItem: PhotoGalleryItem,
                newItem: PhotoGalleryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

// MediaStoreAPIでContextが必要なためAndroidViewModelを継承する
// (app:Application):AndroidViewModel(app)と記述する理由は↓
// ttps://www.fuwamaki.com/article/329
@HiltViewModel
class PhotoGalleryViewModel @Inject constructor(app:Application):AndroidViewModel(app){
    val photoList = MutableLiveData<List<PhotoGalleryItem>>()
    val photoFolderList = MutableLiveData<List<PhotoGalleryItem>>()
    val isPermissionGranted = MutableLiveData<Boolean>().apply { value = false }
    val isPermissionDenied = MutableLiveData<Boolean>().apply { value = false }

    val onSelect = MutableLiveData<Event<Uri>>()
    val onSelectFolder = MutableLiveData<Event<String>>()

    // 指定されたバッケトID内の画像ファイルを取得
    // folderArg: バケットIDを指定
    fun loadPhotoList(folderArg: String = "all"){
        viewModelScope.launch(Dispatchers.IO){
            val list = mutableListOf<PhotoGalleryItem>()
            // フォルダ表示用
//
            // 読み込む列を指定する
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
            )
            // 行の絞り込み条件を指定する nullはすべての行を読み込む
            val selection = null
            // 絞り込み条件の引数
            val selectionArgs = null
            // 並び順 nullは指定なし
            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

            getApplication<Application>().contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,selection, selectionArgs, sortOrder
            )?.use{cursor ->
                // idが格納されている列番号を取得
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
                while (cursor.moveToNext()){
                    val id = cursor.getLong(idColumn)
                    val uri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,id
                    )
                    val bucketId = cursor.getString(bucketIdColumn)

                    // タップされたフォルダ内のファイルだけリストに追加する
                    if (folderArg == "all"){
                        list.add(PhotoGalleryItem(uri,bucketId))
                    }else if (folderArg == bucketId) {
                        list.add(PhotoGalleryItem(uri, bucketId))
                    }
                }
            }
            photoList.postValue(list)
        }
    }

    // 画像ファイルがあるフォルダを取得
    fun loadPhotoFolderList(){
        viewModelScope.launch(Dispatchers.IO){
            // フォルダ表示用
            val folderList = mutableListOf<PhotoGalleryItem>()

            // 読み込む列を指定する
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
            )
            // 行の絞り込み条件を指定する nullはすべての行を読み込む
            val selection = null
            // 絞り込み条件の引数
            val selectionArgs = null
            // 並び順 nullは指定なし
            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

            getApplication<Application>().contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,selection, selectionArgs, sortOrder
            )?.use{cursor ->
                // idが格納されている列番号を取得
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
                val folderListTemp = arrayListOf<String>()
                while (cursor.moveToNext()){
                    val id = cursor.getLong(idColumn)
                    val uri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,id
                    )
                    val bucketId = cursor.getString(bucketIdColumn)

                    // フォルダ表示しない場合はif文なし
                    if (folderListTemp.contains(bucketId)){
                        continue
                    } else {
                        folderList.add(PhotoGalleryItem(uri,bucketId))
                        folderListTemp.add(bucketId)
                    }
                }
            }
            photoFolderList.postValue((folderList))
        }
    }

    // 画像ファイル一覧取得
    fun getPhotoItem(index: Int) = photoList.value?.getOrNull(index)

    // フォルダ一覧取得
    fun getPhotoFolderItem(index: Int) = photoFolderList.value?.getOrNull(index)

    fun onClick(item: PhotoGalleryItem){
        onSelect.value = Event(item.uri)
    }

    fun onClickFolder(item: PhotoGalleryItem){
        onSelectFolder.value = Event(item.folder)
    }
}