package com.example.googlemap.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.example.googlemap.utils.ToastUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val locationService : FusedLocationProviderClient
) {

    suspend fun requestPermission(getLocation : () -> Unit) {
        Dexter.withContext(context)
            .withPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    getLocation()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    ToastUtils.displayToast(context,"Please grant location permission, After that you will be able to access location")
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest();
                }
            }
        ).check()
    }

    @SuppressLint("MissingPermission")
    suspend fun getLocation() : Result<Location?>{
        return try{
            val locationTask : Task<Location> = locationService.lastLocation
            val location = Tasks.await(locationTask)
            Result.success(location);
        }catch(e: Exception){
            Result.failure(e)
        }
    }
}