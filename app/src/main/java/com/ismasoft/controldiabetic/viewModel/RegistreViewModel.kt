package com.ismasoft.controldiabetic.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.ismasoft.controldiabetic.data.repository.LoginRepository
import com.ismasoft.controldiabetic.data.repository.RegistreRepository
import com.ismasoft.controldiabetic.databinding.ActivityRegistreBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistreViewModel(application: Application) : AndroidViewModel(application){

    // Definim el repository per accedir a la BBDD
    private var repository = RegistreRepository(application)

    /** Funció que s'executa al apretar el botó de Registrar usuari
    *   @param user - email de l'usuari que es registre
     *   @param pass **/
    fun onButtonRegistreClicked(binding: ActivityRegistreBinding) {

        viewModelScope.launch {
            if (binding.loginEmail.text.toString().isEmpty()) {
//                _message.value = "No s'ha introduit el correu electrònic"
//            } else if (pass.isEmpty()) {
//                _message.value = "No s'ha introduit la contrasenya"
//            } else if (user.isEmpty() || pass.isEmpty()) {
//                _message.value = "No s'ha introduit correu electrònic ni contrasenya"
            } else {
                repository.requestRegistreUsuari(binding.loginEmail.text.toString(), binding.loginPassword.text.toString())
            }
        }
    }




}