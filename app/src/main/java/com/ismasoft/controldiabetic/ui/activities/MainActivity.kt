package com.ismasoft.controldiabetic.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.databinding.ActivityMainBinding
import com.ismasoft.controldiabetic.viewModel.MainViewModel
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val scope = CoroutineScope(Job() + Dispatchers.Main)

        scope.launch {
            val deferred = async {
                viewModel?.onButtonClicked()
            }
            deferred.await()
            // Comprobar que no s'hagi posat en sogon pla l'aplicació
            if (viewModel?.progressFinish?.value == true) {
                intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

        }
//        setContentView(R.layout.activity_main)

//        /** Funció que si apretem
//         *   @param user - email de l'usuari que es registre
//         *   @param pass - contrasenya **/
//        val textView = findViewById<TextView>(R.id.prova) as TextView
//        textView?.setOnClickListener {
//            intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//        }
//        val scope = CoroutineScope(Job() + Dispatchers.Main)
//        /** Coroutina automatica que mirarà si tenim el login guardat i el recuperarà per fer login automàtic **/
//        scope.launch {
//                Thread.sleep(5000)
//                intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
//        }

        }

    private fun Intent(coroutineScope: CoroutineScope, java: Class<LoginActivity>): Intent? {
        intent = Intent(this, java)
        return intent
    }


}


