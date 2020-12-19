package com.ismasoft.controldiabetic.utilities

object Constants {

    const val DB_ROOT_USUARIS = "usuaris"

    const val DB_ROOT_VISITES = "visites"

    const val DB_ROOT_CONTROLS = "controls"

    const val DB_ROOT_ALARMES = "alarmes"

    /* ERRORS DE LOGIN */
    const val ERROR_FALTA_USUARI_I_CONTRASENYA : String = "No s'ha introduit correu electrònic ni contrasenya"
    const val ERROR_FALTA_USUARI : String = "No s'ha introduit el correu electrònic"
    const val ERROR_FALTA__CONTRASENYA : String = "No s'ha introduit la contrasenya"

    const val COLOR_ERROR_FALTA_CAMP : Int = -65536

    const val OPCIO_DEFECTE_SPINER : String = "Selecciona"

    /* REQUEST CODES */
    const val REGISTRE_2_CODE = 100
    const val RETORN_ACTIVITY_OK_CODE = 100


    /* Missatges d'error Registre */
    const val ERROR_00001 : String = "El nom és un camp obligatori"
    const val ERROR_00002 : String = "El cognom és un camp obligatori"
    const val ERROR_00003 : String = "El segon cognom és un camp obligatori"
    const val ERROR_00004 : String = "La data de naixement és un camp obligatori"
    const val ERROR_00005 : String = "La data de naixement no pot ser igual o superior a l'actual'"
    const val ERROR_00006 : String = "El genere és un camp obligatori"
    const val ERROR_00007 : String = "El Pes és un camp obligatori"
    const val ERROR_00008 : String = "L'alçada és un camp obligatori"
    const val ERROR_00009 : String = "El correu electrònic és un camp obligatori"
    const val ERROR_00010 : String = "El format del correu electrònic no es correcte"
    const val ERROR_00011 : String = "La contrasenya és un camp obligatori"
    const val ERROR_00012 : String = "La contrasenya ha de ser mínim de 6 caracters"
    const val ERROR_00013 : String = "Aquest correu electrònic ja està registra't"
    const val ERROR_00014 : String = "El pes ha de ser major que zero"
    const val ERROR_00015 : String = "L'alçada ha de ser major que zero"

    const val ERROR_00016 : String = "El nom del centre és un camp obligatori"
    const val ERROR_00017 : String = "La població del centre és un camp obligatori"
    const val ERROR_00018 : String = "El nom del metge és un camp obligatori"
    const val ERROR_00019 : String = "El correu electrònic del metge és un camp obligatori"
    const val ERROR_00020 : String = "El format del correu electrònic del metge no es correcte"
    const val ERROR_00021 : String = "El tipus de diabetis és un camp obligatori"
    const val ERROR_00022 : String = "La data de naixement és un camp obligatori"
    const val ERROR_00023 : String = "La data de diagnosi no pot ser superior a l'actual'"



}