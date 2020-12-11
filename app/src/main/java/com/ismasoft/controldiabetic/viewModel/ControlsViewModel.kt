package com.ismasoft.controldiabetic.viewModel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.data.model.Control
import com.ismasoft.controldiabetic.data.model.ControlAmbId
import com.ismasoft.controldiabetic.data.repository.*
import com.ismasoft.controldiabetic.utilities.getDataSenseHora
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.util.*
import java.util.Calendar.DATE
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.time.ExperimentalTime
import kotlin.time.days

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

    // VALORS A RETORNAR A LA VISTA GENERAL DE CONTROLS
    private val _ultimControlGlucosa = MutableLiveData<String>()
    val ultimControlGlucosa : LiveData<String> get() = _ultimControlGlucosa
    private val _ultimControlInsulina = MutableLiveData<String>()
    val ultimControlInsulina : LiveData<String> get() = _ultimControlInsulina
    private val _mitjanaAhirGlucosa = MutableLiveData<String>()
    val mitjanaAhirGlucosa : LiveData<String> get() = _mitjanaAhirGlucosa
    private val _mitjanaSetmanaGlucosa = MutableLiveData<String>()
    val mitjanaSetmanaGlucosa : LiveData<String> get() = _mitjanaSetmanaGlucosa
    private val _mitjanaMesGlucosa = MutableLiveData<String>()
    val mitjanaMesGlucosa : LiveData<String> get() = _mitjanaMesGlucosa

    private val _ambControls = MutableLiveData<Boolean>(false)
    val ambControls : LiveData<Boolean> get() = _ambControls

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

    fun getDaysAgo(daysAgo: Int): Calendar {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.HOUR_OF_DAY,0)
        calendar.set(Calendar.MINUTE,0)
        calendar.set(Calendar.SECOND,0)
        return calendar
    }

    override fun llistaControlsOK(llistaControls: ArrayList<ControlAmbId>) {

        // Ultim valor de glucosa
        var ultimaGlucosa : Int = 0
        // Ultim valor de insulina
        var ultimaInsulina : Int = 0
        // Glucosa i insulina ultim dia
        var controlsAvui: ArrayList<ControlAmbId> = ArrayList()
        // Glucosa i insulina ultim dia
        var controlsAhir: ArrayList<ControlAmbId> = ArrayList()
        var mitjaAhirGlucosa : Int = 0
        // Glucosa i insulina ultima setmana
        var controlsSetmana: ArrayList<ControlAmbId> = ArrayList()
        var mitjaSetmanaGlucosa : Int = 0
        // Glucosa i insulina ultim mes
        var controlsMes: ArrayList<ControlAmbId> = ArrayList()
        var mitjaMesGlucosa : Int = 0

        var dataActual = getDaysAgo(0)
        var dataAhir = getDaysAgo(1)
        var dataSetmana = getDaysAgo(7)
        var dataMes = getDaysAgo(30)

        /** Separem els controls amb llistes d'avui, d'ahir, de la setmana i del mes **/
        for(control in llistaControls)
        {
            var dataControl = getDataSenseHora(control.dataControl!!)
            if(dataControl.time.after(dataActual.time)){
                controlsAvui.add(control)
            }
            if(dataControl.time.after(dataAhir.time)){
                controlsAhir.add(control)
            }
            if(dataControl.time.after(dataSetmana.time)){
                controlsSetmana.add(control)
            }
            if(dataControl.time.after(dataMes.time)){
                controlsMes.add(control)
            }
        }

        /** OBTENIM L'ULTIM CONTROL DE GLUCOSA I INSULINA**/
        var controlUltimGlucosa : ControlAmbId? = null
        var controlUltimInsulina: ControlAmbId? = null
        if(controlsAvui.size>0){
            for(control in controlsAvui){
                if (controlUltimGlucosa == null || controlUltimGlucosa.dataControl!! < control.dataControl){
                    controlUltimGlucosa = control
                }
                if ((controlUltimInsulina == null || controlUltimInsulina.dataControl!! < control.dataControl) && control.valorInsulina != 0){
                    controlUltimInsulina = control
                }
            }
        }
        else if(controlsAhir.size>0){
            for(control in controlsAhir){
                if (controlUltimGlucosa == null || controlUltimGlucosa.dataControl!! < control.dataControl){
                    controlUltimGlucosa = control
                }
                if ((controlUltimInsulina == null || controlUltimInsulina?.dataControl!! < control.dataControl) && control.valorInsulina != 0){
                    controlUltimInsulina = control
                }
            }
        }
        else if(controlsSetmana.size>0){
            for(control in controlsSetmana){
                if (controlUltimGlucosa == null || controlUltimGlucosa.dataControl!! < control.dataControl){
                    controlUltimGlucosa = control
                }
                if ((controlUltimInsulina == null || controlUltimInsulina.dataControl!! < control.dataControl) && control.valorInsulina != 0){
                    controlUltimInsulina = control
                }
            }
        }
        else if(controlsMes.size>0){
            for(control in controlsMes){
                if (controlUltimGlucosa == null || controlUltimGlucosa.dataControl!! < control.dataControl){
                    controlUltimGlucosa = control
                }
                if ((controlUltimInsulina == null || controlUltimInsulina.dataControl!! < control.dataControl) && control.valorInsulina != 0){
                    controlUltimInsulina = control
                }
            }
        }

        // Busquem el valor de l'insulina fins a l'última setmana anterior en cas que no s'hagi trobat en el dia
        if(controlUltimInsulina?.valorInsulina==null){
            for(control in controlsSetmana){
                if ((controlUltimInsulina == null || controlUltimInsulina.dataControl!! < control.dataControl) && control.valorInsulina != 0){
                    controlUltimInsulina = control
                }
            }
        }

        if(controlUltimGlucosa?.valorGlucosa!=null){
            ultimaGlucosa = controlUltimGlucosa.valorGlucosa.toString().toInt()
        }else{
            ultimaGlucosa=0
        }
        if(controlUltimInsulina?.valorInsulina!=null){
            ultimaInsulina = controlUltimInsulina.valorInsulina.toString().toInt()
        }else{
            ultimaInsulina=0
        }

        /** OBTENIM LA MITJANA DE LA GLUCOSA D'AHIR **/
        var contadorControls = 0
        var sumaValorsGlucosa = 0
        for(control in controlsAhir){
            sumaValorsGlucosa += control.valorGlucosa?.toInt()!!
            contadorControls++
        }
        if(contadorControls != 0) {
            mitjaAhirGlucosa = sumaValorsGlucosa / contadorControls
        }else{
            mitjaAhirGlucosa = 0
        }

        /** OBTENIM LA MITJANA DE LA GLUCOSA DE LA SETMANA **/
        contadorControls = 0
        sumaValorsGlucosa = 0
        for(control in controlsSetmana){
            sumaValorsGlucosa += control.valorGlucosa?.toInt()!!
            contadorControls++
        }
        if(contadorControls != 0) {
            mitjaSetmanaGlucosa = sumaValorsGlucosa / contadorControls
        }else{
            mitjaSetmanaGlucosa = 0
        }

        /** OBTENIM LA MITJANA DE LA GLUCOSA DELS ultims 30 dies **/
        contadorControls = 0
        sumaValorsGlucosa = 0
        for(control in controlsMes){
            sumaValorsGlucosa += control.valorGlucosa?.toInt()!!
            contadorControls++
        }
        if(contadorControls != 0) {
            mitjaMesGlucosa = sumaValorsGlucosa / contadorControls
        }else{
            mitjaMesGlucosa = 0
        }

        _mitjanaAhirGlucosa.value = "${mitjaAhirGlucosa} mg/dl"
        _mitjanaSetmanaGlucosa.value = "${mitjaSetmanaGlucosa} mg/dl"
        _mitjanaMesGlucosa.value = "${mitjaMesGlucosa} mg/dl"
        _ultimControlGlucosa.value= "${ultimaGlucosa} mg/dl"
        _ultimControlInsulina.value = "${ultimaInsulina} ui"
        controlActivityInstance.llistaControlsOK(llistaControls)
    }

    override fun LlistaControlsNOK() {
        controlActivityInstance.LlistaControlsNOK()
    }

    private fun validacioDeRangDeGlucosa(document: DocumentSnapshot) {
        _rangTotalOK.value = true
        _rangParcialOk.value = true
        _rangMissatge.value = "El control de glucosa està dins els paràmetres establerts"
        // Si el control es despres de l'apat
        if(controlTractat.esDespresDeApat == true){
            val valorMaxGlucosaDespresApat = document.data?.get("glucosaAltaDespresApat")
            val valorMinGlucosaDespresApat = document.data?.get("glucosaBaixaDespresApat")
            if(valorMaxGlucosaDespresApat.toString().toInt() < controlTractat.valorGlucosa.toString().toInt()){
                _rangTotalOK.value = false
                _rangMissatge.value = "El control de glucosa sobrepassa el límit màxim establert"
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
                _rangMissatge.value = "El control de glucosa sobrepasa el límit màxim establert"
            }
            else if(valorMinGlucosa.toString().toInt() > controlTractat.valorGlucosa.toString().toInt()){
                _rangTotalOK.value = false
                _rangMissatge.value = "El control de glucosa no arriba al límit mínim establert"
            }
            else if(valorAltaGlucosa.toString().toInt() < controlTractat.valorGlucosa.toString().toInt()){
                _rangMissatge.value = "El control de glucosa sobrepassa el límit de glucosa Alta"
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