package com.ehhl.chat

import java.io.*
import java.lang.Boolean.parseBoolean
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException
import java.nio.CharBuffer


class Conexao(l: MensagemListener, val nick: String, val host: String) {
    var ativo = true

    lateinit var sin: BufferedReader
    lateinit var out: PrintWriter
    lateinit var socket: Socket

    val portNumber = 45000

    var listener: MensagemListener = l

    var conectado = false
    var logado = false

    init {
        Thread{
            while(ativo) {
                while (conectado);
                if(!ativo) {
                    break
                }
                try {
                    println("Tentando conectar..")

                    socket = Socket()
                    socket.connect(InetSocketAddress(host, portNumber), 10000)
                    println(socket.isConnected)
                    socket.keepAlive = true

                    sin = BufferedReader(InputStreamReader(socket.getInputStream()))
                    out = PrintWriter(BufferedWriter(OutputStreamWriter(socket.getOutputStream())), true)

                    sendREG()

                    conectado = true;
                    listener.conexaoEstabelecida()
                } catch (e: Exception) {
                    println("Deu erro: ${e.localizedMessage}")
                    //throw e
                }
                //Thread.sleep(5000)
            }
        }.start()

        Thread {
            while(ativo) {
                while(!conectado);
                if(!ativo){
                    break
                }
                println("Aguardando mensagens")
                try{
                    var buf = CharArray(20050)
                    sin.read(buf)
                    val fromServer: String = String(buf)
                    if(fromServer.isEmpty())
                        continue
                    //val fromServer = sin.readLine()

                    if(fromServer == null){
                        break;
                    }
                    val mens = Mensagem.extraiMensagens(fromServer)
                    println("${mens.size} mensagens from server: $fromServer")
                    for (men in mens) {
                        when (men.tipo) {
                            Tipo.LIV -> {
                                sendACK()

                            }
                            Tipo.REG -> {
                            }
                            Tipo.ERA -> {
                                listener.novaMensagem(Mensagem(fromServer))
                            }
                            Tipo.ACK -> {
                                if (!logado) {
                                    var resp = parseBoolean(men.dados)
                                    listener.respostaLogin(resp)
                                    logado = resp
                                }
                            }
                            Tipo.UPD -> {
                                listener.novaMensagem(Mensagem(fromServer))
                            }
                            Tipo.MEN -> {
                                listener.novaMensagem(Mensagem(fromServer))
                            }
                        }
                    }

                }catch (e: java.lang.Exception){
                    listener.conexaoPerdida()
                    println("ERROASD: "+e.localizedMessage)

                    conectado = false;
                }


            }
        }.start()

    }

    fun sendREG(){
        val regMsg = Mensagem(tipo = Tipo.REG, from = nick)

        send(regMsg)
    }

    fun sendMEN(men: Mensagem){
        men.from = nick

        send(men)
    }

    fun sendACK(){
        val men = Mensagem(tipo = Tipo.ACK, from = nick)

        send(men)
    }

    fun sendERA(){
        val men = Mensagem(tipo = Tipo.ERA, from = nick)

        send(men)
    }

    fun send(men: Mensagem){
        println("Enviando ${men.format()}")
        out.print(men.format())
        Thread {
            out.flush()
        }.start()
    }

    fun finaliza(){
        sendERA()
        sin.close()
        socket.close()
        ativo = false
        conectado = false
    }

    interface MensagemListener {
        fun novaMensagem(m: Mensagem)
        fun respostaLogin(resp: Boolean)
        fun conexaoPerdida()
        fun conexaoEstabelecida()
    }

}
