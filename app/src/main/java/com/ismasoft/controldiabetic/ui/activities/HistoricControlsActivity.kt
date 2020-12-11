package com.ismasoft.controldiabetic.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.model.AlarmaAmbId
import com.ismasoft.controldiabetic.data.model.Control
import com.ismasoft.controldiabetic.data.model.ControlAmbId
import com.ismasoft.controldiabetic.databinding.ActivityAfegirAlarmaBinding
import com.ismasoft.controldiabetic.databinding.ActivityHistoricControlsBinding
import com.ismasoft.controldiabetic.databinding.FragmentAlarmesBinding
import com.ismasoft.controldiabetic.ui.adapters.AlarmesListAdapter
import com.ismasoft.controldiabetic.ui.adapters.ControlsListAdapter
import com.ismasoft.controldiabetic.viewModel.AlarmesViewModel
import com.ismasoft.controldiabetic.viewModel.ControlsViewModel

class HistoricControlsActivity : AppCompatActivity(), ControlsListAdapter.ItemClickListener {

    private lateinit var viewModel: ControlsViewModel
    private lateinit var binding: ActivityHistoricControlsBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ControlsListAdapter

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        return super.onCreateView(parent, name, context, attrs)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historic_controls)

        var objetoIntent : Intent = intent

        // Per tornar endarrera
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Recuperem els valors enviats del fragment de controls
        var llistaControls : ArrayList<ControlAmbId> = objetoIntent.extras?.get("llistaControls") as ArrayList<ControlAmbId>

        binding = ActivityHistoricControlsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        recyclerView = binding.recyclerViewControls
        recyclerView.layoutManager = LinearLayoutManager(this)
//        adapter = this.let { ControlsListAdapter(it, ArrayList<ControlAmbId>()) }!!
//        adapter.setClickListener(this)

        adapter = this.let { ControlsListAdapter(it, llistaControls) }!!
        adapter.notifyDataSetChanged()

        recyclerView.adapter = adapter

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(this, "Clicat element $position", Toast.LENGTH_SHORT).show()
    }
}