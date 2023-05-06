package com.fatec.ordemservico.adapter

import android.app.Activity
import android.content.Intent
import android.icu.text.CaseMap.Title
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.fatec.ordemservico.model.Order
import com.fatec.ordemservico.R
import com.fatec.ordemservico.view.form_order.FormOrder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class OrderAdapter(private val context: Activity, private val arrayList: ArrayList<Order>) :
    ArrayAdapter<Order>(context, R.layout.custom_list_row, arrayList) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.custom_list_row, null)

        //mapeando minha custom view
        var user_email = view.findViewById<TextView>(R.id.list_row_userEmail)
        var title = view.findViewById<TextView>(R.id.list_row_title)
        var description = view.findViewById<TextView>(R.id.list_row_description)
        var price = view.findViewById<TextView>(R.id.list_row_price)
        var status = view.findViewById<TextView>(R.id.list_row_status)

        //itens do administrador
        var container_userId = view.findViewById<LinearLayout>(R.id.list_container_userId)
        var edit = view.findViewById<ImageView>(R.id.list_row_edit)
        var delete = view.findViewById<ImageView>(R.id.list_row_delete)

        //devo passar as variáveis dessa forma
        title.text = arrayList[position].title
        description.text = arrayList[position].description
        price.text = arrayList[position].price
        status.text = arrayList[position].status
        user_email.text = arrayList[position].user_email

        db.collection("users").document(auth.currentUser!!.email.toString())
            .addSnapshotListener { document, error ->
                if (document != null)
                    if (!document.get("role").toString().equals("admin")) {
                        container_userId.isVisible = false
                        edit.isVisible = false
                        delete.isVisible = false
                    }
            }

        edit.setOnClickListener {
            openFormOrder(position)
        }

        delete.setOnClickListener {
            deleteDocument(arrayList[position].id)
        }
        return view
    }

    fun deleteDocument(id: String) {
        var deletionResult: String =
            FirebaseFirestore.getInstance().collection("orders").document(id).delete().toString()
        //Toast.makeText(context, deletionResult, Toast.LENGTH_SHORT).show()
    }

    fun openFormOrder(position: Int) {
        context.startActivity(Intent(context, FormOrder::class.java).also {
            it.putExtra("document_id", arrayList[position].id)
            it.putExtra("order_title", arrayList[position].title)
            it.putExtra("order_description", arrayList[position].description)
            it.putExtra("order_price", arrayList[position].price)
            it.putExtra("order_status", arrayList[position].status)
        })
    }

}