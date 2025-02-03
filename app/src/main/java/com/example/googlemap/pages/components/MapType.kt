package com.example.googlemap.pages.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.googlemap.R
import com.google.maps.android.compose.MapType

@Composable
fun MapType(
    modifier: Modifier,
    mapType:MapType,
    updateMapType : () -> Unit
){
    IconButton(
        onClick = {
            updateMapType()
        },
        modifier = modifier
            .padding(top = 180.dp, end = 8.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        Icon(
            painter = painterResource(if(mapType == MapType.NORMAL) R.drawable.satellite else R.drawable.map),
            contentDescription = "Satellite Or Default",
            tint = Color.Unspecified
        )
    }
}