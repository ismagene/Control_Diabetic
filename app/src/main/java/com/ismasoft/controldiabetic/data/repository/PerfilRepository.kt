package com.ismasoft.controldiabetic.data.repository

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
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

}