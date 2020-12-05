package com.ismasoft.controldiabetic.data.repository

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ismasoft.controldiabetic.data.model.Control
import com.ismasoft.controldiabetic.utilities.Constants.DB_ROOT_CONTROLS
import com.ismasoft.controldiabetic.utilities.Constants.DB_ROOT_USUARIS
import java.util.*

class ControlsRepository(val application: Application) {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun insertarControlBBDD(
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

    fun obtenirRangsGlucosa(controlsRepositoryInterface: ControlsRepositoryInterface){
        // call fireBaseService
        val docRef = db.collection(DB_ROOT_USUARIS).document(firebaseAuth.currentUser?.uid.toString())
            docRef.get()
            .addOnSuccessListener { document ->
                if(document != null){
                    Log.d(TAG, "Document recuperat: ${document.data}")
                    controlsRepositoryInterface.obtenirRangsOK(document)
                }
                else{
                    Log.d(TAG, "Document no trobat")
                    controlsRepositoryInterface.obtenirRangsNOK()
                }
            }
            .addOnFailureListener(){ exception ->
                Log.d(TAG, "Error al obtenir el document", exception)
                controlsRepositoryInterface.obtenirRangsNOK()
            }
    }

    fun recuperarLlistaControls(controlsRepositoryInterface: ControlsRepositoryInterface) {
        var llistaControls : MutableList<Control> = ArrayList()

        db.collection(DB_ROOT_USUARIS + "/" + firebaseAuth.currentUser?.uid + "/" + DB_ROOT_CONTROLS)
            .get()
            .addOnSuccessListener { result ->
                for(document in result){
                    var control : Control = Control()
//                    var data = document.data.get("dataControl")
//                    control.dataControl?.time ?: data
                    control.valorGlucosa = document.get("valorGlucosa").toString().toInt()
                    control.valorInsulina = document.get("valorInsulina").toString().toInt()
                    control.dataControl =null
                    control.esDespresDeApat = document.get("esDespresDeApat") as Boolean

                    llistaControls.add(control)
                }
                controlsRepositoryInterface.llistaControlsOK(llistaControls)
            }
    }

}