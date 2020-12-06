package com.ismasoft.controldiabetic.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.data.repository.*
import com.ismasoft.controldiabetic.utilities.convertirADateLaDataFirebase

class PerfilViewModel(application: Application) : AndroidViewModel(application) , PerfilRepositoryInterface{

    // Definim el repository per accedir a la BBDD
    private var repository = PerfilRepository(application)
    lateinit var perfilActivityInstance : PerfilRepositoryInterface

    // VALORS A RETORNAR A LA VISTA DEL PERFIL
    private val _valueNom = MutableLiveData<String>()
    val valueNom : LiveData<String> get() = _valueNom
    private val _valueCognom = MutableLiveData<String>()
    val valueCognom : LiveData<String> get() = _valueCognom
    private val _valueCognom2 = MutableLiveData<String>()
    val valueCognom2 : LiveData<String> get() = _valueCognom2
    private val _dataNaixament = MutableLiveData<String>()
    val dataNaixament : LiveData<String> get() = _dataNaixament
    private val _genere = MutableLiveData<String>()
    val genere : LiveData<String> get() = _genere
    private val _pes = MutableLiveData<String>()
    val pes : LiveData<String> get() = _pes
    private val _altura = MutableLiveData<String>()
    val altura : LiveData<String> get() = _altura
    private val _correuElectronic = MutableLiveData<String>()
    val correuElectronic : LiveData<String> get() = _correuElectronic

    fun recuperarDadesUsuari(perfilRepositoryInterface: PerfilRepositoryInterface) {
        perfilActivityInstance = perfilRepositoryInterface
        repository.recuperarDadesUsuari(this)
    }

    override fun recuperarDadesPersonalsOK(document: DocumentSnapshot?) {
        _valueNom.value = document?.get("nom").toString()
        _valueCognom.value = document?.get("primerCognom").toString()
        _valueCognom2.value = document?.get("segonCognom").toString()
        _dataNaixament.value = convertirADateLaDataFirebase(document?.data?.get("dataNaixement") as Timestamp).toString()
        _genere.value = document?.get("genere").toString()
        _pes.value = document?.get("pes").toString() + " Kg"
        _altura.value = document?.get("altura").toString() + " Cm"
        _correuElectronic.value = document?.get("correuElectronic").toString()
    }



    override fun recuperarDadesPersonalsNOK() {
        TODO("Not yet implemented")
    }


    override fun modificarDadesPersOK() {
        TODO("Not yet implemented")
    }

    override fun modificarDadesPersNOK() {
        TODO("Not yet implemented")
    }


//    lateinit var controlActivityInstance : ControlsRepositoryInterface



}