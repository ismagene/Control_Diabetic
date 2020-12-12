package com.ismasoft.controldiabetic.ui.activities

import android.app.TimePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.model.AlarmaAmbId
import com.ismasoft.controldiabetic.data.repository.AlarmesRepositoryInterface
import com.ismasoft.controldiabetic.databinding.ActivityModificarAlarmaBinding
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.utilities.hideKeyboard
import com.ismasoft.controldiabetic.utilities.setAlarm
import com.ismasoft.controldiabetic.viewModel.AlarmesViewModel
import org.jetbrains.anko.alert
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ModificarAlarmaActivity : AppCompatActivity() , AlarmesRepositoryInterface {

    private lateinit var viewModel: AlarmesViewModel
    private lateinit var binding: ActivityModificarAlarmaBinding

    private lateinit var colorTextDefault : ColorStateList
    private var primerOnCreate : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modificar_alarma)

        binding = ActivityModificarAlarmaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Guardem els colors del hint i del text per defecte
        colorTextDefault = binding.horaAlarma.textColors

        // Per tornar endarrera
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val objetoIntent : Intent = intent
        val alarmaModificar : AlarmaAmbId = objetoIntent.extras?.get("alarmaModificar") as AlarmaAmbId

        recuperarDadesAlarma(alarmaModificar)

        binding.modificarAlarma.setOnClickListener(){
            /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
            hideKeyboard(this)

            if(validarEntrada(alarmaModificar)){
                var alarma = inicialitzarAlarma(binding, alarmaModificar)
                viewModel.onButtonModificarAlarma(alarma, this)
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
            if (hasFocus) {
                binding.textHoraAlarma.setTextColor(colorTextDefault)
                obrirCalendariPerSeleccionarHora(binding.horaAlarma.text.toString())
            }
            hideKeyboard(this@ModificarAlarmaActivity)
        }
    }

    private fun recuperarDadesAlarma(alarmaModificar: AlarmaAmbId) {
        binding.horaAlarma.setText(alarmaModificar.horaAlarma.toString())
    }

    private fun inicialitzarAlarma(binding: ActivityModificarAlarmaBinding, alarmaModificar: AlarmaAmbId
    ): AlarmaAmbId {
        val hora = binding.horaAlarma.text.toString()
        return AlarmaAmbId(alarmaModificar.idAlarma.toString(),alarmaModificar.idAlarmaManager, hora)
    }

    private fun validarEntrada(alarmaModificar: AlarmaAmbId): Boolean {
        if(binding.horaAlarma.text == null || binding.horaAlarma.text.toString() == ""){
            binding.textHoraAlarma.setTextColor(Constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "L'hora és obligatoria per guardar l'alarma", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.horaAlarma.text.toString() == alarmaModificar.horaAlarma.toString()){
            Toast.makeText(this, "No s'ha modificat l'hora de l'alarma", Toast.LENGTH_SHORT).show()
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
            this@ModificarAlarmaActivity,
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun modificarAlarmaOK(idAlarmaManager: Int?) {

        // Guardem la alarma al AlarmManager
        /* Preferences per guardar dades en un xml local */
        preferences = applicationContext.getSharedPreferences("ControlDiabetic", MODE_PRIVATE)
        editor = preferences.edit()

        var parts = binding.horaAlarma.text.split(":")
        var hora =  parts[0].toInt()
        var minuts = parts[1].toInt()

        var calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        var dataString = sdf.format(Date())
        parts = dataString.split("/")
        var day =  parts[0].toInt()
        var month = parts[1].toInt()-1
        var year = parts[2].toInt()
        calendar.set(year, month, day, hora, minuts, 0)

        //guardem els valors de l'identificar de l'alarma i el temps al SharedPreference per recuperar-ho en cas de reboot
        editor.putInt("alarmID+${idAlarmaManager}", idAlarmaManager!!)
        editor.putLong("alarmTime+${idAlarmaManager}", calendar.timeInMillis)
        editor.commit()

        // Instanciem l'alarma
        setAlarm(idAlarmaManager, calendar.timeInMillis, this)

        alert("S'ha modificat correctament l'alarma.","Alarma modificada") {
            cancellable(false)
            positiveButton("Continuar") {
                setResult(RESULT_OK)
                finish()
            }
        }.show()
    }
    override fun modificarAlarmaNOK() {
        Toast.makeText(this, "Error al modificar l'alarma", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun afegirAlarmaOK(idAlarmaManager: Int?) {}
    override fun afegirAlarmaNOK() {}
    override fun jaExisteixAlarma() {
        alert("La nova alarma ja existeix","Error al modificar l'alarma") {
            cancellable(false)
            positiveButton("Continuar") {
                setResult(RESULT_OK)
            }
        }.show()
    }
    override fun noExisteixAlarma() {}
    override fun llistaAlarmesOK(llistaAlarmes: ArrayList<AlarmaAmbId>) {}
    override fun llistaAlarmesNOK() {}
    override fun recuperarIdAlarmaNovaOK(idAlarma: Int) { }
    override fun recuperarIdAlarmaNovaNOK() {}
}