package com.ismasoft.controldiabetic.data.repository.interfaces

import com.ismasoft.controldiabetic.data.model.AlarmaAmbId

interface AlarmesRepositoryInterface {

    fun afegirAlarmaOK(idAlarmaManager: Int?)
    fun afegirAlarmaNOK()
    fun jaExisteixAlarma()
    fun noExisteixAlarma()
    fun modificarAlarmaOK(idAlarmaManager: Int?)
    fun modificarAlarmaNOK()
    fun llistaAlarmesOK(llistaAlarmes: ArrayList<AlarmaAmbId>)
    fun llistaAlarmesNOK()

    fun recuperarIdAlarmaNovaOK(idAlarma : Int)
    fun recuperarIdAlarmaNovaNOK()

}