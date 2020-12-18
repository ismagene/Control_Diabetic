package com.ismasoft.controldiabetic.data.repository

import android.app.Application
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ismasoft.controldiabetic.data.model.Visita
import com.ismasoft.controldiabetic.data.model.VisitaAmbId
import com.ismasoft.controldiabetic.data.repository.interfaces.VisitesRepositoryInterface
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.utilities.Constants.DB_ROOT_USUARIS
import com.ismasoft.controldiabetic.utilities.convertirADateLaDataFirebase
import java.util.*

class VisitesRepository(val application: Application) {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    /** Funció que accedeix a la BBDD per recuperar la llista de visites de l'usuari **/
    fun recuperarLlistaVisites(visitesRepositoryInterface : VisitesRepositoryInterface){
        val llistaVisites : ArrayList<VisitaAmbId> = ArrayList()
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
                visitesRepositoryInterface.llistaVisitesOK(llistaVisites)
            }
            .addOnFailureListener{
                visitesRepositoryInterface.llistaVisitesNOK()
            }
    }

    /** Funció que accedeix a la BBDD per recuperar la visita amb la data mes recent  **/
    fun recuperarUltimaVisita(visitesRepositoryInterface : VisitesRepositoryInterface){
        db.collection(Constants.DB_ROOT_USUARIS + "/" + firebaseAuth.currentUser?.uid + "/" + Constants.DB_ROOT_VISITES)
            .get()
            .addOnSuccessListener { result ->
                Log.d(ContentValues.TAG, "Documents recuperats")
                var existeixVisitaProgramada : Boolean = false
                for(document in result){
                    val data = convertirADateLaDataFirebase(document.data.get("dataVisita") as Timestamp)
                    if(data.after(Date())){
                        existeixVisitaProgramada = true
                    }
                }
                if(existeixVisitaProgramada) {
                    visitesRepositoryInterface.existeixVisitaVigent()
                }else{
                    visitesRepositoryInterface.noExisteixVisitaVigent()
                }
            }
            .addOnFailureListener{
                visitesRepositoryInterface.errorAlConsultarVisitaVigent()
            }
    }

    /** Funció que guarda una nova visita a la BBDD  **/
    fun guardarVisita(visita: Visita, visitesRepositoryInterface : VisitesRepositoryInterface){
        db.collection(DB_ROOT_USUARIS + "/" + firebaseAuth.currentUser?.uid + "/" + Constants.DB_ROOT_VISITES)
            .add(visita)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with ID: ${it.id}")
                visitesRepositoryInterface.afegirVisitaOK()
            }
            .addOnFailureListener {
                Log.w(TAG, "Error adding document", it)
                visitesRepositoryInterface.afegirVisitaNOK()
            }
    }

    /** Funció que modifica una visita existent **/
    fun modificarVisita(visitaModificar: VisitaAmbId, visitesRepositoryInterface : VisitesRepositoryInterface){
        db.collection(DB_ROOT_USUARIS + "/" + firebaseAuth.currentUser?.uid + "/" + Constants.DB_ROOT_VISITES).document(visitaModificar.idVisita.toString())
            .update(mapOf(
                "dataVisita" to visitaModificar.dataVisita,
                "motiu" to visitaModificar.motiu
            ))
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Modificació de la vista OK")
                visitesRepositoryInterface.modificarVisitaOK()
            }
            .addOnFailureListener{e ->
                Log.d(ContentValues.TAG, "Error modificació de la visita", e)
                visitesRepositoryInterface.modificarVisitaNOK()
            }
    }

    /** Funció que elimina una visita existent **/
    fun eliminarVisita(idVisita: String, visitesRepositoryInterface : VisitesRepositoryInterface){
        db.collection(DB_ROOT_USUARIS + "/" + firebaseAuth.currentUser?.uid + "/" + Constants.DB_ROOT_VISITES).document(idVisita)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully deleted!")
                visitesRepositoryInterface.eliminarVisitaOK()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting document", e)
                visitesRepositoryInterface.eliminarVisitaNOK()
            }
    }


}