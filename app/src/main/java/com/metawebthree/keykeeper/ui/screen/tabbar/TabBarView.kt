package com.metawebthree.keykeeper.ui.screen.tabbar

import android.content.Context
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.metawebthree.keykeeper.R
import com.metawebthree.keykeeper.config.TabBarRoute
import com.metawebthree.keykeeper.ui.screen.tabbar.product.ProductsView

data class TabBarItem(
    val title: String = "",
    @DrawableRes val icon: Int,
    val url: String = ""
) {

}

class MutableColorState(override var value: String) : MutableState<String> {
    override fun component1(): String = value

    override fun component2(): (String) -> Unit {
        return {
            value = it
        }
    }

}

fun mutableColorOf(value: String): MutableState<String> {
    return MutableColorState(value)
}

@Composable
inline fun rememberColor(
    value: String? = null,
    crossinline f: @DisallowComposableCalls ((value: String) -> MutableState<String>) = {
        mutableColorOf(it)
    }
): MutableState<String> = f(value ?: "#000")

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun TabBarView(
    appNavController: NavHostController,
    context: Context
) {
    val screenOrientationState by rememberUpdatedState(LocalConfiguration.current.orientation)
    val tabBarModel = hiltViewModel<TabBarModel>()
    val isResponseScreenOrientationState =
        tabBarModel.isResponseScreenOrientation.collectAsStateWithLifecycle()
    val isTabBarVisible by tabBarModel.isVisible.collectAsStateWithLifecycle()
    val tabBarNavController = rememberNavController()
    var selectedTabBarIndex by remember {
        mutableStateOf(0)
    }
    var selectedColor by rememberColor()
    LaunchedEffect(screenOrientationState) {
        if (isResponseScreenOrientationState.value) {
            val isLandScape = screenOrientationState == ORIENTATION_LANDSCAPE
            tabBarModel.sendIntent(TabBarIntent.ToggleLandscape(isLandScape))
            tabBarModel.sendIntent(if (isLandScape) TabBarIntent.Hide else TabBarIntent.Show)
        }
    }
    tabBarNavController.addOnDestinationChangedListener { _, destination, _ ->
        TabBarRoute.apply {
            selectedTabBarIndex = when (destination.route) {
                HOME -> 0
                LIST -> 1
                MY -> 2
                else -> 0
            }
        }
    }
    val tabBarList = listOf<TabBarItem>(
        TabBarItem(
            title = "Home",
            icon = R.drawable.home,
            url = TabBarRoute.HOME
        ),
        TabBarItem(
            title = "Products",
            icon = R.drawable.category,
            url = TabBarRoute.LIST
        ),
        TabBarItem(
            title = "My",
            icon = R.drawable.user,
            url = TabBarRoute.MY
        )
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = isTabBarVisible,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                NavigationBar {
                    tabBarList.forEachIndexed { index, item ->
                        val isCurrent = selectedTabBarIndex == index
                        NavigationBarItem(
                            selected = isCurrent,
                            colors = NavigationBarItemDefaults.colors(
                                selectedTextColor = MaterialTheme.colorScheme.primary
                            ),
                            icon = {
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    modifier = Modifier.size(22.dp),
                                    contentDescription = item.title,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            label = {
                                Text(
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                    text = item.title
                                )
                            },
                            onClick = {
                                selectedTabBarIndex = index
                                tabBarNavController.tabNavigate(item.url)
                            }
                        )
                    }
                }
            }
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            NavHost(navController = tabBarNavController, startDestination = TabBarRoute.HOME) {
                composable(TabBarRoute.HOME) {
                    HomeView(
                        context = context
                    )
                }
                composable(TabBarRoute.LIST) {
                    ProductsView(tabBarModel = tabBarModel)
                }
                composable(TabBarRoute.MY) {
                    UserCenter(appNavCtrl = appNavController)
                }
            }
        }
    }
    /*BackHandler {
        when (selectedTabBarIndex) {
            0 -> (context as Activity).finish()
            else -> tabBarNavController.navigate(TabBarRoute.HOME)
        }
    }*/
}

fun NavHostController.tabNavigate(route: String) {
    this.navigate(route) {
        popUpTo(this@tabNavigate.graph.startDestinationId)
        launchSingleTop = true
    }
}