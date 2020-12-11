package com.ismasoft.controldiabetic.data.model

import java.io.Serializable
import java.util.*

data class VisitaAmbId(
    var idVisita: String? = null,
    var dataVisita: Date? = null,
    var motiu: String? = null,
) : Serializable{

}
