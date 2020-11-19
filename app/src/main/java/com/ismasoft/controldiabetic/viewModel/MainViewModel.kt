package com.ismasoft.controldiabetic.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel(){

    private val _progressVisibility = MutableLiveData<Boolean>()
//    val progressVisibility = LiveData<Boolean> get() = _progressVisibility

    val message = MutableLiveData<String>()

    fun onButtonClicked (user: String, pass: String){
        viewModelScope.launch {
            _progressVisibility.value = true
//            message.value =


            _progressVisibility.value = false
        }
    }

}