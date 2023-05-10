package com.calculadora.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import net.objecthunter.exp4j.ExpressionBuilder

var PodePonto = true
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()

        findViewById<TextView>(R.id.Zero).setOnClickListener(){Acrescentar_uma_Expressao("0", true)}
        findViewById<TextView>(R.id.Um).setOnClickListener(){Acrescentar_uma_Expressao("1", true)}
        findViewById<TextView>(R.id.Dois).setOnClickListener(){Acrescentar_uma_Expressao("2", true)}
        findViewById<TextView>(R.id.Tres).setOnClickListener(){Acrescentar_uma_Expressao("3", true)}
        findViewById<TextView>(R.id.Quatro).setOnClickListener(){Acrescentar_uma_Expressao("4", true)}
        findViewById<TextView>(R.id.Cinco).setOnClickListener(){Acrescentar_uma_Expressao("5", true)}
        findViewById<TextView>(R.id.Seis).setOnClickListener(){Acrescentar_uma_Expressao("6", true)}
        findViewById<TextView>(R.id.Sete).setOnClickListener(){Acrescentar_uma_Expressao("7", true)}
        findViewById<TextView>(R.id.Oito).setOnClickListener(){Acrescentar_uma_Expressao("8", true)}
        findViewById<TextView>(R.id.Nove).setOnClickListener(){Acrescentar_uma_Expressao("9", true)}
        findViewById<TextView>(R.id.Ponto).setOnClickListener(){
            try{
                val Expressao = findViewById<TextView>(R.id.Expressao).text.toString()
                val ExpressaoL = Expressao.last()

                if (ExpressaoL.toString() != "." && PodePonto){
                    if(ExpressaoL.toString() != "+" && ExpressaoL.toString() != "-" && ExpressaoL.toString() != "*"
                        && ExpressaoL.toString() != "/" && ExpressaoL.toString() != ""){
                        Acrescentar_uma_Expressao(".", true)
                        PodePonto = false
                    }
                }
            }
            catch (e:java.lang.Exception){
                println(e)
            }

        }


        //Operadores
            findViewById<TextView>(R.id.Soma).setOnClickListener(){
                try {
                    Verifica_Ultima_Expressao("+")
                    PodePonto=true
                }
                catch (e:java.lang.Exception){
                    println(e)
                }
            }
            findViewById<TextView>(R.id.Sub).setOnClickListener(){
                try {
                    Verifica_Ultima_Expressao("-")
                    PodePonto=true
                }
                catch (e:java.lang.Exception){
                    println(e)
                }
            }
            findViewById<TextView>(R.id.Mult).setOnClickListener(){
                try {
                    Verifica_Ultima_Expressao("*")
                    PodePonto=true
                }
                catch (e:java.lang.Exception){
                    println(e)
                }
            }
            findViewById<TextView>(R.id.Divisao).setOnClickListener(){
                try {
                    Verifica_Ultima_Expressao("/")
                    PodePonto=true
                }
                catch (e:java.lang.Exception){
                    println(e)
                }
            }

        //Botao de limpar tudo
        findViewById<TextView>(R.id.Limpar).setOnClickListener(){
            findViewById<TextView>(R.id.Resultado).text = ""
            findViewById<TextView>(R.id.Expressao).text = ""
            PodePonto=true;
        }

        //Bota de BackSpace
        findViewById<ImageView>(R.id.BackSpace).setOnClickListener(){
            try{
                val valor = findViewById<TextView>(R.id.Expressao).text.toString()

                if(valor.isNotBlank()){
                    findViewById<TextView>(R.id.Expressao).text = valor.substring(0, valor.length-1)
                }
                findViewById<TextView>(R.id.Resultado).text = ""

                val Expressao = findViewById<TextView>(R.id.Expressao).text.toString()
                val ExpressaoL = Expressao.last()

                if (ExpressaoL.toString() == "." && !PodePonto){
                        PodePonto = true
                }
            }
            catch(e:java.lang.Exception){
                println(e)
            }

        }
        findViewById<TextView>(R.id.VerResultado).setOnClickListener(){
            try {
                val expressao = ExpressionBuilder(findViewById<TextView>(R.id.Expressao).text.toString()).build()
                val resultado = expressao.evaluate()
                val long_result = resultado.toLong()

                if(resultado == long_result.toDouble()){
                    findViewById<TextView>(R.id.Resultado).text = long_result.toString()
                }
                else{
                    findViewById<TextView>(R.id.Resultado).text = resultado.toString()
                }
                PodePonto=true;
            }
            catch(e: java.lang.Exception){
                print(e)
            }
        }

    }

    fun Acrescentar_uma_Expressao(Expressao: String, Limpar_dados: Boolean){

        if(findViewById<TextView>(R.id.Resultado).text.isNotEmpty()){
            findViewById<TextView>(R.id.Expressao).text = ""
        }

        if (Limpar_dados){
            findViewById<TextView>(R.id.Resultado).text = ""
            findViewById<TextView>(R.id.Expressao).append(Expressao)
        }
        else{
            findViewById<TextView>(R.id.Expressao).append(findViewById<TextView>(R.id.Resultado).text)
            findViewById<TextView>(R.id.Expressao).append(Expressao)
            findViewById<TextView>(R.id.Resultado).text = ""
        }

    }

    fun Verifica_Ultima_Expressao(ExpressaoEntrada: String){
        val Expressao = findViewById<TextView>(R.id.Expressao).text.toString()
        val ExpressaoL = Expressao.last()

        if (ExpressaoL.toString() == "+" || ExpressaoL.toString() == "-" || ExpressaoL.toString() == "*" || ExpressaoL.toString() == "/"){
            findViewById<TextView>(R.id.Expressao).text = Expressao.substring(0, Expressao.length-1)
            Acrescentar_uma_Expressao(ExpressaoEntrada, false)
        }
        else{
            Acrescentar_uma_Expressao(ExpressaoEntrada, false)
        }
        PodePonto = false
    }
}