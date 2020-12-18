package com.ismasoft.controldiabetic.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.ismasoft.controldiabetic.data.repository.LoginRepository
import com.ismasoft.controldiabetic.data.repository.interfaces.LoginRepositoryInterface
import com.ismasoft.controldiabetic.utilities.Constants

class LoginViewModel(application: Application) : AndroidViewModel(application),
    LoginRepositoryInterface {
    // Definim el repository per accedir a la BBDD
    private var repository = LoginRepository(application)
    lateinit var loginActivityInstance : LoginRepositoryInterface

    /* Variables que recuperem directament des de la vista */
    private val _progressVisibility = MutableLiveData<Boolean>()
    val progressVisibility : LiveData<Boolean> get() = _progressVisibility
    private val _message = MutableLiveData<String>()
    val message : LiveData<String> get() = _message
    private val _recuperarPass = MutableLiveData<Boolean>(false)
    val recuperarPass : LiveData<Boolean> get() = _recuperarPass

    private val constants = Constants

    /** Funció que s'executa al apretar el botó de fer login a l'aplicació
     *   @param user - email de l'usuari que es registre
     *   @param pass - contrasenya **/
    fun onButtonLoginClicked(user: String, pass: String, loginRepositoryInterface : LoginRepositoryInterface) {
        // inicialitzem el valor del missatge de resposta.
        _progressVisibility.value = true
        loginActivityInstance = loginRepositoryInterface
        if (validacionsEntrada(user, pass)) {
            repository.requestLogin(user, pass, this)
        }
    }

    /** Funció que s'executa al apretar el botó de fer login a l'aplicació
     *   @param user - email de l'usuari que es registre
     *   @param pass - contrasenya **/
    private fun validacionsEntrada(user: String, pass: String) : Boolean {

        if (user.isEmpty() && pass.isEmpty()) {
            _message.value = constants.ERROR_FALTA_USUARI_I_CONTRASENYA
            _progressVisibility.value = false
            loginActivityInstance.credencialsNOK()
            return false
        } else if (user.isEmpty()) {
            _message.value = constants.ERROR_FALTA_USUARI
            _progressVisibility.value = false
            loginActivityInstance.credencialsNOK()
            return false
        } else if (pass.isEmpty()) {
            _message.value = constants.ERROR_FALTA__CONTRASENYA
            _progressVisibility.value = false
            loginActivityInstance.credencialsNOK()
            return false
        }

        return true
    }

    override fun credencialsOK() {
        _progressVisibility.value = false
        _message.value = ""
        loginActivityInstance.credencialsOK()
    }

    override fun credencialsNOK() {
        _progressVisibility.value = false
        _message.value = "Error al fer login"
        loginActivityInstance.credencialsNOK()
        _recuperarPass.value = true
    }

    fun amagarRecuperarPass() {
        _recuperarPass.value = false
    }

}

