package com.example.mytodo.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun Context.showToast(obj: Any) {
    val msg = when (obj) {
        is Int -> getString(obj)
        is Exception -> obj.message.toString()
        else -> obj.toString()
    }
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun View.showSnackBar(message: String, duration: Int) {
    Snackbar.make(this, message, duration).show()
}