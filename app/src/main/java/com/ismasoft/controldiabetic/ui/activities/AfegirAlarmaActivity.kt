package com.ismasoft.controldiabetic.ui.activities

import android.app.TimePickerDialog
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.model.Alarma
import com.ismasoft.controldiabetic.data.model.Control
import com.ismasoft.controldiabetic.data.repository.AlarmesRepositoryInterface
import com.ismasoft.controldiabetic.databinding.ActivityAfegirAlarmaBinding
import com.ismasoft.controldiabetic.databinding.ActivityAfegirControlBinding
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.utilities.hideKeyboard
import com.ismasoft.controldiabetic.viewModel.AlarmesViewModel
import com.ismasoft.controldiabetic.viewModel.ControlsViewModel
import org.jetbrains.anko.alert
import java.text.SimpleDateFormat
import java.util.*

class AfegirAlarmaActivity : AppCompatActivity(), AlarmesRepositoryInterface {

    private lateinit var viewModel: AlarmesViewModel
    private lateinit var binding: ActivityAfegirAlarmaBinding

    private lateinit var colorTextDefault : ColorStateList
    private var primerOnCreate : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAfegirAlarmaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Guardem els colors del hint i del text per defecte
        colorTextDefault = binding.horaControl.textColors

        // dia i hora per defecte.
        dataIHoraPerDefecte()

        // Per tornar endarrera
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.guardarAlarma.setOnClickListener(){
            /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
            hideKeyboard(this)

            if(validarEntrada()){
                var alarma = inicialitzarAlarma(binding)
                viewModel.onButtonGuardarAlarma(alarma, this)
            }

        }

        binding.cancelarGuardarAlama.setOnClickListener(){
            /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
            hideKeyboard(this)
            finish()
        }

        binding.horaControl.setOnClickListener(){
            hideKeyboard(this)
            obrirCalendariPerSeleccionarHora(binding.horaControl.text.toString())
        }
        binding.horaControl.setOnFocusChangeListener(){ _, hasFocus->
            hideKeyboard(this@AfegirAlarmaActivity)

            if(!primerOnCreate) {
                if (hasFocus) {
                    obrirCalendariPerSeleccionarHora(binding.horaControl.text.toString())
                }
            }else{
                /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
                primerOnCreate=false
            }

        }

    }

    private fun inicialitzarAlarma(binding: ActivityAfegirAlarmaBinding): Alarma {
        val hora = binding.horaControl.text.toString()
        return Alarma(hora)
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
    }

    private fun validarEntrada(): Boolean {
        if(binding.horaControl.text == null || binding.horaControl.text.toString() == ""){
            binding.horaControl.setTextColor(Constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "Falta introduir el valor de glucosa", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
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
            this@AfegirAlarmaActivity,
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

    override fun afegirAlarmaOK() {
        alert("S'ha guardat correctament l'alarma.","Alarma guardada") {
            cancellable(false)
            positiveButton("Continuar") {
                setResult(RESULT_OK)
                finish()
            }
        }.show()
    }
    override fun afegirAlarmaNOK() {}
    override fun jaExisteixAlarma() {
        alert("L'alarma introduida ja existeix","Error al guardar l'alarma") {
            cancellable(false)
            positiveButton("Continuar") {
                setResult(RESULT_OK)
                finish()
            }
        }.show()
    }

    override fun noExisteixAlarma() {}
    override fun modificarAlarmaOK() {}
    override fun modificarAlarmaNOK() {}
    override fun eliminarAlarmaOK() {}
    override fun eliminarAlarmaNOK() {}


}