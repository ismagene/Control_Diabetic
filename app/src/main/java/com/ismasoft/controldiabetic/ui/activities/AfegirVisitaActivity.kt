package com.ismasoft.controldiabetic.ui.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.data.model.Visita
import com.ismasoft.controldiabetic.data.model.VisitaAmbId
import com.ismasoft.controldiabetic.data.repository.VisitesRepositoryInterface
import com.ismasoft.controldiabetic.databinding.ActivityAfegirVisitaBinding
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.utilities.hideKeyboard
import com.ismasoft.controldiabetic.viewModel.VisitesViewModel
import org.jetbrains.anko.alert
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AfegirVisitaActivity : AppCompatActivity() , VisitesRepositoryInterface {

    private lateinit var viewModel: VisitesViewModel
    private lateinit var binding: ActivityAfegirVisitaBinding

    private lateinit var colorTextDefault : ColorStateList
    private lateinit var colorHintDefault : ColorStateList
    private var primerOnCreate : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAfegirVisitaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Guardem els colors del hint i del text per defecte
        colorHintDefault = binding.diaVisita.hintTextColors
        colorTextDefault = binding.diaVisita.textColors

        // dia i hora per defecte.
        dataIHoraPerDefecte()

        // Per tornar endarrera
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.guardarVisita.setOnClickListener(){
            /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
            hideKeyboard(this)

            if(validarEntrada()){
                var visita = inicialitzarVisita(binding)
                // Primer comprovarem que poguem insertar una nova visita
                viewModel.onButtonGuardarVisita(visita,this)
            }

        }

        binding.cancelarGuardarVisita.setOnClickListener(){
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
            hideKeyboard(this@AfegirVisitaActivity)
            if(!primerOnCreate) {
                if (hasFocus) {
                    binding.horaVisita.setTextColor(colorTextDefault)
                    obrirCalendariPerSeleccionarHora(binding.horaVisita.text.toString())
                }
            }else{
                /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
                primerOnCreate=false
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

    private fun validarEntrada(): Boolean {
        // L'hora i la data no poden ser null perquè no ho permet la manera d'introduir-ho.
        // Validem que la data no sigui superior o igual a la del dia
        val dataIntroduida = SimpleDateFormat("dd/MM/yyyy HH:mm").parse("${binding.diaVisita.text.toString()} ${binding.horaVisita.text.toString()}")
        val dataActual = Date()
        if(dataIntroduida.before(dataActual))
        {
            binding.diaVisita.setTextColor(Constants.COLOR_ERROR_FALTA_CAMP)
            binding.horaVisita.setTextColor(Constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(
                this,
                "La data i hora de la visita ha de ser posterior a l'actual",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true
    }

    private fun inicialitzarVisita(binding: ActivityAfegirVisitaBinding): Visita {

        val date = binding.diaVisita.text.toString()
        val hora = binding.horaVisita.text.toString()
        val dataHoraVisita  = SimpleDateFormat("dd/MM/yyyy HH:mm").parse("$date $hora")
        var motiu = "Control"
        if(binding.motiuVisita.toString() == null || binding.motiuVisita.toString() == "")
        {
            motiu = binding.motiuVisita.toString()
        }
        return Visita(dataHoraVisita, motiu)
    }

    private fun dataIHoraPerDefecte() {
        val cal = Calendar.getInstance()
        var hora = cal.get(Calendar.HOUR_OF_DAY)
        var minuts = cal.get(Calendar.MINUTE)
        var horaString = hora.toString().padStart(2, '0')
        binding.horaVisita.setText(
            "${hora.toString().padStart(2, '0')}:${
                minuts.toString().padStart(
                    2,
                    '0'
                )
            }"
        )

        var c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_YEAR, +1)
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)+1
        var day = c.get(Calendar.DAY_OF_MONTH)
        binding.diaVisita.setText(
            "${day.toString().padStart(2, '0')}/${
                month.toString().padStart(
                    2,
                    '0'
                )
            }/$year"
        )
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
            this@AfegirVisitaActivity,
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

    override fun afegirVisitaOK() {
        alert("S'ha afegit una nova visita al metge ","Afegida nova visita") {
            cancellable(false)
            positiveButton("Continuar") {
                setResult(RESULT_OK)
                finish()
            }
        }.show()
    }
    override fun afegirVisitaNOK() {
        Toast.makeText(this, "Error al guardar la visita", Toast.LENGTH_SHORT).show()
    }
    override fun existeixVisitaVigent() {
        Toast.makeText(this, "Ja existeix una visita vigent, no s'en pot afegir una altra, modifica l'actual", Toast.LENGTH_SHORT).show()
    }

    override fun errorAlConsultarVisitaVigent() {
        Toast.makeText(this, "Error al guardar la visita", Toast.LENGTH_SHORT).show()
    }

    override fun noExisteixVisitaVigent() {}
    override fun llistaVisitesOK(document: ArrayList<VisitaAmbId>) {}
    override fun llistaVisitesNOK() {}
    override fun modificarVisitaOK() {}
    override fun modificarVisitaNOK() {}
    override fun eliminarVisitaOK() {}
    override fun eliminarVisitaNOK() {}


}