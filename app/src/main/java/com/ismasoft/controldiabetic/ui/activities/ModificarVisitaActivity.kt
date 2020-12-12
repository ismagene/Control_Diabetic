package com.ismasoft.controldiabetic.ui.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.data.model.Visita
import com.ismasoft.controldiabetic.data.model.VisitaAmbId
import com.ismasoft.controldiabetic.data.repository.VisitesRepositoryInterface
import com.ismasoft.controldiabetic.databinding.ActivityModificarVisitaBinding
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.utilities.hideKeyboard
import com.ismasoft.controldiabetic.viewModel.VisitesViewModel
import org.jetbrains.anko.alert
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ModificarVisitaActivity : AppCompatActivity() , VisitesRepositoryInterface {

    private lateinit var viewModel: VisitesViewModel
    private lateinit var binding: ActivityModificarVisitaBinding

    private lateinit var colorTextDefault : ColorStateList
    private lateinit var colorHintDefault : ColorStateList
    private var primerOnCreate : Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityModificarVisitaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Guardem els colors del hint i del text per defecte
        colorHintDefault = binding.diaVisita.hintTextColors
        colorTextDefault = binding.diaVisita.textColors

        // Per tornar endarrera
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val objetoIntent : Intent = intent
        val visitaModificar : VisitaAmbId = objetoIntent.extras?.get("visitaModificar") as VisitaAmbId

        recuperarDadesVisita(visitaModificar)

        binding.modificarVisita.setOnClickListener(){
            /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
            hideKeyboard(this)

            if(validarEntrada(visitaModificar)){
                var visita = inicialitzarVisita(binding, visitaModificar)
                viewModel.onButtoModificarVisita(visita, this)
            }

        }

        binding.cancelarModificarVisita.setOnClickListener(){
            /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
            hideKeyboard(this)
            finish()
        }

        binding.horaVisita.setOnClickListener(){
            hideKeyboard(this)
            binding.horaVisita.setTextColor(colorTextDefault)
            obrirCalendariPerSeleccionarHora(binding.horaVisita.text.toString())
        }
        binding.horaVisita.setOnFocusChangeListener(){ _, hasFocus->
            hideKeyboard(this@ModificarVisitaActivity)
            if (hasFocus) {
                binding.horaVisita.setTextColor(colorTextDefault)
                obrirCalendariPerSeleccionarHora(binding.horaVisita.text.toString())
            }
        }
        binding.diaVisita.setOnClickListener(){
            hideKeyboard(this)
            binding.diaVisita.setTextColor(colorTextDefault)
            obrirCalendariPerSeleccionarData(binding.diaVisita.text.toString())
        }
        binding.diaVisita.setOnFocusChangeListener(){ _, hasFocus->
            if (hasFocus) {
                hideKeyboard(this)
                binding.diaVisita.setTextColor(colorTextDefault)
                obrirCalendariPerSeleccionarData(binding.diaVisita.text.toString())
            }
        }

    }

    private fun recuperarDadesVisita(visitaModificar: VisitaAmbId) {

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        var dataString = sdf.format(visitaModificar.dataVisita)
        var parts = dataString.split(" ")
        var dia =  parts[0]
        var hora = parts[1]
        binding.horaVisita.setText(hora)
        binding.diaVisita.setText(dia)
        binding.motiuVisita.setText(visitaModificar.motiu)
    }

    private fun validarEntrada(visitaModificar: VisitaAmbId): Boolean {
        if(binding.horaVisita.text == null || binding.horaVisita.text.toString() == ""){
            binding.textHoraVisita.setTextColor(Constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "L'hora és obligatoria per modificar la visita", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.diaVisita.text == null || binding.diaVisita.text.toString() == ""){
            binding.textDiaVisita.setTextColor(Constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "La data és obligatoria per modificar la visita", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.motiuVisita.text == null || binding.motiuVisita.text.toString() == ""){
            binding.textMotiuVisita.setTextColor(Constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El motiu és obligatori per modificar la visita", Toast.LENGTH_SHORT).show()
            return false
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        var dataString = sdf.format(visitaModificar.dataVisita)
        var parts = dataString.split(" ")
        var dia =  parts[0]
        var hora = parts[1]
        if(binding.horaVisita.text.toString() == hora.toString() &&
            binding.diaVisita.text.toString() == dia &&
            binding.motiuVisita.text.toString() == visitaModificar.motiu){
            Toast.makeText(this, "No s'ha modificat les dades de la visita", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun inicialitzarVisita(binding: ActivityModificarVisitaBinding,visitaModificar: VisitaAmbId): VisitaAmbId {

        val date = binding.diaVisita.text.toString()
        val hora = binding.horaVisita.text.toString()
        val dataHoraVisita  = SimpleDateFormat("dd/MM/yyyy HH:mm").parse("$date $hora")
        var motiu = "Control"
        if(binding.motiuVisita.toString() == null || binding.motiuVisita.toString() == "")
        {
            motiu = binding.motiuVisita.toString()
        }
        return VisitaAmbId(visitaModificar.idVisita,dataHoraVisita, motiu)
    }

    private fun obrirCalendariPerSeleccionarHora(temps: String) {
        val cal = Calendar.getInstance()
        var hora = cal.get(Calendar.HOUR_OF_DAY)
        var minuts = cal.get(Calendar.MINUTE)

        if(temps != null && !temps.equals("")){
            var parts = temps.split(":")
            hora =  parts[0].toInt()
            minuts = parts[1].toInt()
        }
        binding.horaVisita.setHintTextColor(colorHintDefault)
        val timeSetListener = TimePickerDialog(
            this@ModificarVisitaActivity,
            TimePickerDialog.OnTimeSetListener() { timePicker, hour, minute ->
                binding.horaVisita.setText(
                    "${hour.toString().padStart(2, '0')}:${
                        minute.toString().padStart(
                            2,
                            '0'
                        )
                    }"
                )
            },
            hora,
            minuts,
            true
        )
        timeSetListener.show()
    }

    private fun obrirCalendariPerSeleccionarData(data: String){
        /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
        hideKeyboard(this)

        binding.diaVisita.setHintTextColor(colorHintDefault)

        var c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)

        if(data != null && !data.equals("")){
            var parts = data.split("/")
            day =  parts[0].toInt()
            month = parts[1].toInt()-1
            year = parts[2].toInt()
        }

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener() { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                binding.diaVisita.setText(
                    "${dayOfMonth.toString().padStart(2, '0')}/${
                        (monthOfYear + 1).toString().padStart(
                            2,
                            '0'
                        )
                    }/$year"
                )
            },
            year,
            month,
            day
        )

        dpd.show()
        /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
        hideKeyboard(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun afegirVisitaOK() {}
    override fun afegirVisitaNOK() {}
    override fun existeixVisitaVigent() {}
    override fun errorAlConsultarVisitaVigent() {}
    override fun noExisteixVisitaVigent() {}
    override fun llistaVisitesOK(document: ArrayList<VisitaAmbId>) {}
    override fun llistaVisitesNOK() {}
    override fun modificarVisitaOK() {
        alert("S'ha modificat correctament la visita.","Visita modificada") {
            cancellable(false)
            positiveButton("Continuar") {
                setResult(RESULT_OK)
                finish()
            }
        }.show()
    }
    override fun modificarVisitaNOK() {
        Toast.makeText(this, "Error al modificar la visita", Toast.LENGTH_SHORT).show()
        finish()
    }
    override fun eliminarVisitaOK() {

    }
    override fun eliminarVisitaNOK() {
        Toast.makeText(this, "Error al eliminar la visita", Toast.LENGTH_SHORT).show()
        finish()
    }

}