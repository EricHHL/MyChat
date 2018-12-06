package com.ehhl.chat

interface OnFragmentInteractionListener {
    fun abreContato(item: Contato)
    fun mudaTitulo(titulo: String)
    fun showBackButton(show: Boolean)
    fun enviaMensagem(m: Mensagem)
    fun login(nick: String, server: String)
}