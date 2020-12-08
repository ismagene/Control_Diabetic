package com.ismasoft.controldiabetic.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.model.Visita
import com.ismasoft.controldiabetic.data.model.VisitaAmbId
import com.ismasoft.controldiabetic.ui.fragments.VisitesFragment
import java.text.SimpleDateFormat


class VisitesListAdapter(var context: Context, val mData: ArrayList<VisitaAmbId>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mClickListener: ItemClickListener? = null

    /*enum class RowType {
        PRODUCT_ITEM
    }

    fun getViewTypeCount(): Int {
        return RowType.values().size
    }*/

    override fun getItemCount(): Int {
        return mData.size
    }

    internal fun getItem(position: Int): VisitaAmbId? {
        return mData[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = mInflater.inflate(R.layout.item_visites_adapter, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        setProduct(holder, position)
    }

    inner class ProductViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        internal var motiuVisita: TextView = itemView.findViewById(R.id.motiuVisita)
        internal var dataVisita: TextView = itemView.findViewById(R.id.dataVisita)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, adapterPosition)
        }

    }

    private fun setProduct(holder: RecyclerView.ViewHolder, position: Int) {
        val item = holder as ProductViewHolder

        val visita = mData[position]
        val sdf = SimpleDateFormat("dd/MM/yyyy")

        item.motiuVisita.text = visita.motiu.toString()
        visita.dataVisita.let {
            item.dataVisita.text = sdf.format(it!!)
        }
    }

    internal fun setClickListener(itemClickListener: ItemClickListener) {
        this.mClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

}