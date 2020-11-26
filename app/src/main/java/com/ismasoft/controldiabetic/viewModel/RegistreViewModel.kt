package com.ismasoft.controldiabetic.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.ismasoft.controldiabetic.data.model.User
import com.ismasoft.controldiabetic.data.repository.LoginRepository
import com.ismasoft.controldiabetic.data.repository.RegistreRepository
import com.ismasoft.controldiabetic.databinding.ActivityRegistre2Binding
import com.ismasoft.controldiabetic.databinding.ActivityRegistreBinding

class RegistreViewModel(application: Application) : AndroidViewModel(application){

    // Definim el repository per accedir a la BBDD
    private var repository = RegistreRepository(application)

     /** Funció que s'executa al apretar continuar en el registre d'usuari
    *   @param user - email de l'usuari que es registre
     *   @param pass **/
    fun onButtonContinuarClicked(usuari: User) : Boolean{
            // validacions de la primera pantalla
            if (usuari?.nom?.isEmpty() == true) {
                return false
            }else if(usuari?.primerCognom?.isEmpty() == true){
                return false
            }

         return true
    }

    fun onButtonRegistreClicked(usuari: User) : Boolean{

        repository.requestRegistreUsuari(usuari.correuElectronic.toString(), usuari.contrasenya.toString())
        repository.insertarUsuariBBDD()

        return true
    }



}