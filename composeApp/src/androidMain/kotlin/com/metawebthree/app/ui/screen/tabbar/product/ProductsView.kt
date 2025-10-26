package com.metawebthree.app.ui.screen.tabbar.product

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.metawebthree.app.ui.components.LoadingProcess
import com.metawebthree.app.ui.components.MeridiemButton
import com.metawebthree.app.ui.components.TimeShower
import com.metawebthree.app.ui.components.TimeShowerOptions
import com.metawebthree.app.ui.screen.tabbar.TabBarIntent
import com.metawebthree.app.ui.screen.tabbar.TabBarModel
import kotlinx.coroutines.delay

@SuppressLint("ContextCastToActivity")
@Composable
fun ProductsView(tabBarModel: TabBarModel = hiltViewModel()) {
    val activity = LocalContext.current as Activity
    val isLandscape = tabBarModel.isLandscape.collectAsStateWithLifecycle()
    val productsViewModel = viewModel<ProductsViewModel>()
    val dateState by productsViewModel.dateState.collectAsStateWithLifecycle()
    val timeState by productsViewModel.timeState.collectAsStateWithLifecycle()
    val uiState by productsViewModel.uiState.collectAsStateWithLifecycle()
//    val defaultOrientation = remember { activity.requestedOrientation }
    LaunchedEffect(isLandscape.value) {
        productsViewModel.sendIntent(ProductIntent.ToggleLoadingView(true))
        delay(300)
        if (isLandscape.value) {
            if (activity.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        productsViewModel.sendIntent(ProductIntent.ToggleLoadingView(false))
    }
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Products", fontSize = 18.sp)
                MeridiemButton(activeMeridiemType = if (timeState.hour >= 12) MeridiemType.PM else MeridiemType.AM)
                Switch(checked = isLandscape.value, onCheckedChange = {
                    tabBarModel.sendIntent(TabBarIntent.ToggleLandscape(it))
                })
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${dateState.date}(${dateState.week})",
                    modifier = Modifier.padding(bottom = 15.dp)
                )
                TimeShower(
                    hour = if (uiState.is24hours) timeState.get24HoursStr() else timeState.getMeridiemHourStr(),
                    minute = timeState.getMinuteStr(),
                    second = timeState.getSecondStr(),
                    options = object: TimeShowerOptions {
                        override fun onClick(index: Int): Unit {
                            when (index) {
                                0 -> productsViewModel.sendIntent(ProductIntent.ToggleHoursSystem())
                                else -> {}
                            }
                        }
                    }
                )
            }
            if (uiState.loading)
                LoadingProcess()
        }
    }
}

@Preview
@Composable
fun ProductsViewPreview() {
    ProductsView()
}