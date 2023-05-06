package com.fatec.ordemservico

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.fatec.ordemservico.model.Auth
import com.fatec.ordemservico.view.form_cadastro.FormCadastro
import com.fatec.ordemservico.view.form_login.FormLogin
import com.fatec.ordemservico.view.user.Home
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getCompanyDescription()

        findViewById<Button>(R.id.btnLoginActivity).setOnClickListener{
            openLoginActivity()
        }
    }

    fun openLoginActivity(){
        val intent = Intent(this,FormLogin::class.java)
        startActivity(intent)
    }

    fun getCompanyDescription() {
        db.collection("company").document("ZipSolutio").addSnapshotListener{document, error ->
            if(document != null)
                findViewById<TextView>(R.id.company_description).setText(document!!.get("description").toString())
        }
    }

    override fun onStart() {
        super.onStart()
        //abre home do usuário
        if(auth.currentUser != null)
            startActivity(Intent(this, Home::class.java))
    }
}