package com.ismasoft.controldiabetic.ui.adapters

import com.ismasoft.controldiabetic.data.model.Alarma
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.model.AlarmaAmbId
import com.ismasoft.controldiabetic.data.model.VisitaAmbId
import java.text.SimpleDateFormat

class AlarmesListAdapter (var context: Context, val mData: List<AlarmaAmbId>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mClickListener: ItemClickListener? = null

    override fun getItemCount(): Int {
        return mData.size
    }

    internal fun getItem(position: Int): AlarmaAmbId? {
        return mData[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = mInflater.inflate(R.layout.item_alarmes_adapter, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        setProduct(holder, position)
    }

    inner class ProductViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        internal var hora: TextView = itemView.findViewById(R.id.horaAlarma)
//        internal var modificarAlarma: TextView = itemView.findViewById(R.id.botoModificarAlarma)
//        internal var eliminarAlarma: TextView = itemView.findViewById(R.id.botoEliminarAlarma)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, adapterPosition)
//            if(modificarAlarma!=null) modificarAlarma!!.onItemClick()
        }


    }

    private fun setProduct(holder: RecyclerView.ViewHolder, position: Int) {
        val item = holder as ProductViewHolder
        val alarma = mData[position]
        item.hora.text = alarma.horaAlarma
    }

    internal fun setClickListener(itemClickListener: ItemClickListener) {
        this.mClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

}