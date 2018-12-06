package com.ehhl.chat

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import kotlinx.android.synthetic.main.fragment_contatos.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class ContatosAdapter(
        private val mValues: List<Contato>,
        private val mListener: OnFragmentInteractionListener?)
    : RecyclerView.Adapter<ContatosAdapter.ViewHolder>() {



    private val mOnClickListener: View.OnClickListener

    init {
        var listener = mListener
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Contato
            println("clicou")
            println(listener)
            listener?.abreContato(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_contatos, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.txtNome.text = item.nickname
        holder.txtMensagem.text = item.ultMensagem

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val txtNome: TextView = mView.content
        val txtMensagem: TextView = mView.txtMensagem
        override fun toString(): String {
            return super.toString() + " '" + txtNome.text + "'"
        }
    }
}
