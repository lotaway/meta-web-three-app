package com.metawebthree.app.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.metawebthree.app.config.ApplicationRoute
import com.metawebthree.app.ui.theme.DefaultTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StartPage(navController: NavHostController) {
    val delayTime: Long = 3000
    val circleColor = MaterialTheme.colorScheme.onPrimaryContainer
    val lineColor = MaterialTheme.colorScheme.primaryContainer
    var lineAlpha by remember {
        mutableFloatStateOf(0f)
    }
    val animatable = remember {
        Animatable(0f)
    }
    var showText by remember {
        mutableStateOf(false)
    }
    var showIcon by remember {
        mutableStateOf(false)
    }
    var showLine by remember {
        mutableStateOf(false)
    }
    val canvasRotate by animateFloatAsState(
        targetValue = when {
            lineAlpha > 0.3f -> 360f
            else -> 0f
        },
        animationSpec = tween(durationMillis = delayTime.div(6).toInt()),
        label = ""
    )
    LaunchedEffect(Unit) {
        delay(delayTime)
        navController.popBackStack()
        navController.navigate(ApplicationRoute.HOME_PAGE)
    }
    LaunchedEffect(Unit) {
        val lastTime = delayTime.div(3)
        launch {
            delay(lastTime.div(2))
            showIcon = true
            delay(lastTime.div(4))
            showLine = true
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = delayTime.div(6).toInt(),
                    easing = LinearEasing
                )
            )
        }
        delay(lastTime)
        showText = true
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = showIcon,
            enter = scaleIn(/*initialScale = 0.1f*/)
        ) {
            Canvas(
                modifier = Modifier
                    .size(100.dp)
                    .rotate(canvasRotate)
            ) {
                val centerOffset = Offset(size.width / 2, size.height / 2)
                drawCircle(
                    color = circleColor,
                    radius = size.minDimension / 2,
                    center = centerOffset
                )
                if (showLine) {
                    lineAlpha = animatable.value
                    drawLine(
                        color = lineColor,
                        start = centerOffset,
                        end = Offset(
                            size.width / 2,
                            size.height / 2 + (size.height / 3)
                        ),
                        strokeWidth = 3.dp.toPx(),
                        cap = StrokeCap.Round,
                        alpha = lineAlpha
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = showText,
            enter = expandVertically(expandFrom = Alignment.Bottom) + fadeIn(initialAlpha = 0.1f)
        ) {
            Text(
                text = "Welcome to META WEB THREE",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartPagePreview() {
    DefaultTheme {
        val navHostController = rememberNavController()
        StartPage(navHostController)
    }
}