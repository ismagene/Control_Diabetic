package com.ismasoft.controldiabetic.data.repository

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginRepository(val application: Application) {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var logged = MutableLiveData<Boolean>()

    fun requestLogin(mail:String,password:String) {
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