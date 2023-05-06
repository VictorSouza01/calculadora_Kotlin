package com.fatec.ordemservico.dialog


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.fatec.ordemservico.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

//Dialog é como se fosse um MODAL
class OrderDialog: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_create_order, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    // quando o dialog der start vai mudar os parâmetros de layout
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        //Achando o id dos elementos
        val inputTitle = dialog?.findViewById<TextInputEditText>(R.id.inputTitle)
        val inputDescription = dialog?.findViewById<TextInputEditText>(R.id.inputDescription)
        val inputPrice = dialog?.findViewById<TextInputEditText>(R.id.inputPrice)

        dialog?.findViewById<Button>(R.id.btnRequisitarOrdemServico)?.setOnClickListener{
            var title = inputTitle?.text.toString()
            var description = inputDescription?.text.toString()
            var price = inputPrice?.text.toString()

            if(title.isEmpty() || description.isEmpty() || price.isEmpty())
            {
                val snackbar = Snackbar.make(it,"Preencha todos os campos",Snackbar.LENGTH_LONG)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.setTextColor(Color.WHITE)
                snackbar.show()
            }else {
                var order = hashMapOf(
                    "title" to title,
                    "description" to description,
                    "price" to price,
                    "status" to "Aberto",
                    "user_id" to FirebaseAuth.getInstance().currentUser!!.email.toString()
                )
                //se cada documento da minha coleção de ordens tem um campo primario automático não é necessário falar o documento
                FirebaseFirestore.getInstance().collection("orders").add(order).addOnSuccessListener {document->
                    //limpando os campos
                    inputTitle!!.setText("")
                    inputDescription!!.setText("")
                    inputPrice!!.setText("")
                    inputTitle!!.requestFocus()

                    //snackbar de sucesso
                    val snackbar = Snackbar.make(it,"Nova ordem de serviço requisitada",Snackbar.LENGTH_LONG)
                    snackbar.setBackgroundTint(Color.GREEN)
                    snackbar.setTextColor(Color.WHITE)
                    snackbar.show()
                }.addOnFailureListener{exception->
                    var mensagemError = when(exception){
                        is FirebaseFirestoreException -> "Houve algum erro"
                        else -> "Não foi possível inserir esta informações em nosso banco de dados"
                    }
                    val snackbar = Snackbar.make(it,mensagemError,Snackbar.LENGTH_LONG)
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.setTextColor(Color.WHITE)
                    snackbar.show()
                }
            }
        }
    }
}