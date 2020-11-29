package com.ismasoft.controldiabetic.data.repository

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope

class RegistreRepository(val application: Application) {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    var retornRegistre = MutableLiveData<Boolean>()
    var mailTrobat = MutableLiveData<Boolean>(false)

    fun comprobarExisteixEmail(mail: String){

        db.collection("usuaris")
            .get()
            .addOnSuccessListener{
                for(document in it) {
                    Log.d(TAG, "Document recuperat: ${document.id} => ${document.get("correuElectronic")}")
                    if (document.get("correuElectronic").toString().equals(mail)) {
                        mailTrobat.value = true
//                        registreRepositoryInterface.comprobarExisteixEmailOK()
//                        break
                    }
                }
//                if(mailTrobat.value == false){
//                    registreRepositoryInterface.comprobarExisteixEmailNOK()
//                }
            }
    }

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
        db.collection("usuaris")
            .add(user)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with ID: ${it.id}")
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