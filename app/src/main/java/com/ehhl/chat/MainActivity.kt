package com.ehhl.chat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), OnFragmentInteractionListener, Conexao.MensagemListener {


    lateinit var nick: String

    var fragChat: MutableList<ChatFragment> = mutableListOf()
    val fragContatos = ContatosFragment()
    val fragLogin = LoginFragment()

    lateinit var conexao: Conexao



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        fragContatos.mainActivity=this

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.content_main, fragLogin)
        ft.commit()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_logoff-> {
                fragChat.clear()
                fragContatos.contatos.clear()
                conexao.finaliza()
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.content_main, fragLogin)
                ft.commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStop() {
        super.onStop()
        conexao.finaliza()
        println("Finalizou")
    }

    override fun abreContato(item: Contato) {
        println("Abrindo contato ${item.nickname}")
        val ft = supportFragmentManager.beginTransaction()
        var frag: ChatFragment? = null

        for(f in fragChat){
            println("Comparando '${f.contato!!.nickname}' com '${item.nickname}'")
            if(f.contato!!.nickname == item.nickname) {

                frag = f
                break
            }
        }
        if (frag == null) {
            frag = ChatFragment.newInstance(item)
            fragChat.add(frag)
            println("Novo chatFragment para ${item.nickname}")
        }

        ft.replace(R.id.content_main, frag)
        ft.addToBackStack(item.nickname)
        ft.commit()

    }

    override fun mudaTitulo(titulo: String){
        supportActionBar?.let {
            it.title = titulo
        }
    }

    override fun showBackButton(show: Boolean){
        supportActionBar?.setDisplayHomeAsUpEnabled(show)
    }

    override fun novaMensagem(m: Mensagem) {
        //println(m.format())
        when(m.tipo){
            Tipo.MEN->{
                var achou = false
                for (frag in fragChat){
                    println("Comparando '${frag.contato!!.nickname}' com '${m.from}'")
                    if(frag.contato!!.nickname == m.from){
                        frag.NovaMensagem(m)
                        achou = true
                    }
                }
                var c: Contato? = null
                for(contato in fragContatos.contatos){
                    if(contato.nickname == m.from){
                        c = contato//.ultMensagem = m.dados
                        break
                    }
                }
                if(!achou){
                    val frag = ChatFragment.newInstance(c!!)
                    frag.NovaMensagem(m)
                    fragChat.add(frag)
                    println("Novo chatFragment para ${m.from}")
                }
                runOnUiThread {
                    fragContatos.lstAdapter.notifyDataSetChanged()
                }
            }
            Tipo.UPD->{
                fragContatos.atualizaContatos(m.dados)
            }
        }
    }

    override fun enviaMensagem(m: Mensagem){
        conexao.send(m)
    }

    override fun login(nick: String, server: String) {
        this.nick = nick
        conexao = Conexao(this, nick, server)
    }

    override fun respostaLogin(resp: Boolean){
        if(resp) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.content_main, fragContatos)
            ft.commit()
        }else{
            runOnUiThread {
                Toast.makeText(this, "Nick indisponível", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun conexaoPerdida() {
        runOnUiThread {
            Toast.makeText(this, "Conexão perdida, tentando reconectar..", Toast.LENGTH_LONG).show()
        }
    }

    override fun conexaoEstabelecida() {
        runOnUiThread{
            Toast.makeText(this, "Conexão estabelecida", Toast.LENGTH_LONG).show()
        }

    }


}
