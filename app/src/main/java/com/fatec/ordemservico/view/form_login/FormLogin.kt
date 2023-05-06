package com.fatec.ordemservico.view.form_login

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.fatec.ordemservico.R
import com.fatec.ordemservico.view.form_cadastro.FormCadastro
import com.fatec.ordemservico.view.user.Home
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException

class FormLogin : AppCompatActivity() {

    lateinit var inputEmail: TextInputEditText
    lateinit var inputPassword: TextInputEditText

    //firebase authentication
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_login)

        inputEmail = findViewById(R.id.inputEmail)
        inputPassword = findViewById(R.id.inputPassword)

        findViewById<Button>(R.id.btnLogar).setOnClickListener {
            validateForm(it)
        }

        findViewById<TextView>(R.id.abreCadastroForm).setOnClickListener {
            openRegisterActivity(it)
        }
    }

    fun openRegisterActivity(View: View) {
        //objeto anônimo
        startActivity(Intent(this, FormCadastro::class.java))
    }

    fun validateForm(view: View) {
        val email = inputEmail.text.toString()
        val password = inputPassword.text.toString()
        if (email.isNullOrBlank() || password.isNullOrBlank())
            Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_LONG)
                .setBackgroundTint(Color.RED).show()
        else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { autenticacao ->
                if (autenticacao.isSuccessful) {
                    openHome()
                }
            }.addOnFailureListener {
                val mensagemErro = when (it) {
                    is FirebaseAuthEmailException -> "Email inválido"
                    else -> "Não foi possível fazer login"
                }
                Snackbar.make(view, mensagemErro, Snackbar.LENGTH_LONG).setBackgroundTint(Color.RED).show()
            }
        }
    }

    fun openHome() {
        startActivity(Intent(this, Home::class.java))
    }
}