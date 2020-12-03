package com.ismasoft.controldiabetic.data.repository

import android.app.Application
import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class LoginRepository(val application: Application) {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun requestLogin(mail:String, password:String, loginRepositoryInterface: LoginRepositoryInterface)  {

        firebaseAuth.signInWithEmailAndPassword(mail, password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    Log.d(ContentValues.TAG, "Login correcte")
                    loginRepositoryInterface.credencialsOK()
                }else {
                    Log.d(ContentValues.TAG, "Login incorrecte")
                    loginRepositoryInterface.credencialsNOK()
                }
        }
    }
}