package com.ismasoft.controldiabetic.viewModel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainViewModel (application: Application) : AndroidViewModel(application){

    /* Variables que recuperem directament des de la vista */
    private val _progressFinish = MutableLiveData<Boolean>(true)
    val progressFinish : LiveData<Boolean> get() = _progressFinish

    suspend fun onButtonClicked (){
        coroutineScope  {
            launch {
                // Funcions varies inicials de l'aplicació
                Thread.sleep(2500)
                _progressFinish.value = false
            }
        }
    }

}