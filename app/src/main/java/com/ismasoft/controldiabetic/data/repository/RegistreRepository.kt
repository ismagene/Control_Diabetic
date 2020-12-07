package com.ismasoft.controldiabetic.data.repository

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ismasoft.controldiabetic.utilities.Constants.DB_ROOT_USUARIS

class RegistreRepository(val application: Application) {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun comprobarExisteixEmail(mail: String, registreRepositoryInterface : RegistreRepositoryInterface){
        // call fireBaseService
        db.collection(DB_ROOT_USUARIS)
            .get()
            .addOnSuccessListener {
                var mailTrobat = false
                for (document in it) {
                    Log.d(TAG, "Document recuperat: ${document.id} => ${document.get("correuElectronic")}")
                    if (document.get("correuElectronic").toString().equals(mail)) {
                        mailTrobat = true
                        registreRepositoryInterface.comprobarExisteixEmailOK()
                        break
                    }
                }
                if(!mailTrobat) {
                    registreRepositoryInterface.comprobarExisteixEmailNOK()
                }
        }
    }

    fun requestRegistreUsuari(mail:String,password:String, registreRepositoryInterface : RegistreRepositoryInterface) {

        // call fireBaseservice
        firebaseAuth.createUserWithEmailAndPassword(mail, password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    Log.w(TAG, "Usuari creat a firebase")
                    registreRepositoryInterface.registreOK()
                }else{
                    Log.w(TAG, "Error al crear l'usuari a firebase")
                    registreRepositoryInterface.registreNOK()
                }
        }
    }

    fun insertarUsuariBBDD(user: com.ismasoft.controldiabetic.data.model.User, registreRepositoryInterface: RegistreRepositoryInterface){

        db.collection(DB_ROOT_USUARIS).document(firebaseAuth.currentUser?.uid.toString())
            .set(user)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with ID: ${firebaseAuth.currentUser?.uid.toString()}")
                registreRepositoryInterface.registreInsertarOK()
            }
            .addOnFailureListener {
                Log.w(TAG, "Error adding document", it)
                registreRepositoryInterface.registreInsertarNOK()
            }

    }

    fun restaurarContrasenya(email: String, registreRepositoryInterface: RegistreRepositoryInterface) {

        firebaseAuth.sendPasswordResetEmail(email.toString())
            .addOnCompleteListener(){
                if(it.isSuccessful){
                    Log.w(TAG, "reset de la contrasenya OK")
                    registreRepositoryInterface.registreOK()
                }
                else{
                    Log.w(TAG, "Error al fer reset de la contrasenya")
                    registreRepositoryInterface.registreNOK()
                }
            }


    }

}