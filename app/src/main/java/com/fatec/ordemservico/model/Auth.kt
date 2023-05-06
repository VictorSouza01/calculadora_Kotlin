package com.fatec.ordemservico.model

import android.content.Context
import android.content.SharedPreferences


class Auth(userName : String, email : String, role : String, context: Context) {
    var userName : String
    var email : String
    var role : String
    var context : Context

    init {
        this.userName = userName
        this.email = email
        this.role = role
        this.context = context
    }


}