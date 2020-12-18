package com.ismasoft.controldiabetic.data.model

import java.io.Serializable

data class AlarmaAmbId(
    var idAlarma: String? = null,
    override var idAlarmaManager: Int? = null,
    override var horaAlarma: String? = null
): Alarma(idAlarmaManager, horaAlarma) , Serializable{

}
