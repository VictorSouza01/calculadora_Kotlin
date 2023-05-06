package com.fatec.ordemservico.view.form_cadastro

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import com.fatec.ordemservico.R
import com.fatec.ordemservico.view.form_login.FormLogin
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore


class FormCadastro : AppCompatActivity() {

    lateinit var inputUserName : TextInputEditText
    lateinit var inputEmail : TextInputEditText
    lateinit var inputPassword : TextInputEditText
    lateinit var btnCadastrar : Button
    lateinit var btnLogar : Button


    //firebase authentication
    private val auth = FirebaseAuth.getInstance()

    //firebase firestore
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_cadastro)

        inputUserName = findViewById(R.id.inputUserName)
        inputEmail = findViewById(R.id.inputEmail)
        inputPassword = findViewById(R.id.inputPassword)
        btnCadastrar = findViewById(R.id.btnCadastrar)
        btnLogar = findViewById(R.id.btnVoltar)

        btnCadastrar.setOnClickListener {
                validate(it)
        }
    }

    fun validate(view : View){

        val userName = inputUserName.text.toString()
        val email = inputEmail.text.toString()
        val password = inputPassword.text.toString()

        if(userName.isEmpty() || email.isEmpty() || password.isEmpty())
        {
            val snackbar = Snackbar.make(view,"preencha todos os campos",Snackbar.LENGTH_LONG)
            snackbar.setBackgroundTint(Color.RED)
            snackbar.show()
            return
        }
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
            if(it.isSuccessful){
                // JSON
                //Json para cadastro de usuario
                val users = hashMapOf(
                    "UsersEmail" to email,
                    "UsersName" to userName
                    /*"role" to "comum"*/
                )
                //Cadastrando usuario na coleção users
                db.collection("Company").document("gKzG0Qv40IrxWAEpXTf7").collection("Users").document(email).set(users).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this,"deu certo",Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener{
                    val mensagemError = when (it){
                        is FirebaseError -> "Erro no banco de dados, nós já estamos cientes e iremos arrumar."
                        else -> "Não foi possível inserir o registro no Banco de dados"
                    }
                    Toast.makeText(this,mensagemError,Toast.LENGTH_LONG).show()
                }
                inputUserName.setText("")
                inputEmail.setText("")
                inputPassword.setText("")
                inputUserName.requestFocus()

                val snackbar = Snackbar.make(view,"Conta criada com sucesso",Snackbar.LENGTH_LONG)
                snackbar.setBackgroundTint(Color.GREEN)
                snackbar.show()

                //login visible
                btnLogar.isVisible = true
            }

        }.addOnFailureListener{
            // esse when é como se fosse um if | if(firebaseAuthWeakPasswordException) -> faz alguma coisa | tipo um switch case
            val mensagemErro = when(it){
                is FirebaseAuthWeakPasswordException -> "Digite uma senha que tenha no minimo 6 caracteres!"
                is FirebaseAuthInvalidCredentialsException -> "Digite um email válido!"
                is FirebaseAuthUserCollisionException -> "O email já foi usado!"
                else -> "Não foi possível cadastrar sua conta"
            }
            val snackbar = Snackbar.make(view, mensagemErro, Snackbar.LENGTH_LONG)
            snackbar.setBackgroundTint(Color.RED)
            snackbar.show()
        }
    }

    fun back(){
        startActivity(Intent(this, FormLogin::class.java))
    }

}