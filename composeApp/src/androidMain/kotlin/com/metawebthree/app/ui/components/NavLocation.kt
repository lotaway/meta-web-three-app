package com.metawebthree.keykeeper.ui.components

import android.content.Context
import android.widget.LinearLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.amap.api.maps2d.MapView

@Composable
fun NavLocation() {
    //  高德地图没有适配compose，得使用操作原始xml的方式去使用地图SDK（需要申请账号）
    AndroidView(factory = { context ->
        /*val ll = LinearLayout(context)
        val myMapView = MyMapView(context)
        myMapView.onCreate()
        ll.addView(myMapView)*/
        MapView(context).apply {
            onCreate(null)
            map.uiSettings.isZoomControlsEnabled = true
        }
    })
}

@Preview
@Composable
fun NavLocationPreview() {
    NavLocation()
}

class MapGuideView(context: Context) : MapView(context) {
    fun onCreate() {
        super.onCreate(null)
        map.uiSettings.isZoomControlsEnabled = true
    }
}