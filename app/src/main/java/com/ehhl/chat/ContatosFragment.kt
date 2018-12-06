package com.ehhl.chat

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ContatosFragment : Fragment() {

    val contatos: MutableList<Contato> = ArrayList()
    private var listener: OnFragmentInteractionListener? = null
    lateinit var lstAdapter: ContatosAdapter

    var mainActivity: MainActivity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contato, container, false)


        //contatos.add(Contato("Fulano"))

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                lstAdapter = ContatosAdapter(contatos, listener)
                adapter = lstAdapter
            }
        }

        listener?.let{
            it.mudaTitulo("MyChat")
            it.showBackButton(false)
        }
        return view
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun atualizaContatos(sContatos: String){
        var aContatos = sContatos.split(";")
        contatos.clear()
        for (nome in aContatos){
            if(nome != mainActivity?.nick)
                contatos.add(Contato(nome))
        }
        mainActivity?.runOnUiThread {
            lstAdapter.notifyDataSetChanged()
        }

    }


}
