package com.metawebthree.keykeeper.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.metawebthree.keykeeper.ui.screen.tabbar.product.MeridiemType

@Composable
fun MeridiemButton(
    firstTitle: String = "AM",
    secondTitle: String = "PM",
    activeMeridiemType: MeridiemType = MeridiemType.AM,
    roundedCornerShape: Dp = 10.dp
) {
    Row {
        OutlinedButton(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = if (activeMeridiemType == MeridiemType.AM) MaterialTheme.colorScheme.primary else Color.Gray),
            shape = RoundedCornerShape(
                topStart = roundedCornerShape,
                bottomStart = roundedCornerShape
            )
        ) {
            Text(text = firstTitle)
        }
        OutlinedButton(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = if (activeMeridiemType == MeridiemType.PM) MaterialTheme.colorScheme.primary else Color.Gray),
            shape = RoundedCornerShape(topEnd = roundedCornerShape, bottomEnd = roundedCornerShape)
        ) {
            Text(text = secondTitle)
        }
    }
}

@Preview
@Composable
fun MeridiemButtonPreview() {
    MeridiemButton()
}