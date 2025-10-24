package com.metawebthree.keykeeper.ui.screen.tabbar

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.metawebthree.keykeeper.config.ApplicationRoute
import com.metawebthree.keykeeper.model.ImageMediaModel

@Composable
fun UserCenter(context: Context, appNavCtrl: NavHostController) {
    val imageMediaModel = viewModel<ImageMediaModel>()
    imageMediaModel.updateImages(context)
    Text(text = "User Center")
    Button(onClick = {
        appNavCtrl.navigate(ApplicationRoute.SETTING_PAGE)
    }) {
        Text(text = "Setting")
    }
    LazyColumn(modifier= Modifier.fillMaxSize()) {
        items(imageMediaModel.imageMedias) {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(model = it.uri, contentDescription = "Image media")
            }
        }
    }
}