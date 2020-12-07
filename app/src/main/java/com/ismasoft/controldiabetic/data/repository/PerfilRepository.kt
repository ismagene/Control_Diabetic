package com.ismasoft.controldiabetic.data.repository

import android.app.Application
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ismasoft.controldiabetic.data.model.Control
import com.ismasoft.controldiabetic.utilities.Constants.DB_ROOT_CONTROLS
import com.ismasoft.controldiabetic.utilities.Constants.DB_ROOT_USUARIS

class PerfilRepository(val application: Application) {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun modificarDadesBBDD(
        control: Control,
        controlsRepositoryInterface: ControlsRepositoryInterface
    ){

        db.collection(DB_ROOT_USUARIS + "/" + firebaseAuth.currentUser?.uid + "/" + DB_ROOT_CONTROLS)
            .add(control)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with ID: ${it.id}")
                controlsRepositoryInterface.afegirControlOK()
            }
            .addOnFailureListener {
                Log.w(TAG, "Error adding document", it)
                controlsRepositoryInterface.afegirControlNOK()
            }
    }

    fun recuperarDadesUsuari(perfilRepositoryInterface: PerfilRepositoryInterface) {

        db.collection(DB_ROOT_USUARIS).document(firebaseAuth.currentUser?.uid.toString())
            .get()
            .addOnCompleteListener() { task  ->
                if(task .isSuccessful){
                    Log.d(TAG, "DocumentSnapshot recuperat with ID: ${task.result?.id}")
                    perfilRepositoryInterface.recuperarDadesPersonalsOK(task.result)
                }
                else{
                    Log.w(TAG, "Error recuperant l'usuari")
                    perfilRepositoryInterface.recuperarDadesPersonalsNOK()
                }
            }

    }

    fun validarContrasenya(contrasenya: String, perfilRepositoryInterface: PerfilRepositoryInterface) {

        val user= FirebaseAuth.getInstance().currentUser
        var credential = EmailAuthProvider.getCredential(user?.email.toString(),contrasenya)

        user?.reauthenticate(credential)
            ?.addOnCompleteListener{
                if(it.isSuccessful){
                    Log.d(ContentValues.TAG, "contrasenya correcte")
                    perfilRepositoryInterface.validarContrasenyaOK()
                }else {
                    Log.d(ContentValues.TAG, "contrasenya incorrecte")
                    perfilRepositoryInterface.validarContrasenyaNOK()
                }
            }
    }

    fun modificarContrasenya(contrasenya : String, perfilRepositoryInterface: PerfilRepositoryInterface) {
        val user= FirebaseAuth.getInstance().currentUser
        user?.updatePassword(contrasenya)
            ?.addOnCompleteListener(){
                if(it.isSuccessful){
                    Log.d(ContentValues.TAG, "contrasenya correcte")
                    perfilRepositoryInterface.modificarContrasenyaOK()
                }else {
                    Log.d(ContentValues.TAG, "contrasenya incorrecte")
                    perfilRepositoryInterface.modificarContrasenyaNOK()
                }
            }
    }

    fun tancarSessio(){
        firebaseAuth.signOut()
    }
}