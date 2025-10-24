package com.metawebthree.keykeeper.ui.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.metawebthree.keykeeper.service.SyncService

@Composable
fun SettingPage(context: Context?) {
    val isSyncOn = rememberSaveable {
        mutableStateOf(false)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Setting Page")
        Button(onClick = {
            Intent(context, SyncService::class.java).also { intent ->
                intent.action =
                    if (isSyncOn.value) SyncService.Actions.STOP.toString() else SyncService.Actions.START.toString()
                isSyncOn.value = !isSyncOn.value
                context?.startService(intent)
            }
        }) {
            Text(text = "SYNC")
        }
    }
}

@Preview
@Composable
fun SettingPageView() {
    SettingPage(null)
}