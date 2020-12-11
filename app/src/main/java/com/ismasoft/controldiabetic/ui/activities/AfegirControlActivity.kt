package com.ismasoft.controldiabetic.ui.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.model.Control
import com.ismasoft.controldiabetic.data.repository.ControlsRepositoryInterface
import com.ismasoft.controldiabetic.databinding.ActivityAfegirControlBinding
import com.ismasoft.controldiabetic.utilities.Constants.COLOR_ERROR_FALTA_CAMP
import com.ismasoft.controldiabetic.utilities.hideKeyboard
import com.ismasoft.controldiabetic.viewModel.ControlsViewModel
import org.jetbrains.anko.alert
import java.text.SimpleDateFormat
import java.util.*


class AfegirControlActivity : AppCompatActivity(), ControlsRepositoryInterface {

    private lateinit var viewModel: ControlsViewModel
    private lateinit var binding: ActivityAfegirControlBinding

    private lateinit var colorHintDefault : ColorStateList
    private lateinit var colorTextDefault : ColorStateList

    private var primerOnCreate : Boolean = true
    private lateinit var llistaControls : List<Control>

    @SuppressLint("RestrictedApi")
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

        binding.cancelarGuardarControl.setOnClickListener(){
            /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
            hideKeyboard(this)
            finish()
        }

        binding.horaControl.setOnClickListener(){
            hideKeyboard(this)
            binding.horaControl.setTextColor(colorTextDefault)
            obrirCalendariPerSeleccionarHora(binding.horaControl.text.toString())
        }
        binding.horaControl.setOnFocusChangeListener(){ _, hasFocus->
            hideKeyboard(this@AfegirControlActivity)
            if(!primerOnCreate) {
                if (hasFocus) {
                    binding.horaControl.setTextColor(colorTextDefault)
                    obrirCalendariPerSeleccionarHora(binding.horaControl.text.toString())
                }
            }else{
                /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
                primerOnCreate=false
            }

        }
        binding.diaControl.setOnClickListener(){
            hideKeyboard(this)
            binding.diaControl.setTextColor(colorTextDefault)
            obrirCalendariPerSeleccionarData(binding.diaControl.text.toString())
        }
        binding.diaControl.setOnFocusChangeListener(){ _, hasFocus->
            if (hasFocus) {
                hideKeyboard(this)
                binding.diaControl.setTextColor(colorTextDefault)
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

        return Control(dataHoraControl, glucosa, insulina, despresApat)
    }

    private fun dataIHoraPerDefecte() {
        val cal = Calendar.getInstance()
        var hora = cal.get(Calendar.HOUR_OF_DAY)
        var minuts = cal.get(Calendar.MINUTE)
        var horaString = hora.toString().padStart(2, '0')
        binding.horaControl.setText(
            "${hora.toString().padStart(2, '0')}:${
                minuts.toString().padStart(
                    2,
                    '0'
                )
            }"
        )

        var c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)+1
        var day = c.get(Calendar.DAY_OF_MONTH)
        binding.diaControl.setText(
            "${day.toString().padStart(2, '0')}/${
                month.toString().padStart(
                    2,
                    '0'
                )
            }/$year"
        )
    }

    private fun validarEntrada(): Boolean {
        if(binding.glucosa.text == null || binding.glucosa.text.toString() == ""){
            binding.textglucosa.setTextColor(COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "Falta introduir el valor de glucosa", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validem que la data no sigui superior o igual a la del dia
        val dataIntroduida = SimpleDateFormat("dd/MM/yyyy HH:mm").parse("${binding.diaControl.text.toString()} ${binding.horaControl.text.toString()}")
        val dataActual = Date()
        if(dataIntroduida.after(dataActual))
        {
            binding.diaControl.setTextColor(COLOR_ERROR_FALTA_CAMP)
            binding.horaControl.setTextColor(COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(
                this,
                "La data i hora del control no  pot ser superior a l'actual",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true
    }

    private fun obrirCalendariPerSeleccionarHora(temps: String) {
        val cal = Calendar.getInstance()
        var hora = cal.get(Calendar.HOUR_OF_DAY)
        var minuts = cal.get(Calendar.MINUTE)

        binding.horaControl.setHintTextColor(colorHintDefault)

        if(temps != null && !temps.equals("")){
            var parts = temps.split(":")
            hora =  parts[0].toInt()
            minuts = parts[1].toInt()
        }
        val timeSetListener = TimePickerDialog(
            this@AfegirControlActivity,
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
        /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
        hideKeyboard(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun afegirControlOK() {
        val factory = LayoutInflater.from(this)
        if(viewModel.rangTotalOk.value == true){
            if(viewModel.rangParcialOk.value == true){
                // TOT OK
                var view : View = factory.inflate(R.layout.feedback_control_ok, null);
                val alertDialog = AlertDialog.Builder(this)
                        .setTitle("Control guardat")
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
                    .setTitle("Control guardat")
                    .setView(view)
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
                .setTitle("Control guardat")
                .setView(view)
                .setMessage(viewModel.rangMissatge.value.toString())
                .setPositiveButton("Continuar") { dialogInterface, i ->
                    setResult(RESULT_OK)
                    finish()
                }
            alertDialog.show()
        }
    }

    override fun afegirControlNOK() {
        Toast.makeText(this, "Error al guardar el control", Toast.LENGTH_SHORT).show()
    }

    override fun obtenirRangsOK(document: DocumentSnapshot) {}
    override fun obtenirRangsNOK() {}
    override fun llistaControlsOK(document: HashMap<String, Control>) {}
    override fun LlistaControlsNOK() {}
    override fun modificarControlOK() {}
    override fun modificarControlNOK() {}
    override fun eliminarControlOK() {}
    override fun eliminarControlNOK() {}
}