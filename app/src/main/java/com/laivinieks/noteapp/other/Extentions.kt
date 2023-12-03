package com.laivinieks.noteapp.other

import android.app.Activity
import android.content.Context

import android.os.Build

import androidx.core.content.ContextCompat



object Extentions {


    fun Activity.setStatusBarColor(color: Int,context:Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(context,color);
        }

    }


}