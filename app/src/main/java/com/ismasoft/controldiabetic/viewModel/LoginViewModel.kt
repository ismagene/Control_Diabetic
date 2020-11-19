package com.ismasoft.controldiabetic.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel(){

//    private lateinit var appRepository:LoginRepository
//    private val userMutableLiveData : MutableLiveData<FirebaseUser>()

    private var _progressVisibility = MutableLiveData<Boolean>()
    val progressVisibility : LiveData<Boolean> get() = _progressVisibility

    fun onButtonClicked(user: String, pass: String){
        viewModelScope.launch {
            _progressVisibility.value = true
            Thread.sleep(2000)
//           if (user.isNotEmpty() && pass.isNotEmpty()) "Succes" else "Failure"
        }
        _progressVisibility.value = false
    }





//    private val _progressVisibility = MutableLiveData<Boolean>()
//    val progressVisibility = LiveData<Boolean> get() = _progressVisibility
//
//    val message = MutableLiveData<String>()
//
//    fun onButtonClicked (user: String, pass: String){
//        viewModelScope.launch {
//            _progressVisibility.value = true
//            message.value =
//
//
//            _progressVisibility.value = false
//        }
//    }

}