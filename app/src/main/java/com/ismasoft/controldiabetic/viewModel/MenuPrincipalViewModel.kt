package com.ismasoft.controldiabetic.viewModel

import android.app.Application
import androidx.lifecycle.*

class MenuPrincipalViewModel (application: Application) : AndroidViewModel(application){

    /* Variables que recuperem directament des de la vista */
    private val _progressFinish = MutableLiveData<Boolean>()
    val progressFinish : LiveData<Boolean> get() = _progressFinish
    val message = MutableLiveData<String>()

     fun onButtonClicked (){

    }

}