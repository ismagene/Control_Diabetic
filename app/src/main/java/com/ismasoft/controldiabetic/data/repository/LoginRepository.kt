package com.ismasoft.controldiabetic.data.repository

import android.app.Application
import android.util.Log
import android.util.Log.INFO
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
                        print("Login correcte")
                    }else{
                        logged.value = false
                        print("Login incorrecte")
                    }
                }
    }
}