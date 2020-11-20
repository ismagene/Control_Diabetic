package com.ismasoft.controldiabetic.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class LoginRepository(val application: Application) {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun requestLogin(mail:String,password:String){
        // call fireBaseservice
        firebaseAuth.signInWithEmailAndPassword(mail, password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    Log.d("login","Login succes")
                }else{
                    Log.d("login","Login failed")
                }
            }
    }

//    private lateinit userMutableLiveData:MutableLiveData<FireBaseUser>

    private val _progressVisibility = MutableLiveData<Boolean>()
//    val progressVisibility = LiveData<Boolean> get() = _progressVisibility



}