package com.ismasoft.controldiabetic.ui.adapters

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.model.VisitaAmbId
import com.ismasoft.controldiabetic.ui.fragments.AlarmesFragment
import com.ismasoft.controldiabetic.ui.fragments.VisitesFragment
import com.ismasoft.controldiabetic.utilities.deleteAlarm
import com.ismasoft.controldiabetic.viewModel.AlarmesViewModel
import com.ismasoft.controldiabetic.viewModel.VisitesViewModel
import org.jetbrains.anko.alert
import java.text.SimpleDateFormat

class VisitesListAdapter(var context: Context, val mData: ArrayList<VisitaAmbId>, val fragment: VisitesFragment) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), VisitesListAdapterInterface {

    private var mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mClickListener: ItemClickListener? = null
    private lateinit var viewModel : VisitesViewModel

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = mInflater.inflate(R.layout.item_visites_adapter, parent, false)
        return VisitesViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        setVisita(holder, position)
    }

    inner class VisitesViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        internal var motiuVisita: TextView = itemView.findViewById(R.id.motiuVisita)
        internal var dataVisita: TextView = itemView.findViewById(R.id.dataVisita)
        internal var eliminarVisita: ImageButton = itemView.findViewById(R.id.botoEliminarVisita)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, adapterPosition)
        }

    }

    private fun setVisita(holder: RecyclerView.ViewHolder, position: Int) {
        val item = holder as VisitesViewHolder
        val visita = mData[position]
        val sdf = SimpleDateFormat("dd/MM/yyyy")

        item.motiuVisita.text = visita.motiu.toString()
        visita.dataVisita.let {
            item.dataVisita.text = sdf.format(it!!)
        }

        item.eliminarVisita.setOnClickListener(){
            context.alert ("Segur que voleu eliminar la visita del dia ${item.dataVisita.text} ?","Eliminar visita") {
                cancellable(false)
                positiveButton("Confirmar") {
                    viewModel = VisitesViewModel(application = Application())
                    viewModel.eliminarVisitaPasada(mData[position].idVisita.toString(), position, this@VisitesListAdapter)
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

    override fun eliminarVisitaPasadaOK(position: Int) {
        // Retorn de la crida OK d'esborrar la visitaPasada
        mData.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
        if(mData.size == 0){
            fragment.noVisites()
            viewModel.noQuedenVisites()
        }
    }

    override fun eliminarVisitaPasadaNOK() {
        Toast.makeText(context, "Error al eliminar la visita", Toast.LENGTH_SHORT).show()
    }

}