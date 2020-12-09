package com.ismasoft.controldiabetic.data.model

import java.io.Serializable
import java.util.*

data class ControlAmbId(
    var idControl: String? = null,
    var dataControl: Date? = null,
    var valorGlucosa: Number? = null,
    var valorInsulina: Number? = null,
    var esDespresDeApat: Boolean? = null
) : Serializable{

}
