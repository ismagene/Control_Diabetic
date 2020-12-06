package com.ismasoft.controldiabetic.utilities

import android.content.Context
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import java.util.*

fun hideKeyboard(context: Context) {
    val imm = context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow((context as AppCompatActivity).findViewById<ViewGroup>(android.R.id.content).windowToken, 0)
}

fun convertirADateLaDataFirebase(dataFirebase : Timestamp): Date {
    return Date(dataFirebase.seconds * 1000 + dataFirebase.nanoseconds / 1000000)
}

