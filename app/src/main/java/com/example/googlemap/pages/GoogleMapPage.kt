package com.example.googlemap.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.googlemap.pages.components.CurrentLocation
import com.example.googlemap.pages.components.DetailBox
import com.example.googlemap.pages.components.MapMarker
import com.example.googlemap.pages.components.MapType
import com.example.googlemap.vm.GoogleMapVM
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun GoogleMapPage(
    paddingValues: PaddingValues
){

    val vm : GoogleMapVM = hiltViewModel()
    val location by vm.location.collectAsState()
    val address by vm.address.collectAsState()
    val latLng = location?.let {
        LatLng(it.latitude, it.longitude)
    } ?: LatLng(1.3521, 103.8198);
    val markerState = rememberMarkerState(position = latLng)
    val cameraPositionState = rememberCameraPositionState() {
        position = CameraPosition.fromLatLngZoom(latLng, 10f)
    }
    val isCurrent by vm.isCurrent.collectAsState()

    val mapType by vm.mapType.collectAsState()

    val getCurrentLocation : () -> Unit = {
        vm.getLocation()
    }

    val updateMapType : () -> Unit  = {
        vm.updateMapType();
    }

    LaunchedEffect(location) {
        location?.let {
            val newLatLng = LatLng(it.latitude, it.longitude)
            markerState.position = newLatLng
            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(newLatLng, 10f))
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().padding(paddingValues)
    ){
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLongClick = { latLng ->
                vm.updateLocation(latLng.latitude,latLng.longitude)
            },
            properties = MapProperties(
                mapType = mapType
            )
        ) {
            if(location != null){
                MapMarker(markerState)
            }
        }
        MapType(modifier = Modifier.align(Alignment.TopEnd),mapType,updateMapType)
        if(location != null && address != null){
            DetailBox(location!!,address!!,modifier = Modifier.align(Alignment.TopEnd))
        }
        CurrentLocation(modifier = Modifier.align(Alignment.BottomEnd),isCurrent,getCurrentLocation)
    }
}