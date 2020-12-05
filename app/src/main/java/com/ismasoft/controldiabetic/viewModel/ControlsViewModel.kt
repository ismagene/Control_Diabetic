package com.ismasoft.controldiabetic.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.data.model.Control
import com.ismasoft.controldiabetic.data.repository.*

class ControlsViewModel(application: Application) : AndroidViewModel(application) , ControlsRepositoryInterface{

    // Definim el repository per accedir a la BBDD
    private var repository = ControlsRepository(application)
    lateinit var controlActivityInstance : ControlsRepositoryInterface

    /* Variables que recuperem directament des de la l'activity */
    private val _rangTotalOK = MutableLiveData<Boolean>()
    val rangTotalOk : LiveData<Boolean> get() = _rangTotalOK
    private val _rangParcialOk = MutableLiveData<Boolean>()
    val rangParcialOk : LiveData<Boolean> get() = _rangParcialOk
    private val _rangMissatge = MutableLiveData<String>()
    val rangMissatge : LiveData<String> get() = _rangMissatge

    private val _llistaControls = MutableLiveData<List<Control>>()
    val llistaControls : LiveData<List<Control>> get() = _llistaControls

    private val _ultimControlGlucosa = MutableLiveData<String>()
    val ultimControlGlucosa : LiveData<String> get() = _ultimControlGlucosa

    private lateinit var controlTractat : Control

    fun onButtonGuardarControl(control: Control, controlsRepositoryInterface : ControlsRepositoryInterface){
        controlTractat = control
        controlActivityInstance = controlsRepositoryInterface
        repository.insertarControlBBDD(control,this)
    }

    fun recuperarLlistaControls(controlsRepositoryInterface : ControlsRepositoryInterface){
        controlActivityInstance = controlsRepositoryInterface
        repository.recuperarLlistaControls(this)
    }

    override fun afegirControlOK() {
        repository.obtenirRangsGlucosa(this)
    }
    override fun afegirControlNOK() {
        controlActivityInstance.afegirControlNOK()
    }
    override fun obtenirRangsOK(document: DocumentSnapshot) {
        validacioDeRangDeGlucosa(document)
        controlActivityInstance.afegirControlOK()

    }
    override fun obtenirRangsNOK() {
        controlActivityInstance.afegirControlNOK()
    }
    override fun llistaControlsOK(llistaControls: List<Control>) {

        for(document in llistaControls)
        {

        }

//        binding.ultimControlGlucosa.setText(llistaControls.get(0).get("valorGlucosa").toString())

        _ultimControlGlucosa.value=llistaControls.get(0).valorGlucosa.toString()+" mg/dl"
        controlActivityInstance.llistaControlsOK(llistaControls)
    }

    override fun LlistaControlsNOK() {
        TODO("Not yet implemented")
    }

    private fun validacioDeRangDeGlucosa(document: DocumentSnapshot) {
        _rangTotalOK.value = true
        _rangParcialOk.value = true
        _rangMissatge.value = "El control de glucosa esta dins els parametres establerts"
        // Si el control es despres de l'apat
        if(controlTractat.esDespresDeApat == true){
            val valorMaxGlucosaDespresApat = document.data?.get("glucosaAltaDespresApat")
            val valorMinGlucosaDespresApat = document.data?.get("glucosaBaixaDespresApat")
            if(valorMaxGlucosaDespresApat.toString().toInt() < controlTractat.valorGlucosa.toString().toInt()){
                _rangTotalOK.value = false
                _rangMissatge.value = "El control de glucosa sobrepasa el límit maxim establert"
            }
            else if(valorMinGlucosaDespresApat.toString().toInt() > controlTractat.valorGlucosa.toString().toInt()){
                _rangTotalOK.value = false
                _rangMissatge.value = "El control de glucosa no arriba al límit mínim establert"
            }
        }
        // Si el control no es despres d'un apat:
        else{
            val valorMaxGlucosa = document.data?.get("glucosaMoltAlta")
            val valorMinGlucosa = document.data?.get("glucosaMoltBaixa")
            val valorAltaGlucosa = document.data?.get("glucosaAlta")
            val valorBaixaGlucosa = document.data?.get("glucosaBaixa")

            if(valorMaxGlucosa.toString().toInt() < controlTractat.valorGlucosa.toString().toInt()){
                _rangTotalOK.value = false
                _rangMissatge.value = "El control de glucosa sobrepasa el límit maxim establert"
            }
            else if(valorMinGlucosa.toString().toInt() > controlTractat.valorGlucosa.toString().toInt()){
                _rangTotalOK.value = false
                _rangMissatge.value = "El control de glucosa no arriba al límit mínim establert"
            }
            else if(valorAltaGlucosa.toString().toInt() < controlTractat.valorGlucosa.toString().toInt()){
                _rangMissatge.value = "El control de glucosa sobrepasa el límit de glucosa Alta"
                _rangParcialOk.value = false
            }
            else if(valorBaixaGlucosa.toString().toInt() > controlTractat.valorGlucosa.toString().toInt()){
                _rangMissatge.value = "El control de glucosa no arriba al límit mínim de glucosa Baixa"
                _rangParcialOk.value = false
            }
        }
    }

    override fun modificarControlOK() {}
    override fun modificarControlNOK() {}
    override fun eliminarControlOK() {}
    override fun eliminarControlNOK() {}

}