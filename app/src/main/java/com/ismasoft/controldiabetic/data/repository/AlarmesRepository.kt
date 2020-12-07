package com.ismasoft.controldiabetic.data.repository

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.type.DateTime
import com.ismasoft.controldiabetic.data.model.Alarma
import com.ismasoft.controldiabetic.data.model.Control
import com.ismasoft.controldiabetic.utilities.Constants.DB_ROOT_ALARMES
import com.ismasoft.controldiabetic.utilities.Constants.DB_ROOT_CONTROLS
import com.ismasoft.controldiabetic.utilities.Constants.DB_ROOT_USUARIS
import java.text.SimpleDateFormat
import java.util.*

class AlarmesRepository(val application: Application) {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun insertarAlarmaBBDD(alarma: Alarma, alarmaRepositoryInterface: AlarmesRepositoryInterface){
        db.collection(DB_ROOT_USUARIS + "/" + firebaseAuth.currentUser?.uid + "/" + DB_ROOT_ALARMES)
            .add(alarma)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with ID: ${it.id}")
                alarmaRepositoryInterface.afegirAlarmaOK()
            }
            .addOnFailureListener {
                Log.w(TAG, "Error adding document", it)
                alarmaRepositoryInterface.afegirAlarmaNOK()
            }
    }

    fun comprobarExisteixAlarma(alarma: String, alarmaRepositoryInterface: AlarmesRepositoryInterface) {
        db.collection(DB_ROOT_USUARIS + "/" + firebaseAuth.currentUser?.uid + "/" + DB_ROOT_ALARMES)
            .get()
            .addOnSuccessListener {
                var alarmaTrobada = false
                for (document in it) {
                    Log.d(TAG, "Document recuperat: ${document.id} => ${document.get("correuElectronic")}")
                    if (document.get("horaAlarma").toString().equals(alarma)) {
                        alarmaTrobada = true
                        alarmaRepositoryInterface.jaExisteixAlarma()
                        break
                    }
                }
                if(!alarmaTrobada) {
                    alarmaRepositoryInterface.noExisteixAlarma()
                }
            }
    }


}