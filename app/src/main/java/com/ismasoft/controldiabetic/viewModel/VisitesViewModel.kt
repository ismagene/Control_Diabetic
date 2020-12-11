package com.ismasoft.controldiabetic.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.ismasoft.controldiabetic.data.model.Visita
import com.ismasoft.controldiabetic.data.model.VisitaAmbId
import com.ismasoft.controldiabetic.data.repository.*
import com.ismasoft.controldiabetic.ui.adapters.VisitesListAdapter
import java.util.*
import kotlin.collections.ArrayList

class VisitesViewModel(application: Application) : AndroidViewModel(application) , VisitesRepositoryInterface{

    // Definim el repository per accedir a la BBDD
    private var repository = VisitesRepository(application)
    lateinit var visitalActivityInstance : VisitesRepositoryInterface

    private var _ambVisites = MutableLiveData<Boolean>(false)
    val ambVisites : LiveData<Boolean> get() = _ambVisites
    private var _ambProximaVisita = MutableLiveData<Boolean>(false)
    var ambProximaVisita : MutableLiveData<Boolean> = _ambProximaVisita
    private var _senseVisites = MutableLiveData<Boolean>(false)
    val senseVisites : LiveData<Boolean> get() = _senseVisites

    private val _visitaVigent : MutableLiveData<VisitaAmbId> = MutableLiveData<VisitaAmbId>()
    val visitaVigent : LiveData<VisitaAmbId> = _visitaVigent

    private lateinit var visitaAfegir : Visita

    fun onButtonGuardarVisita(visita : Visita, visitesRepositoryInterface : VisitesRepositoryInterface){
        visitalActivityInstance = visitesRepositoryInterface
        visitaAfegir = visita
        repository.recuperarUltimaVisita(this)

    }

    fun onButtoModificarVisita(visita : VisitaAmbId, visitesRepositoryInterface : VisitesRepositoryInterface){
        visitalActivityInstance = visitesRepositoryInterface
        repository.modificarVisita(visita,this)

    }

    fun recuperarLlistaVisites(visitesRepositoryInterface : VisitesRepositoryInterface){
        visitalActivityInstance = visitesRepositoryInterface
        repository.recuperarLlistaVisites(this)
    }

    fun eliminarVisita(idVisita: String, visitesRepositoryInterface : VisitesRepositoryInterface){
        visitalActivityInstance = visitesRepositoryInterface
        repository.eliminarVisita(idVisita,this)
    }

    override fun afegirVisitaOK() {
        visitalActivityInstance.afegirVisitaOK()
    }
    override fun afegirVisitaNOK() {
        visitalActivityInstance.afegirVisitaNOK()
    }
    override fun existeixVisitaVigent() {
        visitalActivityInstance.existeixVisitaVigent()
    }

    override fun errorAlConsultarVisitaVigent() {
        visitalActivityInstance.errorAlConsultarVisitaVigent()
    }

    override fun noExisteixVisitaVigent() {
        repository.guardarVisita(visitaAfegir, this)
    }

    override fun llistaVisitesOK(llistaVisites: ArrayList<VisitaAmbId>) {
        if(llistaVisites.size>0){
            _ambVisites.value = true
            _senseVisites.value = false
        }else{
            _ambVisites.value = false
            _senseVisites.value = true
        }

        for(visita in llistaVisites)
        {
            if(visita.dataVisita!!.after(Date())){
                llistaVisites.remove(visita)
                _visitaVigent.value = visita
                _ambProximaVisita.value = true
                break;
            }
        }

        visitalActivityInstance.llistaVisitesOK(llistaVisites)
    }
    override fun llistaVisitesNOK() {
        visitalActivityInstance.llistaVisitesNOK()
    }
    override fun modificarVisitaOK() {
        visitalActivityInstance.modificarVisitaOK()
    }
    override fun modificarVisitaNOK() {
        visitalActivityInstance.modificarVisitaNOK()
    }
    override fun eliminarVisitaOK() {
        visitalActivityInstance.eliminarVisitaOK()
    }
    override fun eliminarVisitaNOK() {
        visitalActivityInstance.eliminarVisitaNOK()
    }

}