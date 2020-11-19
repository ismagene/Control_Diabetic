package com.ismasoft.controldiabetic.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel(){

//    private lateinit var appRepository:LoginRepository
//    private val userMutableLiveData : MutableLiveData<FirebaseUser>()

    private val _progressVisibility = MutableLiveData<Boolean>()
    val progressVisibility : LiveData<Boolean> get() = _progressVisibility

    private val _message = MutableLiveData<String>()
    val message : LiveData<String> get() = _message

    fun onButtonClicked(user: String, pass: String){
        viewModelScope.launch {
            _progressVisibility.value = true
            _message.value = withContext(Dispatchers.IO){
                Thread.sleep(2000)
                if (user.isNotEmpty() && pass.isNotEmpty()) "Succes" else "Failure"
            }
            _progressVisibility.value = false
        }
//        _progressVisibility.value = false
//        _progressVisibility.value = _progressVisibility.value != true
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