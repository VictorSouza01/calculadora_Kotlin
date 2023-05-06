package com.fatec.ordemservico.view.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.fatec.ordemservico.MainActivity
import com.fatec.ordemservico.R
import com.fatec.ordemservico.view.admin.FormEmpresa
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Home : AppCompatActivity() {
    //firebase
    private val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        getUserInformation()

        //efetua o logout
        findViewById<ImageView>(R.id.btnLogout).setOnClickListener{
            logout()
        }

        //abre perfil do usuário
        findViewById<Button>(R.id.btnCardPerfil).setOnClickListener{
            openPerfilActivity()
        }

        //abre ordem de serviço
        findViewById<Button>(R.id.btnCardOrdem).setOnClickListener{
            openOrdemServicoActivity()
        }

        //abre informações da empresa
        findViewById<Button>(R.id.btnCardEmpresa).setOnClickListener{
            openCompanyActivity()
        }
    }
    // pega do firabase firestom o nome do usuário
    fun getUserInformation(){
        // os pontos de exclamação basicamente diz que eu sei que este dado não vai ser nulo para o compilador
        val userEmail = auth.currentUser!!.email.toString()

        //pegando as informações no cloud firestorm
        db.collection("users").document(userEmail).addSnapshotListener{document, error ->
            if(document != null){
                findViewById<TextView>(R.id.textusername).setText(document.getString("username"))
                if(document.getString("role").equals("admin")){
                    findViewById<MaterialCardView>(R.id.cardEmpresa).isVisible = true
                }else {
                    findViewById<MaterialCardView>(R.id.cardEmpresa).isVisible = false
                }
            }
        }
    }

    //destroi a instância do usuário logado
    fun logout() {
        auth.signOut()
        finish()
        startActivity(Intent(this,MainActivity::class.java))
    }

    //abre a activity ordem de serviço
    fun openOrdemServicoActivity(){
        startActivity(Intent(this,Ordem_Servicos::class.java))
    }

    fun openPerfilActivity(){
        startActivity(Intent(this, FormPerfil::class.java))
    }

    fun openCompanyActivity(){
        startActivity(Intent(this, FormEmpresa::class.java))
    }
}