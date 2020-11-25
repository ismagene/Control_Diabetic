package com.ismasoft.controldiabetic.ui.activities

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
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

    /** Funció que es crea al accedir a l'activitat / vista Login **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val scope = CoroutineScope(Job() + Dispatchers.Main)

        with(binding){

            /* Funció Login */
            login.setOnClickListener {
                /* Deseleccionem els valors dels editText perquè s'amagui el teclat i desabilitar els botons perquè fem una crida amb coroutine al Login*/
                login.isEnabled = false
                registrarse.isEnabled = false

                /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.username.windowToken, 0)

                scope.launch {
                    val deferred = async {
                        viewModel?.onButtonLoginClicked(binding.username.text.toString(), binding.password.text.toString())
                    }
                    deferred.await()

                    validacionsLogin()

                    if (viewModel?.logged?.value == true) {
                        intent = Intent(applicationContext, MenuPrincipalActivity::class.java)
                        startActivity(intent)
                    }
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
                if(viewModel?.message?.equals("") == false) {
                    username.setHintTextColor(colorHint)
                    password.setHintTextColor(colorHint)
                }
            }

            password.setOnClickListener(){
                if(viewModel?.message?.equals("") == false) {
                    username.setHintTextColor(colorHint)
                    password.setHintTextColor(colorHint)
                }
            }
        }

    }


    /** Funció per tractar les validacions del Login **/
    private fun validacionsLogin(){
        when {
            constants.ERROR_FALTA_USUARI_I_CONTRASENYA == viewModel.message.value -> {
                colorHint=binding.username.hintTextColors
                binding.username.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
                binding.password.setHintTextColor(Color.rgb(240,2,3))
            }
            constants.ERROR_FALTA_USUARI == viewModel.message.value -> {
                colorHint=binding.username.hintTextColors
                binding.username.setHintTextColor(Color.rgb(240,2,3))
            }
            constants.ERROR_FALTA__CONTRASENYA == viewModel.message.value -> {
                colorHint=binding.username.hintTextColors
                binding.password.setHintTextColor(Color.rgb(240,2,3))
            }
        }
    }

}