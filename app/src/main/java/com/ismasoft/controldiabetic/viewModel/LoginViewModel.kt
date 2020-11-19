package com.ismasoft.controldiabetic.viewModel

import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.*
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.ui.activities.LoginActivity
import com.ismasoft.controldiabetic.ui.activities.RegistreActivity
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

    /** Funció que s'executa al apretar el botó de fer login a l'aplicació
     *   @param user - email de l'usuari que es registre
     *   @param pass - contrasenya **/
    fun onButtonLoginClicked(user: String, pass: String){

        viewModelScope.launch {
            _progressVisibility.value = true
            _message.value = withContext(Dispatchers.IO){
                Thread.sleep(2000)
                if (user.isNotEmpty() && pass.isNotEmpty()) "Succes" else "Failure"
            }
            _progressVisibility.value = false
        }
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