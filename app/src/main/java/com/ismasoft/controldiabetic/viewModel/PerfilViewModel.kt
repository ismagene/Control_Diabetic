package com.ismasoft.controldiabetic.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.data.model.User
import com.ismasoft.controldiabetic.data.repository.*
import com.ismasoft.controldiabetic.data.repository.interfaces.PerfilRepositoryInterface
import com.ismasoft.controldiabetic.utilities.convertirADateLaDataFirebase
import java.text.SimpleDateFormat

class PerfilViewModel(application: Application) : AndroidViewModel(application) ,
    PerfilRepositoryInterface {

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

    private val _valueNomDelCentre = MutableLiveData<String>()
    val valueNomDelCentre : LiveData<String> get() = _valueNomDelCentre
    private val _poblacioDelCentre = MutableLiveData<String>()
    val poblacioDelCentre : LiveData<String> get() = _poblacioDelCentre
    private val _nomDelMetge = MutableLiveData<String>()
    val nomDelMetge : LiveData<String> get() = _nomDelMetge
    private val _correuElectronicMetge = MutableLiveData<String>()
    val correuElectronicMetge : LiveData<String> get() = _correuElectronicMetge
    private val _tipusDiabetis = MutableLiveData<String>()
    val tipusDiabetis : LiveData<String> get() = _tipusDiabetis
    private val _dataDiagnosi = MutableLiveData<String>()
    val dataDiagnosi : LiveData<String> get() = _dataDiagnosi
    private val _glucosaBaixa = MutableLiveData<String>()
    val glucosaBaixa : LiveData<String> get() = _glucosaBaixa
    private val _glucosaAlta = MutableLiveData<String>()
    val glucosaAlta : LiveData<String> get() = _glucosaAlta
    private val _glucosaMoltBaixa = MutableLiveData<String>()
    val glucosaMoltBaixa : LiveData<String> get() = _glucosaMoltBaixa
    private val _glucosaMoltAlta = MutableLiveData<String>()
    val glucosaMoltAlta : LiveData<String> get() = _glucosaMoltAlta
    private val _glucosaBaixaDA = MutableLiveData<String>()
    val glucosaBaixaDA : LiveData<String> get() = _glucosaBaixaDA
    private val _glucosaAltaDA = MutableLiveData<String>()
    val glucosaAltaDA : LiveData<String> get() = _glucosaAltaDA

    fun recuperarDadesUsuari(perfilRepositoryInterface: PerfilRepositoryInterface) {
        perfilActivityInstance = perfilRepositoryInterface
        repository.recuperarDadesUsuari(this)
    }

    fun mostrarDadesPersonals(user : User){
        _valueNom.value = user.nom
        _valueCognom.value = user.primerCognom
        _valueCognom2.value = user.segonCognom
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val dataString = sdf.format(user.dataNaixement)
        _dataNaixament.value = dataString
        _genere.value = user.genere
        _pes.value = user.pes.toString()
        _altura.value = user.altura.toString()
        _correuElectronic.value = user.correuElectronic
    }

    fun mostrarDadesMediques(user : User){
        _valueNomDelCentre.value  = user.centre
        _poblacioDelCentre.value = user.poblacioCentre
        _nomDelMetge.value = user.nomMetge
        _correuElectronicMetge.value = user.correuElectronicMetge
        _tipusDiabetis.value = user.tipusDiabetis
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val dataString = sdf.format(user.dataDiagnosi)
        _dataDiagnosi.value = dataString
        _glucosaBaixa.value = user.glucosaBaixa.toString()
        _glucosaAlta.value = user.glucosaAlta.toString()
        _glucosaMoltBaixa.value = user.glucosaMoltBaixa.toString()
        _glucosaMoltAlta.value = user.glucosaMoltAlta.toString()
        _glucosaBaixaDA.value = user.glucosaBaixaDespresApat.toString()
        _glucosaAltaDA.value = user.glucosaAltaDespresApat.toString()
    }

    fun recuperarDadesMediques(perfilRepositoryInterface: PerfilRepositoryInterface) {
        perfilActivityInstance = perfilRepositoryInterface
        repository.recuperarDadesMediques(this)
    }

    fun modificarDadesPers(user: User, perfilRepositoryInterface: PerfilRepositoryInterface) {
        perfilActivityInstance = perfilRepositoryInterface
        repository.modificarDadesPers(user, this)
    }

    fun modificarDadesMed(user: User, perfilRepositoryInterface: PerfilRepositoryInterface) {
        perfilActivityInstance = perfilRepositoryInterface
        repository.modificarDadesMed(user, this)
    }

    override fun recuperarDadesPersonalsOK(document: DocumentSnapshot?) {
        _valueNom.value = document?.get("nom").toString()
        _valueCognom.value = document?.get("primerCognom").toString()
        _valueCognom2.value = document?.get("segonCognom").toString()
        val dataFirebase = convertirADateLaDataFirebase(document?.data?.get("dataNaixement") as Timestamp)
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val dataString = sdf.format(dataFirebase)
        _dataNaixament.value = dataString
        _genere.value = document.get("genere").toString()
        _pes.value = document.get("pes").toString() + " Kg"
        _altura.value = document.get("altura").toString() + " Cm"
        _correuElectronic.value = document.get("correuElectronic").toString()
    }

    override fun recuperarDadesMediquesOK(document: DocumentSnapshot?) {
        _valueNomDelCentre.value = document?.get("centre").toString()
        _poblacioDelCentre.value = document?.get("poblacioCentre").toString()
        _nomDelMetge.value = document?.get("nomMetge").toString()
        _correuElectronicMetge.value = document?.get("correuElectronicMetge").toString()
        _tipusDiabetis.value = document?.get("tipusDiabetis").toString()
        val dataFirebase = convertirADateLaDataFirebase(document?.data?.get("dataDiagnosi") as Timestamp)
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val dataString = sdf.format(dataFirebase)
        _dataDiagnosi.value = dataString
        _glucosaBaixa.value = document.get("glucosaBaixa").toString() + " mg/dL"
        _glucosaAlta.value = document.get("glucosaAlta").toString() + " mg/dL"
        _glucosaMoltBaixa.value = document.get("glucosaMoltBaixa").toString() + " mg/dL"
        _glucosaMoltAlta.value = document.get("glucosaMoltAlta").toString() + " mg/dL"
        _glucosaBaixaDA.value = document.get("glucosaBaixaDespresApat").toString() + " mg/dL"
        _glucosaAltaDA.value = document.get("glucosaAltaDespresApat").toString() + " mg/dL"
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

    override fun recuperarDadesPersonalsNOK() {
        perfilActivityInstance.recuperarDadesPersonalsNOK()
    }
    override fun recuperarDadesMediquesNOK() {
        perfilActivityInstance.recuperarDadesMediquesNOK()
    }
    override fun modificarDadesPersOK() {
        perfilActivityInstance.modificarDadesPersOK()
    }
    override fun modificarDadesPersNOK() {
        perfilActivityInstance.modificarDadesPersNOK()
    }
    override fun modificarDadesMedOK() {
        perfilActivityInstance.modificarDadesMedOK()
    }
    override fun modificarDadesMedNOK() {
        perfilActivityInstance.modificarDadesMedNOK()
    }

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