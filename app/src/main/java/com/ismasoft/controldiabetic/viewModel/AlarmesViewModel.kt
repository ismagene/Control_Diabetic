package com.ismasoft.controldiabetic.viewModel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.data.model.Alarma
import com.ismasoft.controldiabetic.data.model.Control
import com.ismasoft.controldiabetic.data.repository.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.time.ExperimentalTime
import kotlin.time.days

class AlarmesViewModel(application: Application) : AndroidViewModel(application), AlarmesRepositoryInterface {

    // Definim el repository per accedir a la BBDD
    private var repository = AlarmesRepository(application)
    lateinit var alarmalActivityInstance : AlarmesRepositoryInterface

    private lateinit var alarmaTractada : Alarma

    fun onButtonGuardarAlarma(alarma: Alarma, alarmesRepositoryInterface : AlarmesRepositoryInterface){
        alarmalActivityInstance = alarmesRepositoryInterface
        alarmaTractada = alarma
        repository.comprobarExisteixAlarma(alarma.horaAlarma.toString(), this)

    }

    override fun afegirAlarmaOK() {
        alarmalActivityInstance.afegirAlarmaOK()
    }
    override fun afegirAlarmaNOK() {}
    override fun jaExisteixAlarma() {
        alarmalActivityInstance.jaExisteixAlarma()
    }
    override fun noExisteixAlarma() {
        repository.insertarAlarmaBBDD(alarmaTractada,this)
    }
    override fun modificarAlarmaOK() {}
    override fun modificarAlarmaNOK() {}
    override fun eliminarAlarmaOK() {}
    override fun eliminarAlarmaNOK() {}

}