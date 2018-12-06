package com.ehhl.chat

enum class Tipo(){
    REG, ERA, ACK, UPD, LIV, MEN
}

class Mensagem {


    var versao: Int = 1
    var tipo: Tipo = Tipo.REG
    var from: String = ""
    var to: String = ""
    var tam: Int = 0
    var dados: String = ""

    constructor(s: String){
        this.versao = Integer.parseInt(s.substring(0,1))
        this.tipo = Tipo.valueOf(s.substring(1,4))
        this.from = s.substring(4,24).trim()
        this.to = s.substring(24,44).trim()
        this.tam = Integer.parseInt(s.substring(44,49).trim())
        this.dados = s.substring(49,49+tam)
    }

    constructor(versao: Int = 1, tipo: Tipo, from: String, to: String = "", dados: String = "") {
        this.versao = versao
        this.tipo = tipo
        this.from = from.trim()
        this.to = to.trim()
        this.dados = dados
        this.tam = dados.length
    }

    fun format(): String{
        val s = "%1d%3s%-20s%-20s%05d%s".format(versao, tipo.name, from, to, tam, dados)
        return s
    }

    companion object {
        fun extraiMensagens(str: String): MutableList<Mensagem> {
            var lstMsg = ArrayList<Mensagem>()
            var strm = str
            while(strm.startsWith("1")){
                var msg = Mensagem(str)
                lstMsg.add(msg)
                var offset = 49+msg.tam+1
                strm = strm.substring(offset)

                println("'${strm.get(0)}' virou $strm")
            }

            return lstMsg

        }
    }

}