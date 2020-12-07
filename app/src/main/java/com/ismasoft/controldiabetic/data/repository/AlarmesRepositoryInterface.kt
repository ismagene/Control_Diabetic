package com.ismasoft.controldiabetic.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.data.model.Control

interface AlarmesRepositoryInterface {

    fun afegirAlarmaOK()
    fun afegirAlarmaNOK()
    fun jaExisteixAlarma()
    fun noExisteixAlarma()
    fun modificarAlarmaOK()
    fun modificarAlarmaNOK()
    fun eliminarAlarmaOK()
    fun eliminarAlarmaNOK()

}