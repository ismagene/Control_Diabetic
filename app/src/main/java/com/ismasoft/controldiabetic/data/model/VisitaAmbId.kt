package com.ismasoft.controldiabetic.data.model

import java.io.Serializable
import java.util.*

data class VisitaAmbId(
    var idVisita: String? = null,
    override var dataVisita: Date? = null,
    override var motiu: String? = null,
) : Visita(dataVisita,motiu) , Serializable{

}
