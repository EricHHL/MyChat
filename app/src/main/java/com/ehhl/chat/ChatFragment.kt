package com.ehhl.chat


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.EditText
import kotlinx.android.synthetic.main.fragment_chat.*


class ChatFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
//nick = getPreferences(Context.MODE_PRIVATE).getString("nick", "nick")
    val mensagens: MutableList<Mensagem> = ArrayList()
    lateinit var adapter: MessagesAdapter
    var contato: Contato? = null

    lateinit var nick: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nick = (activity as MainActivity).nick
        adapter = MessagesAdapter(mensagens, nick)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)


        val lstMensagens = view.findViewById<RecyclerView>(R.id.lstMensagens)
        val txtMensagem = view.findViewById<EditText>(R.id.txtMensagem)


        //Seta callback para quando aperta enviar
        txtMensagem.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    enviar()
                    return true
                }
                return false
            }
        })

        lstMensagens.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        layoutManager.stackFromEnd = true
        lstMensagens.layoutManager = layoutManager




        listener?.let{
            it.mudaTitulo(contato!!.nickname)
            it.showBackButton(true)
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        btEnviar.setOnClickListener {
            enviar()
        }
    }

    fun NovaMensagem(mensagem: Mensagem) {
        if (mensagem.tipo == Tipo.MEN) {
            mensagens.add(mensagem)
            contato!!.ultMensagem = mensagem.dados
            activity?.runOnUiThread {
                adapter.notifyDataSetChanged()
            }

        }
    }

    fun enviar(){
        if(txtMensagem.text.isEmpty())
            return
        var msg = Mensagem(tipo = Tipo.MEN, from = nick, to = contato!!.nickname,dados = txtMensagem.text.toString())
        NovaMensagem(msg)

        txtMensagem.setText("")

        listener?.enviaMensagem(msg)
    }

    companion object {
        @JvmStatic
        fun newInstance(contato: Contato) =
                ChatFragment().apply {
                    this.contato = contato
                }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
