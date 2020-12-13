package com.ismasoft.controldiabetic.data.model

import java.io.Serializable
import java.util.*

data class User(
        // dades personals
        var nom: String? = null,
        var primerCognom: String? = null,
        var segonCognom: String? = null,
        var dataNaixement: Date? = null,
        var correuElectronic: String? = null,
        var genere: String? = null,
        var pes: Number? = null,
        var altura: Number? = null,
        // dades mediques
        var centre: String? = null,
        var poblacioCentre: String? = null,
        var nomMetge: String? = null,
        var correuElectronicMetge: String? = null,
        var tipusDiabetis: String? = null,
        var dataDiagnosi: Date? = null,
        var glucosaMoltBaixa: Number? = null,
        var glucosaBaixa: Number? = null,
        var glucosaAlta: Number? = null,
        var glucosaMoltAlta: Number? = null,
        var glucosaBaixaDespresApat: Number? = null,
        var glucosaAltaDespresApat: Number? = null
) : Serializable{



}

