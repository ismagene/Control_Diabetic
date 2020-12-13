package com.ismasoft.controldiabetic.ui.activities

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.data.model.ControlAmbId
import com.ismasoft.controldiabetic.databinding.ActivityEnviarHistoricBinding
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.utilities.JavaMailAPI
import com.ismasoft.controldiabetic.utilities.hideKeyboard
import com.ismasoft.controldiabetic.viewModel.ControlsViewModel
import org.jetbrains.anko.alert
import java.text.SimpleDateFormat
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Transport
import javax.mail.internet.InternetAddress

class EnviarHistoricActivity : AppCompatActivity() {

    private lateinit var viewModel: ControlsViewModel
    private lateinit var binding: ActivityEnviarHistoricBinding

    var llistaControlsFiltrats = ArrayList<ControlAmbId>()

    private lateinit var colorTextDefault : ColorStateList
    private lateinit var colorValorDefault : ColorStateList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var objetoIntent : Intent = intent

        // Per tornar endarrera
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Recuperem els valors enviats del fragment de controls
        llistaControlsFiltrats = objetoIntent.extras?.get("llistaControlsFiltrats") as ArrayList<ControlAmbId>

        binding = ActivityEnviarHistoricBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Guardem els colors del valor i del text per defecte
        colorTextDefault = binding.textCorreuElectronicAenviar.textColors
        colorValorDefault = binding.correuElectronicAEnviar.textColors


        binding.enviarCorreuElectronic.setOnClickListener(){
            hideKeyboard(this)

            if(validarEntrada()) {

                val mEmail: String = binding.correuElectronicAEnviar.text.toString()
                val mSubject: String = "Control diabètic - Històric de controls de glucosa "
                var mMessage: String = "Històric de controls de glucosa: \n"

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

                alert(
                    "S'ha enviat l'historial de controls al correu introduit.",
                    "Històric enviat"
                ) {
                    cancellable(false)
                    positiveButton("Continuar") {
                        setResult(RESULT_OK)
                        finish()
                    }
                }.show()
            }
        }

        binding.cancelarEnviarCorreuElectronic.setOnClickListener(){
            /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
            hideKeyboard(this)
            finish()
        }

        binding.correuElectronicAEnviar.setOnClickListener(){
            binding.textCorreuElectronicAenviar.setTextColor(colorTextDefault)
            binding.correuElectronicAEnviar.setTextColor(colorValorDefault)
        }
        binding.correuElectronicAEnviar.setOnFocusChangeListener(){ _, hasFocus->
            if (hasFocus) {
                binding.textCorreuElectronicAenviar.setTextColor(colorTextDefault)
                binding.correuElectronicAEnviar.setTextColor(colorValorDefault)
            }
        }

    }

    private fun validarEntrada(): Boolean {

        if(binding.correuElectronicAEnviar.text == null || binding.correuElectronicAEnviar.text.toString() == ""){
            binding.textCorreuElectronicAenviar.setTextColor(Constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "És obligatori escriure un mail correcte per enviar l'històric.", Toast.LENGTH_SHORT).show()
            return false
        }else {
            var mailCorrecte =
                android.util.Patterns.EMAIL_ADDRESS.matcher(binding.correuElectronicAEnviar.text.toString())
                    .matches()
            if (!mailCorrecte) {
                binding.correuElectronicAEnviar.setTextColor(Constants.COLOR_ERROR_FALTA_CAMP)
                Toast.makeText(
                    this,
                    "El format del correu electrònic no es correcte",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}