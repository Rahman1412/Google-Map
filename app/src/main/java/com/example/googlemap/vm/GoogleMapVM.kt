package com.example.googlemap.vm

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.googlemap.repository.PermissionRepository
import com.example.googlemap.utils.ToastUtils
import com.google.maps.android.compose.MapType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
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

    private val getLocation : () -> Unit = {
        getLocation()
    }


    init {
        viewModelScope.launch {
            permissionRepo.requestPermission(getLocation)
        }
    }

    fun getLocation(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _isCurrent.value = true
                val result = permissionRepo.getLocation()
                if(result.isSuccess){
                    _location.value = result.getOrNull()
                    _location.value?.let {
                        val address = getAddress(it.latitude,it.longitude)
                        if(address != ""){
                            _address.value = address;
                        }
                    }
                }else{
                    ToastUtils.displayToast(context,result.exceptionOrNull()?.message.toString());
                }
            }
        }
    }

    fun updateLocation(latitude: Double, longitude: Double){
        viewModelScope.launch {
            try{
                val address = getAddress(latitude,longitude)
                if(address == ""){
                    return@launch
                }
                _address.value = address
                _isCurrent.value = false
                _location.value = Location("").apply {
                    this.latitude = latitude
                    this.longitude = longitude
                }
            }catch(e:Exception){
                ToastUtils.displayToast(context,e.message.toString())
            }
        }
    }

    private suspend fun getAddress(latitude: Double, longitude: Double): String {
        return withContext(Dispatchers.IO){
            try{
                val geocoder = Geocoder(context, Locale.getDefault())
                val address = geocoder.getFromLocation(latitude,longitude,1)
                if(address != null){
                    address[0].getAddressLine(0)
                }else{
                    ""
                }
            }catch(e:Exception){
                ToastUtils.displayToast(context,e.message.toString())
                ""
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