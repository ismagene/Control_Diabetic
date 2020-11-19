package com.ismasoft.controldiabetic.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ProgressBar
import com.ismasoft.controldiabetic.R

class RegistreActivity : AppCompatActivity() {

    private lateinit var loginNom:EditText
    private lateinit var loginSurname:EditText
    private lateinit var loginEmail:EditText
    private lateinit var loginPassword:EditText
    private lateinit var progressBar: ProgressBar
//    private lateinit var dbReference:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registre)
    }
}