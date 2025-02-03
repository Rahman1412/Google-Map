package com.example.googlemap.pages.components

import androidx.compose.runtime.Composable
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun MapMarker(
    markerState: MarkerState
){
    Marker(
        state = markerState,
    )
}