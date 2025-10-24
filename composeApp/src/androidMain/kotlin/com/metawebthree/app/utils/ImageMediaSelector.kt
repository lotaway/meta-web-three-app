package com.metawebthree.keykeeper.utils

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.util.Calendar

data class ImageMedia(val id: Long, val name: String, val uri: Uri)

fun ImageMediaSelector(context: Context): MutableList<ImageMedia> {
    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
    )
    val millisYesterday = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, -1)
    }.timeInMillis
    val selection = "${MediaStore.Images.Media.DATE_TAKEN} >= ?"
    val selectionArgs = arrayOf(millisYesterday.toString())
    var sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
    val images = mutableListOf<ImageMedia>()
    context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        sortOrder
    )?.use {
        val idColIndex = it.getColumnIndex(MediaStore.Images.Media._ID)
        val nameColIndex = it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
        while (it.moveToNext()) {
            val id = it.getLong(idColIndex)
            val name = it.getString(nameColIndex)
            val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            images.add(ImageMedia(id, name, uri))
        }
    }
    return images
}