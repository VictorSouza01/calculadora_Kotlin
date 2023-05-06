package com.fatec.ordemservico.view.user

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.fatec.ordemservico.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FormPerfil : AppCompatActivity() {

    //Firebase
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    lateinit var input_nome_completo: TextInputEditText

    //lateinit var input_email : TextInputEditText
    lateinit var input_password: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_perfil)

        input_nome_completo = findViewById(R.id.inputNomeCompleto)
        //input_email = findViewById(R.id.inputEmail)
        input_password = findViewById(R.id.inputPassword)

        getUserInformation()

        findViewById<Button>(R.id.btnEditarInformacoes).setOnClickListener {
            if (input_nome_completo.text.isNullOrEmpty() || input_password.text.isNullOrEmpty())
                Snackbar.make(it, "Preencha todos os campos", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(Color.RED).show()
            else
                updateUserInformation(it)
        }
    }

    fun getUserInformation() {
        //input_email.setText(auth.currentUser!!.email)
        db.collection("users").document(auth.currentUser!!.email.toString())
            .addSnapshotListener { document, error ->
                if (document != null) {
                    input_nome_completo.setText(document.getString("username"))
                    input_password.setText(document.getString("password"))
                }
            }
    }

    fun updateUserInformation(view: View) {
        var userName = input_nome_completo.text.toString()
        //var userEmail = input_email.text.toString()
        var userPassword = input_password.text.toString()

        var updateUser = mapOf(
            "username" to userName,
            "password" to userPassword
        )

        //dando update no firebase firestorm
        db.collection("users").document(auth.currentUser!!.email.toString()).update(updateUser)

        //dando update no firebase auth
        auth.currentUser!!.updatePassword(userPassword)

        Snackbar.make(view, "Dados alterados com sucesso", Snackbar.LENGTH_SHORT)
            .setBackgroundTint(Color.GREEN).show()
    }
}