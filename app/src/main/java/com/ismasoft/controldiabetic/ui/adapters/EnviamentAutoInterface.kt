package com.ismasoft.controldiabetic.ui.adapters

import com.ismasoft.controldiabetic.data.model.ControlAmbId
import com.ismasoft.controldiabetic.data.model.VisitaAmbId

interface EnviamentAutoInterface {

    fun credencialsOK()
    fun credencialsNOK()
    fun visitesOK(document: ArrayList<VisitaAmbId>)
    fun visitesNOK()
    fun llistaControlsOK(document: ArrayList<ControlAmbId>)
    fun llistaControlsNOK()
    fun recuperarDadesUsuariOK(mailMetge: String, nomUsuari: String)
    fun recuperarDadesUsuariNOK()

}