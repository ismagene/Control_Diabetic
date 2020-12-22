package com.ismasoft.controldiabetic.viewModel

import android.app.Application
import androidx.lifecycle.*

class MenuPrincipalViewModel (application: Application) : AndroidViewModel(application){

    /* Variables que recuperem directament des de la vista */
    private val _progressFinish = MutableLiveData<Boolean>()
    val progressFinish : LiveData<Boolean> get() = _progressFinish

    // Clase preparada per el cas d'haver d'utilitzar databinding en el menú principal amb tots els fragments.

}