package com.ismasoft.controldiabetic.data.repository

import com.ismasoft.controldiabetic.data.model.AlarmaAmbId

interface AlarmesRepositoryInterface {

    fun afegirAlarmaOK(idAlarmaManager: Int?)
    fun afegirAlarmaNOK()
    fun jaExisteixAlarma()
    fun noExisteixAlarma()
    fun modificarAlarmaOK()
    fun modificarAlarmaNOK()
    fun llistaAlarmesOK(llistaAlarmes: ArrayList<AlarmaAmbId>)
    fun llistaAlarmesNOK()

    fun recuperarIdAlarmaNovaOK(idAlarma : Int)
    fun recuperarIdAlarmaNovaNOK()

}