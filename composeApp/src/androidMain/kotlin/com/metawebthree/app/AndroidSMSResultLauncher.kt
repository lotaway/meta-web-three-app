package com.metawebthree.app

import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

class AndroidSMSResultLauncher {

    private var activity: ComponentActivity?
        get() {
            if (activity == null) {
                throw RuntimeException("activity not init!")
            }
            return activity
        }
        set(value) {
            activity = value
        }

    fun init(activity: ComponentActivity) {
        this.activity = activity
    }

    fun build(): SMSLauncher {
        val result = activity?.registerForActivityResult(ActivityResultContracts.RequestPermission().also {
            it.createIntent(activity as Context, Manifest.permission.RECEIVE_SMS)
        }) {
            when (it) {
                false -> Toast.makeText(
                    activity,
                    "未授权，无法实现预定的功能：RECEIVE_SMS！",
                    Toast.LENGTH_SHORT
                )
                    .show()

                else -> {}
            }
        }
        return result as SMSLauncher
    }

}