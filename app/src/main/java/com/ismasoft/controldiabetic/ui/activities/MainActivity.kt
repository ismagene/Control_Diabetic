package com.ismasoft.controldiabetic.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.ismasoft.controldiabetic.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** Funció que si apretem
         *   @param user - email de l'usuari que es registre
         *   @param pass - contrasenya **/
        val textView = findViewById<TextView>(R.id.prova) as TextView
        textView?.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

//        with(binding) {
//            button.set
//        }

    }


}