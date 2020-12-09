package com.ismasoft.controldiabetic.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.data.model.Alarma
import com.ismasoft.controldiabetic.data.model.Control
import com.ismasoft.controldiabetic.data.model.Visita
import com.ismasoft.controldiabetic.data.model.VisitaAmbId
import com.ismasoft.controldiabetic.data.repository.*
import java.util.*
import kotlin.collections.ArrayList

class VisitesViewModel(application: Application) : AndroidViewModel(application) , VisitesRepositoryInterface{

    // Definim el repository per accedir a la BBDD
    private var repository = VisitesRepository(application)
    lateinit var visitalActivityInstance : VisitesRepositoryInterface

    private lateinit var visitaTractada : Alarma

    fun onButtonGuardarVisita(visita : Visita, visitesRepositoryInterface : VisitesRepositoryInterface){
        visitalActivityInstance = visitesRepositoryInterface
        repository.guardarVisita(visita , this)
    }

    override fun afegirVisitaOK() {}
    override fun afegirVisitaNOK() {}
    override fun existeixVisitaVigent() {
        visitalActivityInstance.existeixVisitaVigent()
    }

    override fun noExisteixVisitaVigent() {
        visitalActivityInstance.noExisteixVisitaVigent()
    }

    override fun llistaVisitesOK(document: ArrayList<VisitaAmbId>) {
        visitalActivityInstance.llistaVisitesOK(document)
    }
    override fun llistaVisitesNOK() {
        visitalActivityInstance.llistaVisitesNOK()
    }
    override fun modificarVisitaOK() {}
    override fun modificarVisitaNOK() {}
    override fun eliminarVisitaOK() {}
    override fun eliminarVisitaNOK() {}

}