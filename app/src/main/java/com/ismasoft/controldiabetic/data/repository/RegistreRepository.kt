package com.ismasoft.controldiabetic.data.repository

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ismasoft.controldiabetic.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistreRepository(val application: Application) {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    var retornRegistre = MutableLiveData<Boolean>()



    fun requestRegistreUsuari(mail:String,password:String) {

        // call fireBaseservice
        firebaseAuth.createUserWithEmailAndPassword(mail, password)
            .addOnCompleteListener{
                var prova = it.result
                if(it.isSuccessful){
                    retornRegistre.value = it.isSuccessful
                }else{
                    retornRegistre.value = false
                }
        }

    }

    fun insertarUsuariBBDD(){
        var rutaIdUsuari : String =""
        db.collection("users")
                .add(user)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot added with ID: ${it.id}")
                    rutaIdUsuari = it.id
                    db.collection("users/"+it.id+"/visites")
                        .add(user)
                        .addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot added with ID: ${it.id}")
                        }
                        .addOnFailureListener {
                            Log.w(TAG, "Error adding document", it)
                        }
                }
                .addOnFailureListener {
                    Log.w(TAG, "Error adding document", it)
                }

    }

    val user = hashMapOf(
            "first" to "Ada",
            "last" to "Lovelace",
            "born" to 1815
    )

}