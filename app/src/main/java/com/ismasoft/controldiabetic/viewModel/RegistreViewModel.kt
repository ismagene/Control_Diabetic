package com.ismasoft.controldiabetic.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.ismasoft.controldiabetic.data.model.User
import com.ismasoft.controldiabetic.data.repository.RegistreRepository
import com.ismasoft.controldiabetic.data.repository.RegistreRepositoryInterface
import kotlinx.coroutines.*

class RegistreViewModel(application: Application) : AndroidViewModel(application), RegistreRepositoryInterface {

    // Definim el repository per accedir a la BBDD
    private var repository = RegistreRepository(application)
    private val _mailTrobat : MutableLiveData<Boolean>
    init {
        _mailTrobat = repository.mailTrobat
    }
    val mailTrobat : LiveData<Boolean> get() = _mailTrobat

    private val _message = MutableLiveData<String>()
    val message : LiveData<String> get() = _message

    private val registreInterface : RegistreRepositoryInterface = this
    val scope = CoroutineScope(Job() + Dispatchers.Main)

    suspend fun onButtonContinuarClicked(correuElectronic: String) {
        // inicialitzem el valor del missatge de resposta.
        _message.value = ""
        coroutineScope {
            val deferredOne = async {
                //Fer consulta ala BBDD si existeix l'email insertat.
                    repository.comprobarExisteixEmail(correuElectronic)
                    _message.value = withContext(Dispatchers.IO) {
                        if (_mailTrobat.value == true) {
                            "Succes"
                        } else {
                            "Failure"
                        }
                    }
            }
            deferredOne.await()
        }


    }

    fun onButtonRegistreClicked(usuari: User) : Boolean{
        repository.requestRegistreUsuari(usuari.correuElectronic.toString(), usuari.contrasenya.toString())
        repository.insertarUsuariBBDD()

        return true
    }

    override fun comprobarExisteixEmailOK() {
        _mailTrobat.value = true
    }

    override fun comprobarExisteixEmailNOK() {
        _mailTrobat.value = false
    }


}