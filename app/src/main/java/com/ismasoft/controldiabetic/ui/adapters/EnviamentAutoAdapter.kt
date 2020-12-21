package com.ismasoft.controldiabetic.ui.adapters

import android.content.ContentValues
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ismasoft.controldiabetic.data.model.ControlAmbId
import com.ismasoft.controldiabetic.data.model.VisitaAmbId
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.utilities.convertirADateLaDataFirebase
import kotlin.collections.ArrayList

class EnviamentAutoAdapter() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    /** Funcions del MV **/
    fun loginAutomatic(user: String, pass: String, enviamentAutoInterface : EnviamentAutoInterface) {

        firebaseAuth.signInWithEmailAndPassword(user, pass)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    Log.d(ContentValues.TAG, "Login correcte")
                    enviamentAutoInterface.credencialsOK()
                }else {
                    Log.d(ContentValues.TAG, "Login incorrecte")
                    enviamentAutoInterface.credencialsNOK()
                }
        }
    }

    fun recuperarLlistaControls(enviamentAutoInterface : EnviamentAutoInterface) {
        var llistaControls : ArrayList<ControlAmbId> = ArrayList()
        db.collection(Constants.DB_ROOT_USUARIS + "/" + firebaseAuth.currentUser?.uid + "/" + Constants.DB_ROOT_CONTROLS)
            .get()
            .addOnSuccessListener { result ->
                Log.d(ContentValues.TAG, "Documents recuperats")
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
                enviamentAutoInterface.llistaControlsOK(llistaControls)
            }
            .addOnFailureListener{
                enviamentAutoInterface.llistaControlsNOK()
            }
    }

    /** Funció que accedeix a la BBDD per recuperar la llista de visites de l'usuari **/
    fun recuperarLlistaVisites(enviamentAutoInterface : EnviamentAutoInterface) {
        val llistaVisites : java.util.ArrayList<VisitaAmbId> = java.util.ArrayList()
        db.collection(Constants.DB_ROOT_USUARIS + "/" + firebaseAuth.currentUser?.uid + "/" + Constants.DB_ROOT_VISITES)
            .get()
            .addOnSuccessListener { result ->
                Log.d(ContentValues.TAG, "Documents recuperats")
                for(document in result){
                    val visita = VisitaAmbId()
                    visita.idVisita = document.id.toString()
                    val data = convertirADateLaDataFirebase(document.data.get("dataVisita") as Timestamp)
                    visita.dataVisita = data
                    visita.motiu = document.data.get("motiu").toString()
                    llistaVisites.add(visita)
                }
                enviamentAutoInterface.visitesOK(llistaVisites)
            }
            .addOnFailureListener{
                Log.d(ContentValues.TAG, "Error al recuperar les visites")
                enviamentAutoInterface.visitesNOK()
            }
    }

    fun recuperarDadesUsuari(enviamentAutoInterface : EnviamentAutoInterface) {

        db.collection(Constants.DB_ROOT_USUARIS).document(firebaseAuth.currentUser?.uid.toString())
            .get()
            .addOnCompleteListener() { task  ->
                if(task .isSuccessful){
                    Log.d(ContentValues.TAG, "DocumentSnapshot recuperat with ID: ${task.result?.id}")
                    var nomUsuari = task.result?.get("nom").toString() +" "+ task.result?.get("primerCognom").toString() + " " + task.result?.get("segonCognom").toString()
                    var correuMetge = task.result?.get("correuElectronicMetge").toString()
                    enviamentAutoInterface.recuperarDadesUsuariOK(correuMetge,nomUsuari)
                }
                else{
                    Log.w(ContentValues.TAG, "Error recuperant l'usuari")
                    enviamentAutoInterface.recuperarDadesUsuariNOK()
                }
            }
    }

}