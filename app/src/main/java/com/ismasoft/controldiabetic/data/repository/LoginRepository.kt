package com.ismasoft.controldiabetic.data.repository

import android.app.Application
import android.util.Log
import android.util.Log.INFO
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class LoginRepository(val application: Application) {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var logged = MutableLiveData<Boolean>()

    suspend fun requestLogin(mail:String,password:String)  {
        firebaseAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener{
            if(it.isSuccessful){
                logged.value = it.isSuccessful
                print("Login correcte")
            }else {
                logged.value = false
                print("Login incorrecte")
            }
        }
    }
}