package com.ehhl.chat

import java.io.Serializable

data class Contato(
    var nickname: String,
    var ultMensagem: String = ""
): Serializable