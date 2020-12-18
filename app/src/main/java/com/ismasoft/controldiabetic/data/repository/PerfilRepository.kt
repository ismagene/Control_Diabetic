package com.ismasoft.controldiabetic.data.repository

import android.app.Application
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ismasoft.controldiabetic.data.model.User
import com.ismasoft.controldiabetic.data.repository.interfaces.PerfilRepositoryInterface
import com.ismasoft.controldiabetic.utilities.Constants.DB_ROOT_USUARIS

class PerfilRepository(val application: Application) {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    /** Funció que recupera les dades de l'usuari loguejat **/
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

    /** Funció que recupera les dades de l'usuari loguejat per tractar les dades mèdiques**/
    fun recuperarDadesMediques(perfilRepositoryInterface: PerfilRepositoryInterface) {

        db.collection(DB_ROOT_USUARIS).document(firebaseAuth.currentUser?.uid.toString())
            .get()
            .addOnCompleteListener() { task  ->
                if(task .isSuccessful){
                    Log.d(TAG, "DocumentSnapshot recuperat with ID: ${task.result?.id}")
                    perfilRepositoryInterface.recuperarDadesMediquesOK(task.result)
                }
                else{
                    Log.w(TAG, "Error recuperant l'usuari")
                    perfilRepositoryInterface.recuperarDadesMediquesNOK()
                }
            }

    }

    /** Funció que comproba que la contrasenya introduida de l'usuari loguejat és correcta **/
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

    /** Funció que modifica la contrasenya de Firebase de l'usuari **/
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

    /** Funció que tanca la sessió de l'usuari **/
    fun tancarSessio(){
        firebaseAuth.signOut()
    }

    /** Funció que modifica les dades personals de l'usuari **/
    fun modificarDadesPers(usuariModificat: User, perfilRepositoryInterface: PerfilRepositoryInterface) {
        val user= FirebaseAuth.getInstance().currentUser
        db.collection(DB_ROOT_USUARIS).document(user?.uid.toString())
            .update(mapOf(
            "nom" to usuariModificat.nom,
            "primerCognom" to usuariModificat.primerCognom,
            "segonCognom" to usuariModificat.segonCognom,
            "dataDiagnosi" to usuariModificat.dataNaixement,
            "genere" to usuariModificat.genere,
            "pes" to usuariModificat.pes,
            "altura" to usuariModificat.altura
            ))
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Modificació de l'usuari OK")
                perfilRepositoryInterface.modificarDadesPersOK()
            }
            .addOnFailureListener{e ->
                Log.d(ContentValues.TAG, "Error modificació de l'usuari", e)
                perfilRepositoryInterface.modificarDadesPersNOK()
            }

    }

    /** Funció que modifica les dades mèdiques de l'usuari **/
    fun modificarDadesMed(usuariModificat: User, perfilRepositoryInterface: PerfilRepositoryInterface) {
        val user= FirebaseAuth.getInstance().currentUser
        db.collection(DB_ROOT_USUARIS).document(user?.uid.toString())
            .update(mapOf(
                "centre" to usuariModificat.centre,
                "poblacioCentre" to usuariModificat.poblacioCentre,
                "nomMetge" to usuariModificat.nomMetge,
                "correuElectronicMetge" to usuariModificat.correuElectronicMetge,
                "tipusDiabetis" to usuariModificat.tipusDiabetis,
                "dataDiagnosi" to usuariModificat.dataDiagnosi,
                "glucosaBaixa" to usuariModificat.glucosaBaixa,
                "glucosaAlta" to usuariModificat.glucosaAlta,
                "glucosaMoltBaixa" to usuariModificat.glucosaMoltBaixa,
                "glucosaMoltAlta" to usuariModificat.glucosaMoltAlta,
                "glucosaBaixaDespresApat" to usuariModificat.glucosaBaixaDespresApat,
                "glucosaAltaDespresApat" to usuariModificat.glucosaAltaDespresApat
            ))
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Modificació de l'usuari OK")
                perfilRepositoryInterface.modificarDadesMedOK()
            }
            .addOnFailureListener{e ->
                Log.d(ContentValues.TAG, "Error modificació de l'usuari", e)
                perfilRepositoryInterface.modificarDadesMedNOK()
            }

    }
}