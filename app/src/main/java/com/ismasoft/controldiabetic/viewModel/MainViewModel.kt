package com.ismasoft.controldiabetic.viewModel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainViewModel (application: Application) : AndroidViewModel(application){

    /* Variables que recuperem directament des de la vista */
    private val _progressFinish = MutableLiveData<Boolean>()
    val progressFinish : LiveData<Boolean> get() = _progressFinish

    val message = MutableLiveData<String>()

    suspend fun onButtonClicked (){
        coroutineScope  {
            val deferredOne = async {
                _progressFinish.value = true
                // Funcions varies inicials de l'aplicació
                Thread.sleep(1000)
            }
            deferredOne.await()
        }
    }

}