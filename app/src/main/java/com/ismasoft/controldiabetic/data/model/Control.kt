package com.ismasoft.controldiabetic.data.model

import java.util.*

open class Control(
    open var dataControl: Date? = null,
    open var valorGlucosa: Number? = null,
    open var valorInsulina: Number? = null,
    open var esDespresDeApat: Boolean? = null
){

}
