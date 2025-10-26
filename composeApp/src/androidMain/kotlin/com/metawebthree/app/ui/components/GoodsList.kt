package com.metawebthree.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

interface IGoods {
    val id: Int
    val name: String
}

sealed class GoodsShowType {
    class List
    class Grid
}

@Composable
fun GoodsList(gList: List<IGoods>, showType: Boolean) {
    val goodsDetailModifier = Modifier.clickable {
        // show the goods detail info
    }
    if (showType) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2)
        ) {
            items(gList) {
                Text(
                    text = it.name,
                    modifier = goodsDetailModifier
                )
            }
        }
    } else {
        LazyColumn {
            itemsIndexed(gList) { index, goods ->
                Text(
                    text = goods.name,
                    modifier = goodsDetailModifier,
                )
            }
        }
    }
}

@Preview
@Composable
fun TestGoodsList() {
//    GoodsList(gList = )
}