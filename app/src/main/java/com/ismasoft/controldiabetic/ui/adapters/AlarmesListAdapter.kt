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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.model.AlarmaAmbId
import com.ismasoft.controldiabetic.ui.activities.ModificarAlarmaActivity
import com.ismasoft.controldiabetic.ui.fragments.AlarmesFragment
import com.ismasoft.controldiabetic.utilities.deleteAlarm
import com.ismasoft.controldiabetic.viewModel.AlarmesViewModel
import org.jetbrains.anko.alert

class AlarmesListAdapter(var context: Context, val mData: ArrayList<AlarmaAmbId>, val fragment: AlarmesFragment) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() , AlarmesListAdapterInterface {

    private var mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mClickListener: ItemClickListener? = null
    private lateinit var viewModel : AlarmesViewModel

    private val _senseAlarmes = MutableLiveData<Boolean>(false)
    val senseAlarmes : LiveData<Boolean> get() = _senseAlarmes

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = mInflater.inflate(R.layout.item_alarmes_adapter, parent, false)
        return AlarmaViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        setAlarma(holder, position)
    }

    inner class AlarmaViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        internal var hora: TextView = itemView.findViewById(R.id.horaAlarma)
        internal var modificarAlarma: ImageButton = itemView.findViewById(R.id.botoModificarAlarma)
        internal var eliminarAlarma: ImageButton = itemView.findViewById(R.id.botoEliminarAlarma)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, adapterPosition)
        }

    }

    private fun setAlarma(holder: RecyclerView.ViewHolder, position: Int) {
        val item = holder as AlarmaViewHolder
        val alarma = mData[position]
        item.hora.text = alarma.horaAlarma

        item.modificarAlarma.setOnClickListener(){
            val modificarAlarma = Intent(context, ModificarAlarmaActivity::class.java)
            val extras = Bundle()
            extras.putSerializable("alarmaModificar",alarma)
            modificarAlarma.putExtras(extras)
            context.startActivity(modificarAlarma)
        }
        item.eliminarAlarma.setOnClickListener(){
            context.alert ("Segur que voleu eliminar l'alarma ${item.hora.text} ?","Eliminar alarma") {
                cancellable(false)
                positiveButton("Confirmar") {
                    viewModel = AlarmesViewModel(application = Application())
                    viewModel.eliminarAlarma(mData[position].idAlarma.toString(), position, this@AlarmesListAdapter)
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

    override fun eliminarAlarmaOK(position: Int) {
        // Retorn de la crida OK d'esborrar l'alarma
        deleteAlarm(position,context)
        mData.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
        if(mData.size == 0){
            fragment.noAlarmes()
            viewModel.noQuedenAlarmes()
        }
    }

    override fun eliminarAlarmaNOK() {
        Toast.makeText(context, "Error a l'eliminar l'alarma", Toast.LENGTH_SHORT).show()
    }

}