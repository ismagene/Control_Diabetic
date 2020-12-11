package com.ismasoft.controldiabetic.ui.activities

import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.data.model.Alarma
import com.ismasoft.controldiabetic.data.model.AlarmaAmbId
import com.ismasoft.controldiabetic.data.repository.AlarmesRepositoryInterface
import com.ismasoft.controldiabetic.databinding.ActivityAfegirAlarmaBinding
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.utilities.hideKeyboard
import com.ismasoft.controldiabetic.utilities.setAlarm
import com.ismasoft.controldiabetic.viewModel.AlarmesViewModel
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
        colorTextDefault = binding.horaAlarma.textColors

        // dia i hora per defecte.
        dataIHoraPerDefecte()

        // amaguem el teclat d'inici
        hideKeyboard(this)

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

        binding.horaAlarma.setOnClickListener(){
            hideKeyboard(this)
            binding.textHoraAlarma.setTextColor(colorTextDefault)
            obrirCalendariPerSeleccionarHora(binding.horaAlarma.text.toString())
        }
        binding.horaAlarma.setOnFocusChangeListener(){ _, hasFocus->
            hideKeyboard(this@AfegirAlarmaActivity)

            if(!primerOnCreate) {
                if (hasFocus) {
                    binding.textHoraAlarma.setTextColor(colorTextDefault)
                    obrirCalendariPerSeleccionarHora(binding.horaAlarma.text.toString())
                }
            }else{
                /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
                primerOnCreate=false
            }

        }

    }

    private fun inicialitzarAlarma(binding: ActivityAfegirAlarmaBinding): Alarma {
        val hora = binding.horaAlarma.text.toString()
        return Alarma(0,hora)
    }

    private fun dataIHoraPerDefecte() {
        val cal = Calendar.getInstance()
        var hora = cal.get(Calendar.HOUR_OF_DAY)
        var minuts = cal.get(Calendar.MINUTE)
        var horaString = hora.toString().padStart(2, '0')
        binding.horaAlarma.setText(
            "${hora.toString().padStart(2, '0')}:${
                minuts.toString().padStart(
                    2,
                    '0'
                )
            }"
        )
    }

    private fun validarEntrada(): Boolean {
        if(binding.horaAlarma.text == null || binding.horaAlarma.text.toString() == ""){
            binding.textHoraAlarma.setTextColor(Constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "L'hora és obligatoria per guardar l'alarma", Toast.LENGTH_SHORT).show()
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
                binding.horaAlarma.setText(
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

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun afegirAlarmaOK(idAlarmaManager: Int?) {

        // Guardem la alarma al AlarmManager
        /* Preferences per guardar dades en un xml local */
        preferences = applicationContext.getSharedPreferences("ControlDiabetic", MODE_PRIVATE)
        editor = preferences.edit()

        var parts = binding.horaAlarma.text.split(":")
        var hora =  parts[0].toInt()
        var minuts = parts[1].toInt()

        editor.putString("hour", hora.toString())
        editor.putString("minute", minuts.toString())

        var calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        var dataString = sdf.format(Date())
        parts = dataString.split("/")
        var day =  parts[0].toInt()
        var month = parts[1].toInt()-1
        var year = parts[2].toInt()
        calendar.set(year, month, day, hora, minuts, 0)

        //SAVE ALARM TIME TO USE IT IN CASE OF REBOOT
        editor.putInt("alarmID", idAlarmaManager!!)
        editor.putLong("alarmTime", calendar.timeInMillis)
        editor.commit()

        setAlarm(idAlarmaManager, calendar.timeInMillis, this)

        alert("S'ha guardat correctament l'alarma.", "Alarma guardada") {
            cancellable(false)
            positiveButton("Continuar") {
                setResult(RESULT_OK)
                finish()
            }
        }.show()
    }
    override fun afegirAlarmaNOK() {
        Toast.makeText(this, "Error al guardar l'alarma", Toast.LENGTH_SHORT).show()
        finish()
    }
    override fun jaExisteixAlarma() {
        alert("L'alarma introduida ja existeix", "Error al guardar l'alarma") {
            cancellable(false)
            positiveButton("Continuar") {
                setResult(RESULT_OK)
                finish()
            }
        }.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun noExisteixAlarma() {}
    override fun modificarAlarmaOK(idAlarmaManager: Int?) {}
    override fun modificarAlarmaNOK() {}
    override fun llistaAlarmesOK(llistaAlarmes: ArrayList<AlarmaAmbId>) {}
    override fun llistaAlarmesNOK() {}
    override fun recuperarIdAlarmaNovaOK(idAlarma: Int) {}
    override fun recuperarIdAlarmaNovaNOK() {}

}