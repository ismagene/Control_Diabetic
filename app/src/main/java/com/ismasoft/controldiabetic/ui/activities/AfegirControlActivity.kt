package com.ismasoft.controldiabetic.ui.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.data.model.Control
import com.ismasoft.controldiabetic.data.repository.ControlsRepositoryInterface
import com.ismasoft.controldiabetic.databinding.ActivityAfegirControlBinding
import com.ismasoft.controldiabetic.utilities.Constants.COLOR_ERROR_FALTA_CAMP
import com.ismasoft.controldiabetic.utilities.hideKeyboard
import com.ismasoft.controldiabetic.viewModel.ControlsViewModel
import org.jetbrains.anko.alert
import java.util.*
import java.text.SimpleDateFormat

class AfegirControlActivity : AppCompatActivity(), ControlsRepositoryInterface {

    private lateinit var viewModel: ControlsViewModel
    private lateinit var binding: ActivityAfegirControlBinding

    private lateinit var colorHintDefault : ColorStateList
    private lateinit var colorTextDefault : ColorStateList

    private var primerOnCreate : Boolean = true
    private lateinit var llistaControls : List<Control>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAfegirControlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Guardem els colors del hint i del text per defecte
        colorHintDefault = binding.glucosa.hintTextColors
        colorTextDefault = binding.glucosa.textColors

        // dia i hora per defecte.
        dataIHoraPerDefecte()

        // Per tornar endarrera
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.guardarControl.setOnClickListener(){
            /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
            hideKeyboard(this)

            if(validarEntrada()){
                var control=inicialitzarControl(binding)
                viewModel.onButtonGuardarControl(control, this)
            }

        }

        binding.horaControl.setOnClickListener(){
            hideKeyboard(this)
            obrirCalendariPerSeleccionarHora(binding.horaControl.text.toString())
        }
        binding.horaControl.setOnFocusChangeListener(){ _, hasFocus->
            if(!primerOnCreate) {
                if (hasFocus) {
                    obrirCalendariPerSeleccionarHora(binding.horaControl.text.toString())
                    hideKeyboard(this)
                }
            }else{
                /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
                hideKeyboard(this)
                primerOnCreate=false
            }

        }
        binding.diaControl.setOnClickListener(){
            hideKeyboard(this)
            obrirCalendariPerSeleccionarData(binding.diaControl.text.toString())
        }
        binding.diaControl.setOnFocusChangeListener(){ _, hasFocus->
            if (hasFocus) {
                hideKeyboard(this)
                obrirCalendariPerSeleccionarData(binding.diaControl.text.toString())
            }
        }
        binding.glucosa.setOnClickListener(){
            binding.glucosatext.setTextColor(colorTextDefault)
        }
        binding.glucosa.setOnFocusChangeListener(){_,hasFocus->
            if(hasFocus){
                binding.glucosatext.setTextColor(colorTextDefault)
            }
        }

    }

    private fun inicialitzarControl(binding: ActivityAfegirControlBinding): Control {

        val date = binding.diaControl.text.toString()
        val hora = binding.horaControl.text.toString()
        var dataHoraControl  = SimpleDateFormat("dd/MM/yyyy HH:mm").parse("$date $hora")
        var glucosa = binding.glucosa.text.toString().toInt()
        var insulina : Int
        if(binding.insulina.text != null && binding.insulina.text.toString().trim() != ""){
            insulina  = binding.insulina.text.toString().toInt()
        }else{
            insulina = 0
        }
        val despresApat = binding.esDespresApat.isChecked

        return Control(dataHoraControl,glucosa,insulina,despresApat)
    }

    private fun dataIHoraPerDefecte() {
        val cal = Calendar.getInstance()
        var hora = cal.get(Calendar.HOUR_OF_DAY)
        var minuts = cal.get(Calendar.MINUTE)
        var horaString = hora.toString().padStart(2,'0')
        binding.horaControl.setText("${hora.toString().padStart(2,'0')}:${minuts.toString().padStart(2,'0')}")

        var c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)+1
        var day = c.get(Calendar.DAY_OF_MONTH)
        binding.diaControl.setText("${day.toString().padStart(2,'0')}/${month.toString().padStart(2,'0')}/$year")
    }

    private fun validarEntrada(): Boolean {
        if(binding.glucosa.text == null || binding.glucosa.text.toString() == ""){
            binding.glucosatext.setTextColor(COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "Falta introduir el valor de glucosa", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun obrirCalendariPerSeleccionarHora(temps : String) {
        val cal = Calendar.getInstance()
        var hora = cal.get(Calendar.HOUR_OF_DAY)
        var minuts = cal.get(Calendar.MINUTE)

        if(temps != null && !temps.equals("")){
            var parts = temps.split(":")
            hora =  parts[0].toInt()
            minuts = parts[1].toInt()
        }
        val timeSetListener = TimePickerDialog(this@AfegirControlActivity,TimePickerDialog.OnTimeSetListener() { timePicker, hour, minute ->
            binding.horaControl.setText("${hour.toString().padStart(2,'0')}:${minute.toString().padStart(2,'0')}")
        },
        hora,
        minuts,true)
        timeSetListener.show()
    }

    private fun obrirCalendariPerSeleccionarData(data : String){
        /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
        hideKeyboard(this)

        binding.diaControl.setHintTextColor(colorHintDefault)

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
                binding.diaControl.setText("${dayOfMonth.toString().padStart(2,'0')}/${(monthOfYear+1).toString().padStart(2,'0')}/$year")
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

    override fun afegirControlOK() {

        if(viewModel.rangTotalOk.value == true){
            if(viewModel.rangParcialOk.value == true){
                // TOT OK
                alert {
                    title = "Control guardat dins del rang"
                    message(viewModel.rangMissatge.value.toString())
                    cancellable(false)
                    positiveButton("Continuar") {
                        setResult(RESULT_OK)
                        finish()
                    }
                }.show()
            }else{
                // SOBREPASA EL LIMIT PARCIAL
                alert {
                    title = "Control guardat dins del rang maxim pero sobrepasa el rang"
                    message(viewModel.rangMissatge.value.toString())
                    cancellable(false)
                    positiveButton("Continuar") {
                        setResult(RESULT_OK)
                        finish()
                    }
                }.show()
            }
        }
        else{
            // SOBREPASA ELS LIMITS TOTALS
            alert {
                title = "Control guardat fora de rang"
                message(viewModel.rangMissatge.value.toString())
                cancellable(false)
                positiveButton("Continuar") {
                    setResult(RESULT_OK)
                    finish()
                }
            }.show()
        }
    }

    override fun afegirControlNOK() {
        Toast.makeText(this, "Error al guardar el control", Toast.LENGTH_SHORT).show()
    }

    override fun obtenirRangsOK(document: DocumentSnapshot) {}
    override fun obtenirRangsNOK() {}
    override fun llistaControlsOK(document: List<Control>) {}
    override fun LlistaControlsNOK() {}
    override fun modificarControlOK() {}
    override fun modificarControlNOK() {}
    override fun eliminarControlOK() {}
    override fun eliminarControlNOK() {}
}