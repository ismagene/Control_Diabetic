package com.ismasoft.controldiabetic.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.ismasoft.controldiabetic.data.model.Alarma
import com.ismasoft.controldiabetic.data.model.AlarmaAmbId
import com.ismasoft.controldiabetic.data.repository.*
import com.ismasoft.controldiabetic.ui.adapters.AlarmesListAdapterInterface
import kotlin.collections.ArrayList

class AlarmesViewModel(application: Application) : AndroidViewModel(application), AlarmesRepositoryInterface,
    AlarmesListAdapterInterface {

    // Definim el repository per accedir a la BBDD
    private var repository = AlarmesRepository(application)
    lateinit var alarmaActivityInstance : AlarmesRepositoryInterface
    lateinit var alarmaListAdapterInstance : AlarmesListAdapterInterface

    private val _ambAlarmes = MutableLiveData<Boolean>(false)
    val ambAlarmes : LiveData<Boolean> get() = _ambAlarmes
    private val _senseAlarmes = MutableLiveData<Boolean>(false)
    val senseAlarmes : LiveData<Boolean> get() = _senseAlarmes

    private var alarmaAfegir = Alarma()
    private var alarmaModificar = AlarmaAmbId()

    fun onButtonGuardarAlarma(alarma: Alarma, alarmesRepositoryInterface : AlarmesRepositoryInterface){
        alarmaActivityInstance = alarmesRepositoryInterface
        alarmaAfegir = alarma
        repository.comprobarExisteixAlarma(alarma.horaAlarma.toString(), this)
    }

    fun onButtonModificarAlarma(alarma: AlarmaAmbId, alarmesRepositoryInterface : AlarmesRepositoryInterface){
        alarmaActivityInstance = alarmesRepositoryInterface
        alarmaModificar = alarma
        repository.comprobarExisteixAlarma(alarma.horaAlarma.toString(), this)
    }

    fun recuperarLlistaAlarmes(alarmesRepositoryInterface : AlarmesRepositoryInterface){
        alarmaActivityInstance = alarmesRepositoryInterface
        repository.recuperarLlistaAlarmes(this)
    }

    fun eliminarAlarma(idAlarma: String, position: Int, alarmesListAdapterInterface : AlarmesListAdapterInterface){
        alarmaListAdapterInstance = alarmesListAdapterInterface
        repository.eliminarAlarma(idAlarma, position, this)
    }

    override fun afegirAlarmaOK(idAlarmaManager: Int?) {
        alarmaActivityInstance.afegirAlarmaOK(idAlarmaManager)
    }
    override fun afegirAlarmaNOK() {
        alarmaActivityInstance.afegirAlarmaNOK()
    }

    override fun jaExisteixAlarma() {
        alarmaActivityInstance.jaExisteixAlarma()
    }
    override fun noExisteixAlarma() {
        if(alarmaAfegir.horaAlarma != null){
            repository.recuperarIdAlarmaNova(this)
        }else if(alarmaModificar.idAlarma != null){
            repository.modificarAlarma(alarmaModificar,this)
        }

    }
    override fun modificarAlarmaOK() {
        alarmaActivityInstance.modificarAlarmaOK()
    }
    override fun modificarAlarmaNOK() {
        alarmaActivityInstance.modificarAlarmaNOK()
    }
    override fun eliminarAlarmaOK(position: Int) {
        alarmaListAdapterInstance.eliminarAlarmaOK(position)
        if(position==1){
            _ambAlarmes.value = true
            _senseAlarmes.value = false
        }
    }
    override fun eliminarAlarmaNOK() {
        alarmaListAdapterInstance.eliminarAlarmaNOK()
    }
    override fun llistaAlarmesOK(llistaAlarmes: ArrayList<AlarmaAmbId>) {
        if(llistaAlarmes.size>0){
            _ambAlarmes.value = true
            _senseAlarmes.value = false
        }else{
            _ambAlarmes.value = false
            _senseAlarmes.value = true
        }
        alarmaActivityInstance.llistaAlarmesOK(llistaAlarmes)
    }
    override fun llistaAlarmesNOK() {
        alarmaActivityInstance.llistaAlarmesNOK()
    }

    override fun recuperarIdAlarmaNovaOK(idAlarma : Int) {
        alarmaAfegir.idAlarmaManager = idAlarma
        repository.insertarAlarmaBBDD(alarmaAfegir,this)
    }

    override fun recuperarIdAlarmaNovaNOK() {
        alarmaActivityInstance.recuperarIdAlarmaNovaNOK()
    }

    fun noQuedenAlarmes() {

    }

}