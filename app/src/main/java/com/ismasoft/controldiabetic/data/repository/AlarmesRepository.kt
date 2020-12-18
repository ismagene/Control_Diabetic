package com.ismasoft.controldiabetic.data.repository

import android.app.Application
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ismasoft.controldiabetic.data.model.Alarma
import com.ismasoft.controldiabetic.data.model.AlarmaAmbId
import com.ismasoft.controldiabetic.data.repository.interfaces.AlarmesRepositoryInterface
import com.ismasoft.controldiabetic.ui.adapters.AlarmesListAdapterInterface
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.utilities.Constants.DB_ROOT_ALARMES
import com.ismasoft.controldiabetic.utilities.Constants.DB_ROOT_USUARIS
import java.util.*

class AlarmesRepository(val application: Application) {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun insertarAlarmaBBDD(alarma: Alarma, alarmaRepositoryInterface: AlarmesRepositoryInterface){
        db.collection(DB_ROOT_USUARIS + "/" + firebaseAuth.currentUser?.uid + "/" + DB_ROOT_ALARMES)
            .add(alarma)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with ID: ${it.id}")
                alarmaRepositoryInterface.afegirAlarmaOK(alarma.idAlarmaManager)
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

    fun recuperarLlistaAlarmes(alarmesRepositoryInterface : AlarmesRepositoryInterface){
        var llistaAlarmes : ArrayList<AlarmaAmbId> = ArrayList()

        db.collection(Constants.DB_ROOT_USUARIS + "/" + firebaseAuth.currentUser?.uid + "/" + Constants.DB_ROOT_ALARMES)
            .get()
            .addOnSuccessListener { result ->
                Log.d(ContentValues.TAG, "Documents recuperats")
                for(document in result){
                    var alarma = AlarmaAmbId()
                    alarma.idAlarma = document.id.toString()
                    alarma.idAlarmaManager = document.get("idAlarmaManager").toString().toInt()
                    alarma.horaAlarma = document.get("horaAlarma").toString()
                    llistaAlarmes.add(alarma)
                }
                alarmesRepositoryInterface.llistaAlarmesOK(llistaAlarmes)
            }
            .addOnFailureListener{
                alarmesRepositoryInterface.llistaAlarmesNOK()
            }
    }

    fun eliminarAlarma(idAlarma: String, position : Int, alarmesListAdapterInterface : AlarmesListAdapterInterface){
        db.collection(DB_ROOT_USUARIS + "/" + firebaseAuth.currentUser?.uid + "/" + DB_ROOT_ALARMES).document(idAlarma)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully deleted!")
                alarmesListAdapterInterface.eliminarAlarmaOK(position)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting document", e)
                alarmesListAdapterInterface.eliminarAlarmaNOK()
            }
    }

    fun modificarAlarma(alarma: AlarmaAmbId, alarmaRepositoryInterface: AlarmesRepositoryInterface){
        val user= FirebaseAuth.getInstance().currentUser
        db.collection(DB_ROOT_USUARIS + "/" + firebaseAuth.currentUser?.uid + "/" + DB_ROOT_ALARMES).document(alarma.idAlarma.toString())
            .update(mapOf(
                "horaAlarma" to alarma.horaAlarma
            ))
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Modificació de l'alarma OK")
                alarmaRepositoryInterface.modificarAlarmaOK(alarma.idAlarmaManager)
            }
            .addOnFailureListener{e ->
                Log.d(ContentValues.TAG, "Error modificació de l'alarma", e)
                alarmaRepositoryInterface.modificarAlarmaNOK()
            }

    }

    fun recuperarIdAlarmaNova(alarmaRepositoryInterface: AlarmesRepositoryInterface){
        db.collection(Constants.DB_ROOT_USUARIS + "/" + firebaseAuth.currentUser?.uid + "/" + Constants.DB_ROOT_ALARMES)
            .get()
            .addOnSuccessListener { result ->
                Log.d(ContentValues.TAG, "Documents recuperats")
                var idAlarma : Int = 0
                var total : Int = result.size()

                for(idIterator in 1..total){
                    var idTrobat = false
                    for(document in result) {
                        if (idIterator == document["idAlarmaManager"].toString().toInt()) {
                            idTrobat = true
                            break;
                        }
                    }
                    if(!idTrobat) {
                        idAlarma = idIterator
                        break
                    }
                }
                if(idAlarma == 0){
                    idAlarma = total + 1
                }
                alarmaRepositoryInterface.recuperarIdAlarmaNovaOK(idAlarma)
            }
            .addOnFailureListener{
                alarmaRepositoryInterface.recuperarIdAlarmaNovaNOK()
            }
    }

}