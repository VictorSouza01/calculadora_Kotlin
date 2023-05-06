package com.fatec.ordemservico.view.admin

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.fatec.ordemservico.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class FormEmpresa : AppCompatActivity() {
    lateinit var inputDescription : TextInputEditText
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_empresa)
        inputDescription = findViewById(R.id.inputDescCompany)
        getCompanyDescription()
        findViewById<Button>(R.id.btnUpdateCompany).setOnClickListener{
            updateCompany(it)
        }
    }

    fun getCompanyDescription(){
        db.collection("company").document("ZipSolutio").addSnapshotListener{document, error->
            if(document != null)
                inputDescription.setText(document.getString("description"))
        }
    }

    fun updateCompany(view : View){
        var updateCompany = mapOf(
            "description" to inputDescription.text.toString()
        )
        if(inputDescription.text.isNullOrBlank())
            Snackbar.make(view,"Preencha todos os campos",Snackbar.LENGTH_SHORT).setBackgroundTint(Color.RED).show()

        //dando update no documento
        db.collection("company").document("ZipSolutio").update(updateCompany)
        Snackbar.make(view,"Dados da empresa atualizados com sucesso",Snackbar.LENGTH_SHORT).setBackgroundTint(Color.GREEN).show()
    }
}