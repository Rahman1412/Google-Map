package com.example.googlemap.pages.components

import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun DetailBox(location: Location, address:String,modifier: Modifier){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(10.dp)
            .background(Color.Black.copy(alpha = 0.5f))
    ){
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(20.dp)
        ) {
            Text("Latitude  - ${location.latitude}", color = Color.White)
            Text("Longitude - ${location.longitude}",color = Color.White)
            Text(
            "Address - ${address}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )
        }
    }
}