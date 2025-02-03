package com.example.googlemap.pages.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.googlemap.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentLocation(modifier: Modifier,isCurrent:Boolean,getCurrentLocation : () -> Unit){
    IconButton(
        onClick = {
            getCurrentLocation()
        },
        modifier = modifier.padding(bottom = 110.dp, end = 8.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        Icon(
            painter = painterResource(R.drawable.button),
            contentDescription = "Current Location Icon",
            tint = if(isCurrent) Color.Blue else Color.Black
        )
    }
}