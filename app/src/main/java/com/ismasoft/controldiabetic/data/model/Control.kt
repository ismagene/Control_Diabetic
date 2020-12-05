package com.ismasoft.controldiabetic.data.model

import java.util.*

data class Control(
    var dataControl: Date? = null,
    var valorGlucosa: Number? = null,
    var valorInsulina: Number? = null,
    var esDespresDeApat: Boolean? = null
){

}
