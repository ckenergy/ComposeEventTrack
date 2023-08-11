package com.ckenergy.compose.event.track

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SecondPage() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "second", modifier = Modifier
            .padding(10.dp)
            .clickable {
            })
    }
}
