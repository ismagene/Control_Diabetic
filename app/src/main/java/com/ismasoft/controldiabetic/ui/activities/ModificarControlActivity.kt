package com.ismasoft.controldiabetic.ui.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.model.ControlAmbId
import com.ismasoft.controldiabetic.data.repository.interfaces.ControlsRepositoryInterface
import com.ismasoft.controldiabetic.databinding.ActivityModificarControlBinding
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.utilities.hideKeyboard
import com.ismasoft.controldiabetic.viewModel.ControlsViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ModificarControlActivity : AppCompatActivity() , ControlsRepositoryInterface {

    private lateinit var viewModel: ControlsViewModel
    private lateinit var binding: ActivityModificarControlBinding

    private lateinit var colorValorDefault : ColorStateList
    private lateinit var colorTextDefault : ColorStateList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityModificarControlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Guardem els colors del hint i del text per defecte
        colorValorDefault = binding.horaControl.textColors
        colorTextDefault = binding.textDiaControl.textColors

        // Per tornar endarrera
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val objetoIntent : Intent = intent
        val controlModificar : ControlAmbId = objetoIntent.extras?.get("controlModificar") as ControlAmbId

        recuperarDadesControl(controlModificar)

        binding.modificarControl.setOnClickListener(){
            /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
            hideKeyboard(this)

            if(validarEntrada(controlModificar)){
                var control = inicialitzarControl(binding, controlModificar)
                viewModel.onButtonModificarControl(control, this)
            }

        }

        binding.cancelarGuardarControl.setOnClickListener(){
            /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
            hideKeyboard(this)
            finish()
        }

        binding.horaControl.inputType = InputType.TYPE_NULL
        binding.horaControl.setOnClickListener(){
            binding.horaControl.setTextColor(colorValorDefault)
            obrirCalendariPerSeleccionarHora(binding.horaControl.text.toString())
        }
        binding.horaControl.setOnFocusChangeListener(){ _, hasFocus->
            if (hasFocus) {
                binding.horaControl.setTextColor(colorValorDefault)
                obrirCalendariPerSeleccionarHora(binding.horaControl.text.toString())
            }
        }
        binding.diaControl.inputType = InputType.TYPE_NULL
        binding.diaControl.setOnClickListener(){
            hideKeyboard(this)
            binding.diaControl.setTextColor(colorValorDefault)
            obrirCalendariPerSeleccionarData(binding.diaControl.text.toString())
        }
        binding.diaControl.setOnFocusChangeListener(){ _, hasFocus->
            hideKeyboard(this)
            if (hasFocus) {
                binding.diaControl.setTextColor(colorValorDefault)
                obrirCalendariPerSeleccionarData(binding.diaControl.text.toString())
            }
        }
        binding.glucosa.setOnClickListener(){
            binding.textglucosa.setTextColor(colorTextDefault)
        }
        binding.glucosa.setOnFocusChangeListener(){ _, hasFocus->
            if(hasFocus){
                binding.textglucosa.setTextColor(colorTextDefault)
            }
        }

    }

    /** Funcions de privades */
    private fun inicialitzarControl(binding: ActivityModificarControlBinding, controlModificar: ControlAmbId): ControlAmbId {
        val date = binding.diaControl.text.toString()
        val hora = binding.horaControl.text.toString()
        val dataHoraControl  = SimpleDateFormat("dd/MM/yyyy HH:mm").parse("$date $hora")
        val esDespresDapat = binding.esDespresApat.isChecked
        val glucosa = binding.glucosa.text.toString().toInt()
        val insulina : Int
        if(binding.insulina.text != null && binding.insulina.text.toString().trim() != ""){
            insulina  = binding.insulina.text.toString().toInt()
        }else{
            insulina = 0
        }
        return ControlAmbId(controlModificar.idControl,dataHoraControl, glucosa , insulina,esDespresDapat)
    }

    private fun validarEntrada(controlModificar: ControlAmbId): Boolean {
        if(binding.horaControl.text == null || binding.horaControl.text.toString() == ""){
            binding.textHoraControl.setTextColor(Constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "L'hora és obligatòria per modificar el control", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.diaControl.text == null || binding.diaControl.text.toString() == ""){
            binding.textDiaControl.setTextColor(Constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El dia és obligatori per modificar el control", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.glucosa.text == null || binding.glucosa.text.toString() == ""){
            binding.textglucosa.setTextColor(Constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El valor de la glucosa és obligatori per modificar el control", Toast.LENGTH_SHORT).show()
            return false
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        var dataString = sdf.format(controlModificar.dataControl)
        var parts = dataString.split(" ")
        var dia =  parts[0]
        var hora = parts[1]
        if(binding.horaControl.text.toString() == hora &&
            binding.diaControl.text.toString() == dia &&
            binding.esDespresApat.isChecked == controlModificar.esDespresDeApat &&
            binding.glucosa.text == controlModificar.valorGlucosa &&
            (binding.insulina.text == controlModificar.valorInsulina ||
            (binding.insulina.text.equals("") && controlModificar.valorInsulina == 0))){
            Toast.makeText(this, "No s'han modificat les dades del control", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun recuperarDadesControl(controlModificar: ControlAmbId) {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        var dataString = sdf.format(controlModificar.dataControl)
        var parts = dataString.split(" ")
        var dia =  parts[0]
        var hora = parts[1]
        binding.horaControl.setText(hora)
        binding.diaControl.setText(dia)
        binding.esDespresApat.isChecked = controlModificar.esDespresDeApat == true
        binding.glucosa.setText(controlModificar.valorGlucosa.toString())
        binding.insulina.setText(controlModificar.valorInsulina.toString())
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
        val timeSetListener = TimePickerDialog(
            this@ModificarControlActivity,
            TimePickerDialog.OnTimeSetListener() { timePicker, hour, minute ->
                binding.horaControl.setText(
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
                binding.diaControl.setText(
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
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /** Funcions de retorn de l'interficie */
    override fun afegirControlOK() {}
    override fun afegirControlNOK() {}
    override fun obtenirRangsOK(document: DocumentSnapshot) {}
    override fun obtenirRangsNOK() {}
    override fun llistaControlsOK(document: ArrayList<ControlAmbId>) {}
    override fun LlistaControlsNOK() {}
    override fun modificarControlOK() {
        val factory = LayoutInflater.from(this)
        if(viewModel.rangTotalOk.value == true){
            if(viewModel.rangParcialOk.value == true){
                // TOT OK
                var view : View = factory.inflate(R.layout.feedback_control_ok, null);
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("Control modificat")
                    .setCancelable(false)
                    .setView(view)
                    .setMessage("\n"+viewModel.rangMissatge.value.toString())
                    .setPositiveButton("Continuar") { dialogInterface, i ->
                        setResult(RESULT_OK)
                        finish()
                    }
                alertDialog.show()
            }else{
                // SOBREPASA EL LIMIT PARCIAL
                var view : View = factory.inflate(R.layout.feedback_control_semiok, null);
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("Control modificat")
                    .setView(view)
                    .setCancelable(false)
                    .setMessage(viewModel.rangMissatge.value.toString())
                    .setPositiveButton("Continuar") { dialogInterface, i ->
                        setResult(RESULT_OK)
                        finish()
                    }
                alertDialog.show()
            }
        }
        else{
            // SOBREPASA ELS LIMITS TOTALS
            var view : View = factory.inflate(R.layout.feedback_control_nok, null);
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Control modificat")
                .setView(view)
                .setCancelable(false)
                .setMessage(viewModel.rangMissatge.value.toString())
                .setPositiveButton("Continuar") { dialogInterface, i ->
                    setResult(RESULT_OK)
                    finish()
                }
            alertDialog.show()
        }
    }

    override fun modificarControlNOK() {
        Toast.makeText(this, "Error al modificar el control", Toast.LENGTH_SHORT).show()
    }

}