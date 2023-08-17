package com.ckenergy.compose.event.track

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MainPage(start: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        MyText(modifier = Modifier.padding(10.dp), start)
    }
}


/**
 * myText
 */
@Composable
fun MyText(modifier: Modifier, start: () -> Unit) {
    Text(text = "start", modifier = modifier
        .let {
            if (Math.random() > 0.5) it.background(Color.White) else it.background(Color.White)
        }
        .clickable(onClick = start)
    )
}
