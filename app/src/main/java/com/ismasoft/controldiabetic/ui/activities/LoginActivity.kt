package com.ismasoft.controldiabetic.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.data.repository.LoginRepositoryInterface
import com.ismasoft.controldiabetic.databinding.ActivityLoginBinding
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.utilities.Constants.RETORN_ACTIVITY_OK_CODE
import com.ismasoft.controldiabetic.utilities.hideKeyboard
import com.ismasoft.controldiabetic.viewModel.LoginViewModel

class LoginActivity : AppCompatActivity(), LoginRepositoryInterface {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private var constants: Constants = Constants
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var colorHintUser : ColorStateList
    private lateinit var colorHintPass : ColorStateList

    /** Funció que es crea al accedir a l'activitat / vista Login **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        /* Preferences per guardar dades en un xml local */
        preferences = applicationContext.getSharedPreferences("ControlDiabetic", MODE_PRIVATE)
        editor = preferences.edit()

        colorHintUser = binding.username.hintTextColors
        colorHintPass = binding.password.hintTextColors

        val loginRepositoryInterface : LoginRepositoryInterface = this

        recuperarUsuariDelPreference(binding,loginRepositoryInterface)

        with(binding){

            /* Funció Login */
            login.setOnClickListener {
                /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
                hideKeyboard(this@LoginActivity)

                // Bloquejem que es permeti fer clics durant el proces
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                viewModel?.onButtonLoginClicked(username.text.toString(),password.text.toString(),loginRepositoryInterface)
            }

            /* Funció de registre */
            registrarse.setOnClickListener {
                viewModel?.amagarRecuperarPass()
                /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
                hideKeyboard(this@LoginActivity)

                /* Accedim a la activitat de registreActivity */
                intent = Intent(applicationContext, RegistreActivity::class.java)
                startActivity(intent)
            }

            /* Recordar la contrasenya */
            recordarContrasenya.setOnClickListener(){
                intent = Intent(applicationContext, RecuperarContrasenyaActivity::class.java)
                startActivityForResult(intent, RETORN_ACTIVITY_OK_CODE)
            }

            username.setOnClickListener() {
                username.setHintTextColor(colorHintUser)
                password.setHintTextColor(colorHintPass)
            }
            username.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus) {
                    username.setHintTextColor(colorHintUser)
                    password.setHintTextColor(colorHintPass)
                }
            }
            password.setOnClickListener() {
                username.setHintTextColor(colorHintUser)
                password.setHintTextColor(colorHintPass)
            }
            password.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus){
                    username.setHintTextColor(colorHintUser)
                    password.setHintTextColor(colorHintPass)
                }
            }

            guardarUsuari.setOnClickListener() {
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
            editor.putString("checkGuardat", null)
            editor.apply()
            editor.putString("usuariGuardat", "")
            editor.commit()
            editor.putString("contrasenyaGuardada", "")
            editor.commit()
            editor.clear()
        }
    }

    /** Es recupera la informació del sharePreference i si no ens hem desgloguejat entrarem automàticament **/
    private fun recuperarUsuariDelPreference(
        binding: ActivityLoginBinding,
        loginRepositoryInterface: LoginRepositoryInterface
    ) {
        var checkGuardat = preferences.getString("checkGuardat", null)
        var usuariGuardat = preferences.getString("usuariGuardat", null)
        var contrasenyaGuardada = preferences.getString("contrasenyaGuardada", null)

        if(!checkGuardat.isNullOrEmpty()){
            binding.guardarUsuari.isChecked = true
        }
        if(!usuariGuardat.isNullOrEmpty()){
            binding.username.setText(usuariGuardat)
        }
        if(!contrasenyaGuardada.isNullOrEmpty()){
            binding.password.setText(contrasenyaGuardada)
        }
        if(binding.guardarUsuari.isChecked){
            viewModel?.onButtonLoginClicked(binding.username.text.toString(),binding.password.text.toString(),loginRepositoryInterface)
        }

    }

    /** Funció per tractar les validacions del Login **/
    private fun validacionsLogin() {

        when {
            constants.ERROR_FALTA_USUARI_I_CONTRASENYA == viewModel.message.value -> {
                binding.username.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
                binding.password.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            }
            constants.ERROR_FALTA_USUARI == viewModel.message.value -> {
                binding.username.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            }
            constants.ERROR_FALTA__CONTRASENYA == viewModel.message.value -> {
                binding.password.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            }
        }
    }

    override fun credencialsOK() {
        // Desbloquejem que no es permeti fer clics
        window.clearFlags(android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        intent = Intent(applicationContext, MenuPrincipalActivity::class.java)
        startActivity(intent)
        guardarUsuariAlPreference(binding)
    }

    override fun credencialsNOK() {
        Log.d("LoginActivity", "Les credencials no existeixen")
        // Desbloquejem que no es permeti fer clics
        window.clearFlags(android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        validacionsLogin()
        editor.putString("checkGuardat", null)
        editor.apply()
        editor.putString("usuariGuardat", "")
        editor.commit()
        editor.putString("contrasenyaGuardada", "")
        editor.commit()
        editor.clear()

    }

}