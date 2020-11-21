package com.ismasoft.controldiabetic.viewModel

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*
import com.ismasoft.controldiabetic.data.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    /** Funció que s'executa al apretar el botó de fer login a l'aplicació
     *   @param user - email de l'usuari que es registre
     *   @param pass - contrasenya **/
    fun onButtonLoginClicked(user: String, pass: String) {
        // inicialitzem el valor del missatge de resposta.
        _message.value = ""

        viewModelScope.launch {
            _progressVisibility.value = true
            if (user.isEmpty()) {
                _message.value = "No s'ha introduit el correu electrònic"
            } else if (pass.isEmpty()) {
                _message.value = "No s'ha introduit la contrasenya"
            } else if (user.isEmpty() || pass.isEmpty()) {
                _message.value = "No s'ha introduit correu electrònic ni contrasenya"
            } else {
                repository.requestLogin(user, pass)
                _message.value = withContext(Dispatchers.IO) {
                    Thread.sleep(2000)
                    if(_logged.value == true){
                        "Succes"
                    }else{
                        "Failure"
                    }
                }
            }
            _progressVisibility.value = false
        }
    }
}