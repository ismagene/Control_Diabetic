package com.ismasoft.controldiabetic.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.databinding.ActivityLoginBinding
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.viewModel.LoginViewModel
import kotlinx.coroutines.*


class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private var constants: Constants = Constants

    private lateinit var colorHint : ColorStateList
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    /** Funció que es crea al accedir a l'activitat / vista Login **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val scope = CoroutineScope(Job() + Dispatchers.Main)

        /* Preferences per guardar dades en un xml local */
        preferences = applicationContext.getSharedPreferences("ControlDiabetic", MODE_PRIVATE)
        editor = preferences.edit()

        recuperarUsuariDelPreference(binding)

        with(binding){

            /* Funció Login */
            login.setOnClickListener {
                /* Deseleccionem els valors dels editText perquè s'amagui el teclat i desabilitar els botons perquè fem una crida amb coroutine al Login*/
                login.isEnabled = false
                registrarse.isEnabled = false

                /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(username.windowToken, 0)

                scope.launch {
                    val deferred = async {
                        viewModel?.onButtonLoginClicked(
                            username.text.toString(),
                            password.text.toString()
                        )
                    }
                    deferred.await()

                    validacionsLogin()

                    /* Si el valor logged es true, es que s'ha fet login correctament i anirem al menú principal */
                    if (viewModel?.logged?.value == true) {
                        intent = Intent(applicationContext, MenuPrincipalActivity::class.java)
                        startActivity(intent)
                        guardarUsuariAlPreference(binding)
                    }
                    editor.clear()
                    login.isEnabled = true
                    registrarse.isEnabled = true
                }
            }

            /* Funció de registre */
            registrarse.setOnClickListener{
                /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(username.windowToken, 0)

                /* Accedim a la activitat de registreActivity */
                intent = Intent(applicationContext, RegistreActivity::class.java)
                startActivity(intent)
            }

            username.setOnClickListener(){
//                if(viewModel?.message?.equals("") == false) {
//                    username.setHintTextColor(colorHint)
//                    password.setHintTextColor(colorHint)
//                }
            }

            password.setOnClickListener(){
//                if(viewModel?.message?.equals("") == false) {
//                    username.setHintTextColor(colorHint)
//                    password.setHintTextColor(colorHint)
//                }
            }

            guardarUsuari.setOnClickListener(){
                /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(username.windowToken, 0)
            }
        }

    }

    private fun guardarUsuariAlPreference(binding: ActivityLoginBinding) {
        if(binding.guardarUsuari.isChecked){
            editor.putString("checkGuardat", "True")
            editor.apply()
            editor.putString("usuariGuardat", binding.username.text.toString())
            editor.commit()
            editor.putString("contrasenyaGuardada", binding.password.text.toString())
            editor.commit()
        }
        else{
            editor.clear()
        }
    }

    private fun recuperarUsuariDelPreference(binding: ActivityLoginBinding) {
        var checkGuardat = preferences.getString("checkGuardat", null)
        var usuariGuardat = preferences.getString("usuariGuardat",null)
        var contrasenyaGuardada = preferences.getString("contrasenyaGuardada",null)

        if(!checkGuardat.isNullOrEmpty()){
            binding.guardarUsuari.isChecked = true
        }
        if(!usuariGuardat.isNullOrEmpty()){
            binding.username.setText(usuariGuardat)
        }
        if(!contrasenyaGuardada.isNullOrEmpty()){
            binding.password.setText(contrasenyaGuardada)
        }
        /* Accedim a la activitat de registreActivity */
//        intent = Intent(applicationContext, MenuPrincipalActivity::class.java)
//        startActivity(intent)
    }

    /** Funció per tractar les validacions del Login **/
    private fun validacionsLogin(){
        when {
            constants.ERROR_FALTA_USUARI_I_CONTRASENYA == viewModel.message.value -> {
                colorHint=binding.username.hintTextColors
                binding.username.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
                binding.password.setHintTextColor(Color.rgb(240, 2, 3))
            }
            constants.ERROR_FALTA_USUARI == viewModel.message.value -> {
                colorHint=binding.username.hintTextColors
                binding.username.setHintTextColor(Color.rgb(240, 2, 3))
            }
            constants.ERROR_FALTA__CONTRASENYA == viewModel.message.value -> {
                colorHint=binding.username.hintTextColors
                binding.password.setHintTextColor(Color.rgb(240, 2, 3))
            }
        }
    }

}