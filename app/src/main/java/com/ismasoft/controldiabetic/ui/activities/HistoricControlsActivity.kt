package com.ismasoft.controldiabetic.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.model.Control

class HistoricControlsActivity : AppCompatActivity() {

    private lateinit var llistaControls : HashMap<String, Control>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historic_controls)

        var objetoIntent : Intent = intent

        //var llistaControls : HashMap<String, Control> = objetoIntent.extras?.get("prova") as HashMap<String, Control>

    }
}