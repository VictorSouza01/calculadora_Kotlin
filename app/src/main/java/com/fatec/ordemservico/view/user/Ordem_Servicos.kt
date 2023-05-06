package com.fatec.ordemservico.view.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import com.fatec.ordemservico.model.Order
import com.fatec.ordemservico.R
import com.fatec.ordemservico.adapter.OrderAdapter
import com.fatec.ordemservico.dialog.OrderDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Ordem_Servicos : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val orders = arrayListOf<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ordem_servicos)

        //get user role
        db.collection("users").document(auth.currentUser!!.email.toString())
            .addSnapshotListener { document, error ->
                if (document != null) {
                    getOrders(document.get("role").toString())
                }
            }

        //float add order click
        findViewById<FloatingActionButton>(R.id.floatAddOrder).setOnClickListener {
            openOrderDialog()
        }
    }

    fun getOrders(userRole: String) {
        //firebase firestorm | Todas vez que houver uma mundança na lista este método é chamado
        db.collection("orders").addSnapshotListener() { documents, error ->
            //limpa o arraylist
            orders.clear()
            if (documents != null) {
                for (document in documents) {

                    if (userRole.equals("admin")) {
                        var order = Order(
                            document.id,
                            document.get("user_id").toString(),
                            document.get("title").toString(),
                            document.get("description").toString(),
                            document.get("status").toString(),
                            document.get("price").toString()
                        )
                        orders.add(order)
                    } else {
                        if (document.get("user_id").toString().equals(auth.currentUser!!.email)) {
                            var order = Order(
                                document.id,
                                document.get("user_id").toString(),
                                document.get("title").toString(),
                                document.get("description").toString(),
                                document.get("status").toString(),
                                document.get("price").toString()
                            )
                            orders.add(order)
                        }
                    }
                }
                //adapter da listview | O Listview tem que ficar aqui dentro
                //custom adapter
                val adapter = OrderAdapter(this, orders)

                //listsView
                val listView = findViewById<ListView>(R.id.listOfOrders)

                //passando o adapter para a listview
                listView.adapter = adapter
            } else {
                Toast.makeText(this, "Não foi possível trazer os dados", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun openOrderDialog() {
        //instanciando o orderDialog (Classe criada) que extende dialogFragment
        val dialog = OrderDialog()
        dialog.show(supportFragmentManager, dialog.tag)
    }
}