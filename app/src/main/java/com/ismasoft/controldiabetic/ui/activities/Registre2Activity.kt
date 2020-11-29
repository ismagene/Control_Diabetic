package com.ismasoft.controldiabetic.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.model.User
import com.ismasoft.controldiabetic.databinding.ActivityRegistre2Binding
import com.ismasoft.controldiabetic.viewModel.RegistreViewModel

class Registre2Activity : AppCompatActivity() {

    private lateinit var viewModel: RegistreViewModel
    private lateinit var binding: ActivityRegistre2Binding

    private lateinit var usuari : User

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registre2)

        binding = ActivityRegistre2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider (this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        var usuari : User? = null

        var bundle = intent.extras
        val nom : String? = bundle?.getString("nom")
        val cognom: String? = bundle?.getString("cognom")

        binding.buttonRegister.setOnClickListener {

            var usuari = inicialitzarUsuari(binding)

            var retornRegistre2 = viewModel.onButtonRegistreClicked(usuari!!)
            if(retornRegistre2){
                intent = Intent(this,MenuPrincipalActivity::class.java)
                startActivity(intent)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun inicialitzarUsuari(binding: ActivityRegistre2Binding): User? {
//
//        var defaultZoneId: ZoneId = ZoneId.ofOffset("ECT", ZoneOffset.UTC)
//        var dataNaixament = LocalDate.parse(binding.loginNaixament.text.toString(), DateTimeFormatter.ISO_DATE)
//
//        val instant =  Instant.parse(binding.loginNaixament.text.toString())
//        val londonZonedDateTime = instant.atZone(ZoneId.of("Europe/London"))

        return null
//        return User(
//                this.binding.loginNom.text.toString(),
//                this.binding.loginCognom.text.toString(),
//                this.binding.loginCognom2.text.toString(),
////                Date.from(dataNaixament.atStartOfDay(defaultZoneId).toInstant()),
//                null,
//                this.binding.loginCorreuElectronic.text.toString(),
//                this.binding.loginPassword.text.toString(),
//                this.binding.loginGenere.text.toString(),
//                null,
//                null,
//                "prova",
//                "prova",
//                "prova",
//                "prova",
//                null,
//                0,
//                0,
//                0,
//                0,
//                0,
//                0,
//        )

    }
}