package com.ismasoft.controldiabetic.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.data.model.Control
import com.ismasoft.controldiabetic.data.model.Visita
import com.ismasoft.controldiabetic.data.model.VisitaAmbId

interface VisitesRepositoryInterface {

    fun afegirVisitaOK()
    fun afegirVisitaNOK()

    fun existeixVisitaVigent()
    fun errorAlConsultarVisitaVigent()
    fun noExisteixVisitaVigent()

    fun llistaVisitesOK(document: ArrayList<VisitaAmbId>)
    fun llistaVisitesNOK()

    fun modificarVisitaOK()
    fun modificarVisitaNOK()
    fun eliminarVisitaOK()
    fun eliminarVisitaNOK()

}