package com.ismasoft.controldiabetic.data.repository

import com.google.firebase.firestore.DocumentSnapshot

interface PerfilRepositoryInterface {

    fun recuperarDadesPersonalsOK(document: DocumentSnapshot?)
    fun recuperarDadesPersonalsNOK()

    fun recuperarDadesMediquesOK(document: DocumentSnapshot?)
    fun recuperarDadesMediquesNOK()

    fun modificarDadesPersOK()
    fun modificarDadesPersNOK()

    fun modificarDadesMedOK()
    fun modificarDadesMedNOK()

    fun validarContrasenyaOK()
    fun validarContrasenyaNOK()

    fun modificarContrasenyaOK()
    fun modificarContrasenyaNOK()

}