package com.metawebthree.keykeeper.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.reflect.KProperty1

@FunctionalInterface
interface TimeShowerOptions {
    fun onClick(index: Int): Unit
}

@Composable
fun TimeShower(texts: List<String>, options: TimeShowerOptions? = null) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        texts.forEachIndexed { index, item ->
            TimeShowerItem(text = item, withQuote = index != texts.size - 1, {
                options?.onClick(index)
            })
        }
    }
}

@Composable
fun TimeShower(hour: String, minute: String, second: String, options: TimeShowerOptions? = null) {
    TimeShower(texts = listOf(hour, minute, second), options)
}

@Composable
fun TimeShowerItem(text: String, withQuote: Boolean = false, onClick: () -> Unit = {}) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.clickable { onClick() },
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
        )
    }
    if (withQuote)
        Text(text = ":")
}