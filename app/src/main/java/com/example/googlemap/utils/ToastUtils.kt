package com.example.googlemap.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

object ToastUtils{
    private var toast : Toast? = null

    fun displayToast(context: Context, message :String = ""){
        Handler(Looper.getMainLooper()).post{
            toast?.cancel()
            toast = Toast.makeText(context,message,Toast.LENGTH_SHORT)
            toast?.show()
        }
    }
}