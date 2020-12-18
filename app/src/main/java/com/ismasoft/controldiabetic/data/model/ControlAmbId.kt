package com.ismasoft.controldiabetic.data.model

import java.io.Serializable
import java.util.*

data class ControlAmbId(
    var idControl: String? = null,
    override var dataControl: Date? = null,
    override var valorGlucosa: Number? = null,
    override var valorInsulina: Number? = null,
    override var esDespresDeApat: Boolean? = null
) : Control(dataControl,valorGlucosa,valorInsulina,esDespresDeApat),Serializable{

}
