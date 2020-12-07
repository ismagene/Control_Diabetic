package com.ismasoft.controldiabetic.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.data.repository.*
import com.ismasoft.controldiabetic.utilities.convertirADateLaDataFirebase
import java.text.SimpleDateFormat

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
        var dataFirebase = convertirADateLaDataFirebase(document?.data?.get("dataNaixement") as Timestamp)
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        var dataString = sdf.format(dataFirebase)
        _dataNaixament.value = dataString
        _genere.value = document?.get("genere").toString()
        _pes.value = document?.get("pes").toString() + " Kg"
        _altura.value = document?.get("altura").toString() + " Cm"
        _correuElectronic.value = document?.get("correuElectronic").toString()
    }

    fun validarContrasenya(contrasenyaAntiga:String, perfilRepositoryInterface: PerfilRepositoryInterface) {
        perfilActivityInstance = perfilRepositoryInterface
        repository.validarContrasenya(contrasenyaAntiga,this)
    }

    fun modificarContrasenya(contrasenya: String, perfilRepositoryInterface: PerfilRepositoryInterface) {
        perfilActivityInstance = perfilRepositoryInterface
        repository.modificarContrasenya(contrasenya,this)
    }

    fun tancarSessio(){
        repository.tancarSessio()
    }

    override fun recuperarDadesPersonalsNOK() {}
    override fun modificarDadesPersOK() {}
    override fun modificarDadesPersNOK() {}
    override fun validarContrasenyaOK() {
        perfilActivityInstance.validarContrasenyaOK()
    }
    override fun validarContrasenyaNOK() {
        perfilActivityInstance.validarContrasenyaNOK()
    }
    override fun modificarContrasenyaOK() {
        perfilActivityInstance.modificarContrasenyaOK()
    }
    override fun modificarContrasenyaNOK() {
        perfilActivityInstance.modificarContrasenyaNOK()
    }

}