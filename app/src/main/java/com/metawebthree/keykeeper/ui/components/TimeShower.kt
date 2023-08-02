package com.metawebthree.keykeeper.ui.components

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

@Composable
fun TimeShower(texts: List<String>) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        texts.forEachIndexed { index, item ->
            TimeShowerItem(text = item, withQuote = index != texts.size - 1)
        }
    }
}

@Composable
fun TimeShower(hour: String, minute: String, second: String) {
    TimeShower(texts = listOf(hour, minute, second))
}

@Composable
fun TimeShowerItem(text: String, withQuote: Boolean = false) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
        )
    }
    if (withQuote)
        Text(text = ":")
}