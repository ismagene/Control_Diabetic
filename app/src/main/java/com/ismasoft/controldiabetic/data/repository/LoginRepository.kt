package com.ismasoft.controldiabetic.data.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class LoginRepository(val application: Application) {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var logged = MutableLiveData<Boolean>()

    suspend fun requestLogin(mail:String,password:String)  {
        firebaseAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        logged.value = it.isSuccessful
                    }else{
                        logged.value = false
                    }
                }
    }
}