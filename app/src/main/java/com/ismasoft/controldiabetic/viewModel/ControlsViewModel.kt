package com.ismasoft.controldiabetic.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.ismasoft.controldiabetic.data.model.User
import com.ismasoft.controldiabetic.data.repository.LoginRepositoryInterface
import com.ismasoft.controldiabetic.data.repository.RegistreRepository
import com.ismasoft.controldiabetic.data.repository.RegistreRepositoryInterface

class ControlsViewModel(application: Application) : AndroidViewModel(application){

    // Definim el repository per accedir a la BBDD
    private var repository = RegistreRepository(application)

    fun onButtonContinuarClicked(correuElectronic: String, registreRepositoryInterface : RegistreRepositoryInterface) {
        // inicialitzem el valor del missatge de resposta.
        //Fer consulta ala BBDD si existeix l'email insertat.
        repository.comprobarExisteixEmail(correuElectronic, registreRepositoryInterface)
    }

    fun onButtonRegistreClicked(usuari: User, registreRepositoryInterface : RegistreRepositoryInterface) {
        repository.requestRegistreUsuari(usuari.correuElectronic.toString(), usuari.contrasenya.toString(),registreRepositoryInterface)
    }

    fun registreUsuariABBDD(usuari: User, registreRepositoryInterface : RegistreRepositoryInterface){
        repository.insertarUsuariBBDD(usuari,registreRepositoryInterface)
    }

}