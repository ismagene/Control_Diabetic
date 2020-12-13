package com.ismasoft.controldiabetic.data.model

import java.io.Serializable

data class AlarmaAmbId(
    var idAlarma: String? = null,
    var idAlarmaManager: Int? = null,
    var horaAlarma: String? = null
) : Serializable{

}
