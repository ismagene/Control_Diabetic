package com.ismasoft.controldiabetic.utilities

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.data.model.ControlAmbId
import com.ismasoft.controldiabetic.data.model.VisitaAmbId
import com.ismasoft.controldiabetic.data.repository.interfaces.ControlsRepositoryInterface
import com.ismasoft.controldiabetic.data.repository.interfaces.LoginRepositoryInterface
import com.ismasoft.controldiabetic.ui.adapters.EnviamentAutoAdapter
import com.ismasoft.controldiabetic.ui.adapters.EnviamentAutoInterface
import java.text.SimpleDateFormat

class EnviamentAutoHistoricService: IntentService , EnviamentAutoInterface{
    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    internal lateinit var notification: Notification
    constructor(name:String) : super(name) {}
    constructor() : super("SERVICE") {}

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    // Definim el repository per accedir a la BBDD
    var adapter = EnviamentAutoAdapter()

    var llistaVisitesFiltre = ArrayList<VisitaAmbId>()
    var llistaControlsFiltrats = ArrayList<ControlAmbId>()

    @TargetApi(Build.VERSION_CODES.O)
    override fun onHandleIntent(intent: Intent?) {

        preferences = applicationContext.getSharedPreferences("ControlDiabetic", MODE_PRIVATE)
        editor = preferences.edit()

        // Fer loggin previ amb les dades del sharedPreference, per si l'aplicació està tancada i s'ha reiniciat
        recuperarUsuariDelPreference()
    }

    override fun credencialsOK() {
        // Obtenir les llistes de visites
        adapter.recuperarLlistaVisites(this)
    }

    override fun visitesOK(llistaVisites: ArrayList<VisitaAmbId>) {

        // Ordenem els controls.
        llistaVisites.sortWith(kotlin.Comparator { o1, o2 ->
            var date1 = o1.dataVisita
            var date2 = o2.dataVisita
            date2?.compareTo(date1)!! // Comparamem les dates
        })

        // Si te més tamany que 1 es que té una visita passada
        if(llistaVisites.size>1){
            llistaVisitesFiltre.add(llistaVisites[0])
            llistaVisitesFiltre.add(llistaVisites[1])
        }else{
            llistaVisitesFiltre.add(llistaVisites[0])
            var iniciVirtual = VisitaAmbId()
            var dataFiltreInici = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/1900 00:00:00")
            iniciVirtual.dataVisita = dataFiltreInici
            llistaVisitesFiltre.add(iniciVirtual)
        }

        // Obtenir dades històriques des d'avui a l'ultima visita.
        adapter.recuperarLlistaControls(this)
    }
    override fun llistaControlsOK(llistaControls: ArrayList<ControlAmbId>) {

        // Ordenem els controls.
        llistaControls.sortWith(kotlin.Comparator { o1, o2 ->
            var date1 = o1.dataControl
            var date2 = o2.dataControl
            date2?.compareTo(date1)!! // Comparamem les dates
        })

        // Fer filtratge

        var dataFiltreInici = llistaVisitesFiltre[1].dataVisita
        var dataFiltreFi = llistaVisitesFiltre[0].dataVisita

        for (control in llistaControls) {
            if (dataFiltreInici?.before(control.dataControl) == true && dataFiltreFi?.after(control.dataControl) == true) {
                llistaControlsFiltrats.add(control)
            }
        }

        // Recuperar dades de l'usuari
        adapter.recuperarDadesUsuari(this)
    }

    override fun recuperarDadesUsuariOK(mailMetge: String, nomUsuari: String) {
        // Fer enviament.
        val mEmail: String = mailMetge
        val mSubject: String = "Control diabètic - Històric de controls de glucosa "
        var mMessage: String = "Històric de controls de glucosa: \n\n"
        mMessage += "Usuari: ${nomUsuari} \n"

        var diaAcutal = ""
        for (control in llistaControlsFiltrats) {

            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
            var dataString = sdf.format(control.dataControl)
            var parts = dataString.split(" ")
            var dia = parts[0]
            var hora = parts[1]
            if (diaAcutal.equals("")) {
                diaAcutal = dia
                mMessage += ("\nControls del dia: $dia")
            }else if(!diaAcutal.equals(dia)){
                mMessage += ("\n\nControls del dia: $dia")
            }
            mMessage += ("\n   Hora: $hora -> Glucosa: ${control.valorGlucosa} ")
            if (control.valorInsulina.toString().toInt() > 0) {
                mMessage += ("Insulina: ${control.valorInsulina}")
            }
            if(control.esDespresDeApat == true){
                mMessage += (" el control és desprès d'àpat.")
            }
        }

        mMessage = "$mMessage\n\n\nAtt; L'equip de l'aplicació: Control diabètic."

        val javaMailAPI = JavaMailAPI(this, mEmail, mSubject, mMessage)
        javaMailAPI.execute()

        /* Preferences per guardar dades en un xml local */
        preferences = applicationContext.getSharedPreferences("sharedAlarmaVisita", MODE_PRIVATE)
        editor = preferences.edit()
        editor.clear()
    }

    override fun credencialsNOK() {}
    override fun visitesNOK() {}
    override fun llistaControlsNOK() {}
    override fun recuperarDadesUsuariNOK() {}

    /** Es recupera la informació del sharePreference i si no ens hem desgloguejat entrarem automàticament **/
    private fun recuperarUsuariDelPreference() {
        var usuariGuardat = preferences.getString("usuariGuardat", null)
        var contrasenyaGuardada = preferences.getString("contrasenyaGuardada", null)
        if (usuariGuardat != null && contrasenyaGuardada != null) {
            adapter.loginAutomatic( usuariGuardat.toString(), contrasenyaGuardada.toString(),this)
        }
    }

}