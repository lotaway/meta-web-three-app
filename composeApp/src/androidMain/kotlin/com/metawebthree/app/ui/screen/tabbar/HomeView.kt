package com.metawebthree.keykeeper.ui.screen.tabbar

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.metawebthree.keykeeper.BrowserActivity
import com.metawebthree.keykeeper.ui.components.GoodsList
import com.metawebthree.keykeeper.ui.components.IGoods
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import metawebthreeapp.composeapp.generated.resources.Res
import metawebthreeapp.composeapp.generated.resources.home_banner
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration.Companion.seconds

interface VideoArgs {
    val `package`: String
}

interface EmailArgs {
    val emailAddresses: Array<String>
    val title: String
    val content: String
}

sealed class BannerNavigateType {
    class Video(var args: VideoArgs) : BannerNavigateType()
    class Email(var args: EmailArgs) : BannerNavigateType()
    class Link(val link: String) : BannerNavigateType()
}

inline fun BannerNavigateType.isVideo(next: (VideoArgs) -> Unit) {
    if (this is BannerNavigateType.Video)
        next(args)
}

inline fun BannerNavigateType.isEmail(next: (EmailArgs) -> Unit) {
    if (this is BannerNavigateType.Email)
        next(args)
}

inline fun BannerNavigateType.isLink(next: (String) -> Unit) {
    if (this is BannerNavigateType.Link)
        next(link)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeView(context: Context?) {
    val bannerList = listOf(
        "https://kj.8248.net/images/upload/202206/06/202206061140091890.png",
        "https://kj.8248.net/images/upload/202206/06/202206061140212700.png",
        "https://kj.8248.net/images/cn/templates/MN043/banner3.png"
    )
    val targetType = BannerNavigateType.Link("https://way-luke.mysxl.cn")
    val composeScope = rememberCoroutineScope()
    val pageState = rememberPagerState()
    val bannerIndex = pageState.currentPage
    LaunchedEffect(Unit) {
        delay(duration = 4.seconds)
        (pageState.currentPage + 1).let {
            if (it < bannerList.size) it else 0
        }.run {
            pageState.animateScrollToPage(this)
        }
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(
                resource = Res.drawable.home_banner
            ),
            contentDescription = "Home banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    targetType.isVideo { videoArgs ->
                        Intent(Intent.ACTION_MAIN).also {
                            it.`package` = videoArgs.`package`
                            try {
                                context?.startActivity(it)
                            } catch (e: ActivityNotFoundException) {
                                e.printStackTrace()
                            }
                        }
                    }
                    targetType.isEmail { emailArgs ->
                        Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_EMAIL, emailArgs.emailAddresses)
                            putExtra(Intent.EXTRA_SUBJECT, emailArgs.title)
                            putExtra(Intent.EXTRA_TEXT, emailArgs.content)
                        }
                    }
                    targetType.isLink {
                        Intent(context, BrowserActivity::class.java).also {
                            context?.startActivity(it)
                        }
                    }
                }
        )
        SectionTitle(
            name = "Hello meta world",
            description = ""
        )
        Box {
            HorizontalPager(
                state = pageState,
                count = bannerList.size,
                contentPadding = PaddingValues(horizontal = 40.dp),
                modifier = Modifier
                    .height(200.dp)
            ) {
                val imageScale by animateFloatAsState(
                    targetValue = if (bannerIndex == it) 1f else 0.8f,
                    animationSpec = tween(150),
                )
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(imageScale)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.FillWidth,
                    model = ImageRequest.Builder(LocalContext.current).data(bannerList[it])
                        .scale(Scale.FILL).build(),
                    contentDescription = "Home Banner"
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                bannerList.indices.forEach {
                    RadioButton(selected = bannerIndex == it, onClick = {
                        composeScope.launch {
                            pageState.animateScrollToPage(page = it)
                        }
                    })
                }
            }
        }
    }
//            GoodsSection()
}

@Preview
@Composable
fun HomeViewPreview() {
    HomeView(context = null)
}

@Composable
fun SectionTitle(name: String, description: String) {
    Column {
        Row {
            Text(name)
            if (description != "")
                Text("welcome to $description")
        }
    }
}

@Preview
@Composable
fun SectionTitlePreview() {
    SectionTitle(
        description = "",
        name = "meta"
    )
}

@Composable
fun GoodsSection(gList: List<IGoods>) {
    var showType by remember { mutableStateOf(false) }
    SectionTitle(
        name = "Goods List",
        description = ""
    )
    Column {
        Button(onClick = {
            showType = !showType
        }) {
            Text("Switch Show Type")
        }
    }
    GoodsList(gList, showType)
}