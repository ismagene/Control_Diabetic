package com.ismasoft.controldiabetic.ui.adapters

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.model.ControlAmbId
import com.ismasoft.controldiabetic.ui.activities.HistoricControlsActivity
import com.ismasoft.controldiabetic.ui.activities.ModificarControlActivity
import com.ismasoft.controldiabetic.viewModel.ControlsViewModel
import org.jetbrains.anko.alert
import java.text.SimpleDateFormat

class ControlsListAdapter(var context: Context, val mData: ArrayList<ControlAmbId>, val activity: HistoricControlsActivity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() , ControlsListAdapterInterface {

    private var mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mClickListener: ItemClickListener? = null
    private lateinit var viewModel : ControlsViewModel

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = mInflater.inflate(R.layout.item_controls_adapter, parent, false)
        return ControlViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        setControl(holder, position)
    }

    inner class ControlViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        internal var hora: TextView = itemView.findViewById(R.id.horaControl)
        internal var dia: TextView = itemView.findViewById(R.id.diaControl)
        internal var glucosa: TextView = itemView.findViewById(R.id.glucosaControl)
        internal var modificarControl: ImageButton = itemView.findViewById(R.id.botoModificarControl)
        internal var eliminarControl: ImageButton = itemView.findViewById(R.id.botoEliminarControl)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, adapterPosition)
        }

    }

    private fun setControl(holder: RecyclerView.ViewHolder, position: Int) {
        val item = holder as ControlViewHolder
        var control = mData[position]

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        var dataString = sdf.format(control.dataControl)
        var parts = dataString.split(" ")
        var dia =  parts[0]
        var hora = parts[1]

        item.hora.text = hora
        item.dia.text = dia
        item.glucosa.text = control.valorGlucosa.toString() + "mg/dL"

        item.modificarControl.setOnClickListener(){
            val modificarControl = Intent(context, ModificarControlActivity::class.java)
            val extras = Bundle()
            extras.putSerializable("controlModificar",control)
            modificarControl.putExtras(extras)
            context.startActivity(modificarControl)
        }
        item.eliminarControl.setOnClickListener(){
            context.alert ("Segur que voleu eliminar el control de l'hora:  ${item.hora.text} del dia: ${item.dia.text} ?","Eliminar alarma") {
                cancellable(false)
                positiveButton("Confirmar") {
                    viewModel = ControlsViewModel(application = Application())
                    viewModel.eliminarControl(mData[position].idControl.toString(), position, this@ControlsListAdapter)
                }
                negativeButton("Cancelar"){
                    // Nothing to do
                }
            }.show()
        }

    }

    internal fun setClickListener(itemClickListener: ItemClickListener) {
        this.mClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    override fun eliminarControlOK(position: Int) {
        // Retorn de la crida OK d'esborrar l'alarma
        mData.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
        if(mData.size == 0){
            activity.senseControls()
        }
    }

    override fun eliminarControlNOK() {
        Toast.makeText(context, "Error al eliminar el control", Toast.LENGTH_SHORT).show()
    }

    override fun hihaControls() {}
    override fun noHihaControls() {}

}