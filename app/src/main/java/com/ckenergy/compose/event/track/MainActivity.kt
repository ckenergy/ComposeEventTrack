package com.ckenergy.compose.event.track

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ckenergy.cet.core.AutoTrackHelper
import com.ckenergy.cet.core.ComposeEventListener
import com.ckenergy.compose.event.track.ui.theme.ComposeEventTrackTheme

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AutoTrackHelper.setEventListener(object : ComposeEventListener{
            override fun onPageChange(route: String) {
//                TODO("Not yet implemented")
                Log.d(TAG, "onPageChange:$route")
            }

            override fun onClick(event: String) {
//                TODO("Not yet implemented")
                Log.d(TAG, "onClick:$event")
            }

        })
        setContent {
            ComposeEventTrackTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph()
                }
            }
        }
    }
}