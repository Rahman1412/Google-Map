package com.example.googlemap.vm

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlemap.repository.PermissionRepository
import com.example.googlemap.utils.ToastUtils
import com.google.maps.android.compose.MapType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class GoogleMapVM @Inject constructor(
    private val permissionRepo: PermissionRepository,
    @ApplicationContext private val context:Context
) : ViewModel() {

    private val _location = MutableStateFlow<Location?>(null)
    val location : StateFlow<Location?> = _location;

    private val _address = MutableStateFlow<String?>(null)
    val address : StateFlow<String?> = _address

    private val _isCurrent = MutableStateFlow<Boolean>(false)
    val isCurrent :StateFlow<Boolean> = _isCurrent

    private val _mapType = MutableStateFlow<MapType>(MapType.NORMAL)
    val mapType :StateFlow<MapType> = _mapType


    init {
        viewModelScope.launch {
            permissionRepo.requestPermission()
            getLocation()
        }
    }

    fun getLocation(){
        _isCurrent.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = permissionRepo.getLocation()
                if(result.isSuccess){
                    _location.value = result.getOrNull()
                    _location.value?.let {
                        getAddress(it.latitude,it.longitude)
                    }
                }else{
                    ToastUtils.displayToast(context,result.exceptionOrNull()?.message.toString());
                }
            }
        }
    }

    fun updateLocation(latitude: Double, longitude: Double){
        _isCurrent.value = false
        getAddress(latitude,longitude)
        _location.value = Location("").apply {
            this.latitude = latitude
            this.longitude = longitude
        }
    }

    fun getAddress(latitude: Double, longitude: Double){
        viewModelScope.launch {
            val geocoder = Geocoder(context, Locale.getDefault())
            val address = geocoder.getFromLocation(latitude,longitude,1)
            if(address != null){
                _address.value = address[0].getAddressLine(0)
            }else{
                _address.value = ""
            }
        }
    }

    fun updateMapType(){
        if(_mapType.value == MapType.NORMAL){
            _mapType.value = MapType.SATELLITE
        }else{
            _mapType.value = MapType.NORMAL
        }
    }

}