package com.ismasoft.controldiabetic.ui.activities

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.repository.RegistreRepositoryInterface
import com.ismasoft.controldiabetic.databinding.ActivityRecuperarContrasenyaBinding
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.viewModel.RegistreViewModel
import org.jetbrains.anko.alert

class RecuperarContrasenyaActivity : AppCompatActivity(), RegistreRepositoryInterface {

    private lateinit var viewModel: RegistreViewModel
    private lateinit var binding: ActivityRecuperarContrasenyaBinding
    private lateinit var colorHintDefault : ColorStateList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_contrasenya)

        binding = ActivityRecuperarContrasenyaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Guardem els colors del hint i del text per defecte
        colorHintDefault = binding.correuElectronic.hintTextColors

        // Per tornar endarrera
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.buttonRecuperar.setOnClickListener(){
            if(validarEntrada()){
                viewModel.onButtonContinuarClicked(binding.correuElectronic.text.toString(),this)
            }
        }

        binding.correuElectronic.setOnClickListener(){
            binding.correuElectronic.setHintTextColor(colorHintDefault)
        }
        binding.correuElectronic.setOnFocusChangeListener(){ _,hasFocus ->
            if(hasFocus){
                binding.correuElectronic.setHintTextColor(colorHintDefault)
            }
        }

    }

    private fun validarEntrada(): Boolean {
        if(binding.correuElectronic.text == null || binding.correuElectronic.text.toString() == ""){
            binding.correuElectronic.setHintTextColor(Constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El correu electrònic és obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun comprobarExisteixEmailOK() {
        // Tractar la recuperació.
        viewModel.restaurarContrasenya(binding.correuElectronic.text.toString(),this)
    }

    override fun comprobarExisteixEmailNOK() {
        Toast.makeText(this, "El correu electrònic no existeix a l'aplicació", Toast.LENGTH_SHORT).show()
    }

    override fun registreOK() {

        alert {
            title = "Restauració de la contrasenya realitzada"
            message("S'ha enviat un correu per recuperar la contrasenya.")
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

    override fun registreNOK() {
        Toast.makeText(this, "Error al restaurar la contrasenya", Toast.LENGTH_SHORT).show()
    }
    override fun registreInsertarOK() {}
    override fun registreInsertarNOK() {}
}