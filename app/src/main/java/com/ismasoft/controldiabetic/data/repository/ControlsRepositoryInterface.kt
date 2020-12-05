package com.ismasoft.controldiabetic.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.data.model.Control

interface ControlsRepositoryInterface {

    fun afegirControlOK()
    fun afegirControlNOK()
    fun obtenirRangsOK(document: DocumentSnapshot)
    fun obtenirRangsNOK()

    fun llistaControlsOK(document: List<Control>)
    fun LlistaControlsNOK()

    fun modificarControlOK()
    fun modificarControlNOK()
    fun eliminarControlOK()
    fun eliminarControlNOK()

}