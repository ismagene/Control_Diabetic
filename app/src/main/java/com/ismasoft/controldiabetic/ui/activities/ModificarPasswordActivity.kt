package com.ismasoft.controldiabetic.ui.activities

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.repository.PerfilRepositoryInterface
import com.ismasoft.controldiabetic.databinding.ActivityModificarPasswordBinding
import com.ismasoft.controldiabetic.utilities.Constants.COLOR_ERROR_FALTA_CAMP
import com.ismasoft.controldiabetic.viewModel.PerfilViewModel
import org.jetbrains.anko.alert


class ModificarPasswordActivity : AppCompatActivity(), PerfilRepositoryInterface {

    private lateinit var viewModel: PerfilViewModel
    private lateinit var binding: ActivityModificarPasswordBinding
    private lateinit var colorHintDefault : ColorStateList
    private lateinit var colorTextDefault : ColorStateList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_contrasenya)

        binding = ActivityModificarPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Guardem els colors del hint i del text per defecte
        colorHintDefault = binding.contrasenyaAntiga.hintTextColors
        colorTextDefault = binding.contrasenyaAntiga.textColors

        // Per tornar endarrera
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.buttonRecuperar.setOnClickListener(){

            if(validarEntrada()){
                viewModel.validarContrasenya(binding.contrasenyaAntiga.text.toString(),this)
            }
        }
        binding.contrasenyaAntiga.setOnClickListener(){
            binding.contrasenyaAntiga.setHintTextColor(colorHintDefault)
            binding.contrasenyaAntiga.setTextColor(colorTextDefault)
        }
        binding.contrasenyaAntiga.setOnFocusChangeListener(){ _,hasFocus ->
            if(hasFocus) {
                binding.contrasenyaAntiga.setHintTextColor(colorHintDefault)
                binding.contrasenyaAntiga.setTextColor(colorTextDefault)
            }
        }
        binding.contrasenyaNova.setOnClickListener(){
            binding.contrasenyaNova.setHintTextColor(colorHintDefault)
            binding.contrasenyaNova.setTextColor(colorTextDefault)
        }
        binding.contrasenyaNova.setOnFocusChangeListener(){ _,hasFocus ->
            if(hasFocus) {
                binding.contrasenyaNova.setHintTextColor(colorHintDefault)
                binding.contrasenyaNova.setTextColor(colorTextDefault)
            }
        }
        binding.contrasenyaNovaConfirmar.setOnClickListener(){
            binding.contrasenyaNovaConfirmar.setHintTextColor(colorHintDefault)
            binding.contrasenyaNovaConfirmar.setTextColor(colorTextDefault)
        }
        binding.contrasenyaNovaConfirmar.setOnFocusChangeListener(){ _,hasFocus ->
            if(hasFocus) {
                binding.contrasenyaNovaConfirmar.setHintTextColor(colorHintDefault)
                binding.contrasenyaNovaConfirmar.setTextColor(colorTextDefault)
            }
        }
    }

    private fun validarEntrada(): Boolean {
        if(binding.contrasenyaAntiga.text == null || binding.contrasenyaAntiga.text.toString() == ""){
            binding.contrasenyaAntiga.setHintTextColor(COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "La contrasenya actual és obligatoria", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.contrasenyaAntiga.text.length < 6){
            binding.contrasenyaAntiga.setTextColor(COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this,"La contrasenya ha de ser mínim de 6 caracters",Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.contrasenyaNova.text == null || binding.contrasenyaNova.text.toString() == ""){
            binding.contrasenyaNova.setHintTextColor(COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "La contrasenya nova és obligatoria", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.contrasenyaNova.text.length < 6) {
            binding.contrasenyaNova.setTextColor(COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this,"La contrasenya ha de ser mínim de 6 caracters",Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.contrasenyaNovaConfirmar.text == null || binding.contrasenyaNovaConfirmar.text.toString() == "" ){
            binding.contrasenyaNovaConfirmar.setHintTextColor(COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "La contrasenya de confirmació és obligatoria", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.contrasenyaNovaConfirmar.text.length < 6){
            binding.contrasenyaNovaConfirmar.setTextColor(COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this,"La contrasenya ha de ser mínim de 6 caracters",Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.contrasenyaNova.text.toString() != binding.contrasenyaNovaConfirmar.text.toString()){
            binding.contrasenyaNovaConfirmar.setTextColor(COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "La confirmació de la contrasenya no és igual a la contrasenya", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


    override fun recuperarDadesPersonalsOK(document: DocumentSnapshot?) {}
    override fun recuperarDadesPersonalsNOK() {}
    override fun modificarDadesPersOK() {}
    override fun modificarDadesPersNOK() {}

    override fun validarContrasenyaOK() {
        viewModel.modificarContrasenya(binding.contrasenyaNova.text.toString(),this)
    }

    override fun validarContrasenyaNOK() {
        Toast.makeText(this, "La contrasenya actual és incorrecte.", Toast.LENGTH_SHORT).show()
    }

    override fun modificarContrasenyaOK() {

        // ALERTA I ACABAR OK
        alert {
            title = "Contrasenya modificada"
            message("S'ha modificat correctament la contrasenya.")
            cancellable(false)
            positiveButton("Continuar") {
                setResult(RESULT_OK)
                finish()
            }
        }.show()

    }

    override fun modificarContrasenyaNOK() {
        Toast.makeText(this, "Error al modificar la contrasenya.", Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}