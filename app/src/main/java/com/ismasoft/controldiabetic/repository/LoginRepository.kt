package com.ismasoft.controldiabetic.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class LoginRepository() {

    private lateinit var application:Application
    private lateinit var firebaseAuth:FirebaseAuth
//    private lateinit userMutableLiveData:MutableLiveData<FireBaseUser>

    private val _progressVisibility = MutableLiveData<Boolean>()
//    val progressVisibility = LiveData<Boolean> get() = _progressVisibility



}