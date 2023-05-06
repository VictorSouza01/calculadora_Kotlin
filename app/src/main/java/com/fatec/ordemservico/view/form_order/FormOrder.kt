package com.fatec.ordemservico.view.form_order

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import com.fatec.ordemservico.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.Objects

class FormOrder : AppCompatActivity() {
    lateinit var input_document_id: EditText
    lateinit var input_title: TextInputEditText
    lateinit var input_description: TextInputEditText
    lateinit var input_price: TextInputEditText

    //input do drop down
    lateinit var input_statuses: AutoCompleteTextView

    //status
    lateinit var status: String

    //itens para o drop down
    val statuses = arrayOf("Aberto", "Aceito", "Concluido", "Cancelado")

    //firebase firestom
    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_order)

        //mapeando os inputs
        input_document_id = findViewById(R.id.inputOrderId)
        input_document_id.isVisible = false
        input_title = findViewById(R.id.inputTitle)
        input_description = findViewById(R.id.inputDescription)
        input_price = findViewById(R.id.inputPrice)

        //dropdown container de todos os status
        input_statuses = findViewById(R.id.dropDownStatus)

        //selecionando a adapter para o dropdown dos status
        val adapter = ArrayAdapter(this, R.layout.dropdown_text, statuses)

        //colocar o adapter no container dropdown dos status
        input_statuses.setAdapter(adapter)

        //iniciando status com vazio toda vez que a activity é aberta
        status = ""

        //colocando o item para o status quando é selecionado
        input_statuses.setOnItemClickListener { parent, view, position, id ->
            status = parent.getItemAtPosition(position).toString()
        }

        setOrderFromIntentToInputs()

        findViewById<Button>(R.id.btnEditarOrdemServico).setOnClickListener {
            if (input_document_id.text.toString()
                    .isNullOrBlank() || input_description.text.toString()
                    .isNullOrBlank() || input_price.text.toString()
                    .isNullOrBlank() || status.isNullOrBlank()
            )
                Snackbar.make(it, "Preencha todos os campos", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(Color.RED).show()
            else
                updateOrderInformation(it)
        }
    }

    fun setOrderFromIntentToInputs() {
        input_document_id.setText(intent.getStringExtra("document_id"))
        input_title.setText(intent.getStringExtra("order_title"))
        input_description.setText(intent.getStringExtra("order_description"))
        input_price.setText(intent.getStringExtra("order_price"))
    }

    fun updateOrderInformation(view: View) {
        // Outro tipo de JSON?
        val updateOrder = mapOf(
            "title" to input_title.text.toString(),
            "description" to input_description.text.toString(),
            "price" to input_price.text.toString(),
            "status" to status
        )

        db.collection("orders").document(input_document_id.text.toString()).update(updateOrder)
            .addOnCompleteListener {
                Snackbar.make(view, "Dados atualizos com sucesso", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(
                        Color.GREEN
                    ).show()
            }
    }
}