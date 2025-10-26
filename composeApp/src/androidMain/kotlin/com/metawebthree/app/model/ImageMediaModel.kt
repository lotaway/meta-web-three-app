package com.metawebthree.app.model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.metawebthree.app.utils.ImageMedia
import com.metawebthree.app.utils.ImageMediaSelector

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