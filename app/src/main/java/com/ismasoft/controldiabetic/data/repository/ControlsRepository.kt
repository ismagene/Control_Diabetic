package com.ismasoft.controldiabetic.data.repository

import android.app.Application
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.type.DateTime
import com.ismasoft.controldiabetic.data.model.Control
import com.ismasoft.controldiabetic.data.model.ControlAmbId
import com.ismasoft.controldiabetic.ui.adapters.ControlsListAdapterInterface
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.utilities.Constants.DB_ROOT_CONTROLS
import com.ismasoft.controldiabetic.utilities.Constants.DB_ROOT_USUARIS
import com.ismasoft.controldiabetic.utilities.convertirADateLaDataFirebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

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
        var llistaControls : ArrayList<ControlAmbId> = ArrayList()
        db.collection(DB_ROOT_USUARIS + "/" + firebaseAuth.currentUser?.uid + "/" + DB_ROOT_CONTROLS)
            .get()
            .addOnSuccessListener { result ->
                Log.d(TAG, "Documents recuperats")
                for(document in result){
                    var control : ControlAmbId = ControlAmbId()
                    control.valorGlucosa = document.get("valorGlucosa").toString().toInt()
                    control.valorInsulina = document.get("valorInsulina").toString().toInt()
                    val data = convertirADateLaDataFirebase(document.data.get("dataControl") as Timestamp)
                    control.dataControl = data
                    control.esDespresDeApat = document.get("esDespresDeApat") as Boolean
                    control.idControl = document.id

                    llistaControls.add(control)
                }
                controlsRepositoryInterface.llistaControlsOK(llistaControls)
            }
            .addOnFailureListener{
                controlsRepositoryInterface.LlistaControlsNOK()
            }
    }

    fun eliminarControl(idControl: String, position: Int, controlsListAdapterInterface: ControlsListAdapterInterface) {
        db.collection(DB_ROOT_USUARIS + "/" + firebaseAuth.currentUser?.uid + "/" + Constants.DB_ROOT_CONTROLS).document(idControl)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully deleted!")
                controlsListAdapterInterface.eliminarControlOK(position)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting document", e)
                controlsListAdapterInterface.eliminarControlNOK()
            }
    }

    fun modificarControl(controlModificar: ControlAmbId, controlsRepositoryInterface : ControlsRepositoryInterface){
        db.collection(DB_ROOT_USUARIS + "/" + firebaseAuth.currentUser?.uid + "/" + Constants.DB_ROOT_CONTROLS).document(controlModificar.idControl.toString())
            .update(mapOf(
                "dataControl" to controlModificar.dataControl,
                "esDespresDeApat" to controlModificar.esDespresDeApat,
                "valorGlucosa" to controlModificar.valorGlucosa,
                "valorInsulina" to controlModificar.valorInsulina
            ))
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Modificació del control OK")
                controlsRepositoryInterface.modificarControlOK()
            }
            .addOnFailureListener{e ->
                Log.d(ContentValues.TAG, "Error modificació del control", e)
                controlsRepositoryInterface.modificarControlNOK()
            }
    }

}