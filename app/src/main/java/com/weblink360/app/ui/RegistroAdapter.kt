package com.weblink360.app.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.weblink360.app.R
import com.weblink360.app.util.ItemRegistro

/** Adaptador reutilizable: muestra cualquier listado como tarjetas título + detalle. */
class RegistroAdapter : RecyclerView.Adapter<RegistroAdapter.Holder>() {

    private var items: List<ItemRegistro> = emptyList()

    fun actualizar(nuevos: List<ItemRegistro>) {
        items = nuevos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_registro, parent, false)
        return Holder(vista)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.titulo.text = item.titulo
        holder.detalle.text = item.detalle
        holder.detalle.visibility = if (item.detalle.isBlank()) View.GONE else View.VISIBLE
    }

    override fun getItemCount(): Int = items.size

    class Holder(vista: View) : RecyclerView.ViewHolder(vista) {
        val titulo: TextView = vista.findViewById(R.id.tvTitulo)
        val detalle: TextView = vista.findViewById(R.id.tvDetalle)
    }
}
