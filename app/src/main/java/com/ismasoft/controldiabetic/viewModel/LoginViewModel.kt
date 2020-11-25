package com.ismasoft.controldiabetic.viewModel

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*
import com.ismasoft.controldiabetic.data.repository.LoginRepository
import com.ismasoft.controldiabetic.utilities.Constants
import kotlinx.coroutines.*

class LoginViewModel(application: Application) : AndroidViewModel(application){
    // Definim el repository per accedir a la BBDD
    private var repository = LoginRepository(application)
    private val _logged : MutableLiveData<Boolean>
    init {
        _logged = repository.logged
    }
    val logged : LiveData<Boolean> get() = _logged

    /* Variables que recuperem directament des de la vista */
    private val _progressVisibility = MutableLiveData<Boolean>()
    val progressVisibility : LiveData<Boolean> get() = _progressVisibility
    private val _message = MutableLiveData<String>()
    val message : LiveData<String> get() = _message

    private val constants = Constants

    /** Funció que s'executa al apretar el botó de fer login a l'aplicació
     *   @param user - email de l'usuari que es registre
     *   @param pass - contrasenya **/
    suspend fun onButtonLoginClicked(user: String, pass: String) {
        // inicialitzem el valor del missatge de resposta.
        _message.value = ""
        coroutineScope {
            val deferredOne = async {
                _progressVisibility.value = true
                if (validacionsEntrada(user, pass)) {
                    repository.requestLogin(user, pass)
                    _message.value = withContext(Dispatchers.IO) {
                        Thread.sleep(2000)
                        if (_logged.value == true) {
                            "Succes"
                        } else {
                            "Failure"
                        }
                    }
                }
                _progressVisibility.value = false
            }
            deferredOne.await()
        }
    }

    /** Funció que s'executa al apretar el botó de fer login a l'aplicació
     *   @param user - email de l'usuari que es registre
     *   @param pass - contrasenya **/
    private fun validacionsEntrada(user: String, pass: String) : Boolean {

        if (user.isEmpty() && pass.isEmpty()) {
            _message.value = constants.ERROR_FALTA_USUARI_I_CONTRASENYA
            return false
        } else if (user.isEmpty()) {
            _message.value = constants.ERROR_FALTA_USUARI
            return false
        } else if (pass.isEmpty()) {
            _message.value = constants.ERROR_FALTA__CONTRASENYA
            return false
        }
        return true
    }

}

