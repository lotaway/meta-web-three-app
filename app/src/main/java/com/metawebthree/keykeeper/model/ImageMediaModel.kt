package com.metawebthree.keykeeper.model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.metawebthree.keykeeper.utils.ImageMedia
import com.metawebthree.keykeeper.utils.ImageMediaSelector

class ImageMediaModel: ViewModel() {
    var imageMedias by mutableStateOf(emptyList<ImageMedia>())
        private set

    fun updateImages(_imageMedias: List<ImageMedia>) {
        imageMedias = _imageMedias
    }

    fun updateImages(context: Context) {
        ImageMediaSelector(context).run {
            imageMedias = this
        }
    }
}