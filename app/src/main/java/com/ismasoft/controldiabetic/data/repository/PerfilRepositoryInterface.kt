package com.ismasoft.controldiabetic.data.repository

import com.google.firebase.firestore.DocumentSnapshot

interface PerfilRepositoryInterface {

    fun recuperarDadesPersonalsOK(document: DocumentSnapshot?)
    fun recuperarDadesPersonalsNOK()

    fun modificarDadesPersOK()
    fun modificarDadesPersNOK()


}