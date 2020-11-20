package com.ismasoft.controldiabetic.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.ismasoft.controldiabetic.data.model.User

class RegistreRepository(val application: Application) {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var retornRegistre = MutableLiveData<Boolean>()

    fun requestRegistreUsuari(mail:String,password:String) {
        // call fireBaseservice
        firebaseAuth.createUserWithEmailAndPassword(mail, password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    retornRegistre.value = it.isSuccessful
                }else{
                    retornRegistre.value = false
                }
            }
    }

    fun insertarUsuariBBDD(Usuari: User){

    }

}