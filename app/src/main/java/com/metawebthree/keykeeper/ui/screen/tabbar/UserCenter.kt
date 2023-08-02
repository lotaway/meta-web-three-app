package com.metawebthree.keykeeper.ui.screen.tabbar

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.metawebthree.keykeeper.config.ApplicationRoute

@Composable
fun UserCenter(appNavCtrl: NavHostController) {
    Text(text = "User Center")
    Button(onClick = {
        appNavCtrl.navigate(ApplicationRoute.SETTING_PAGE)
    }) {
        Text(text = "Setting")
    }
}