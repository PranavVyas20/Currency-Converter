package com.example.converterapp

import android.annotation.SuppressLint
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import java.math.RoundingMode


class MainActivity : AppCompatActivity() {
    lateinit var unit1:String
    lateinit var unit2:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mySpinner = findViewById<Spinner>(R.id.mySpinner)
        val myspinner2 = findViewById<Spinner>(R.id.mySpinner2)
        val button = findViewById<Button>(R.id.button)
        val convertedAmountTv:TextView = findViewById(R.id.convertedCunnrencyTV)
        val enterAmount = findViewById<EditText>(R.id.enterAmountTV)
        val textView: TextView = findViewById(R.id.convertedCunnrencyTV)

        val currencies = listOf<String>(
            "INR","USD","LKR","CAD","PKR","EUR","YEN","HKD","IDR","KRW","MXN",
            "RUB","SGD","AED","NZD","EGP","AFN","GBP","IQD","ZAR","THB",
            "MYR","TRY")

        mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (parent != null) {
                    val currentSelected:String = currencies[position]
                    Log.d("chal gya2",currentSelected)
//                    Toast.makeText(this@MainActivity,"${parent.getItemAtPosition(position)} ${currencies[position]}",Toast.LENGTH_SHORT).show()
                    unit1 = currentSelected
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        myspinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (parent != null) {
                    val currentSelected:String = currencies[position]
                    Log.d("chal gya",currentSelected)
//                    Toast.makeText(this@MainActivity,"${parent.getItemAtPosition(position)} ${currencies[position]}",Toast.LENGTH_SHORT).show()
                    unit2 = currentSelected
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        button.setOnClickListener {
            val amount = enterAmount.text
            convertedAmountTv.text = "Loading..."
            convertedAmountTv.visibility = View.VISIBLE
            Toast.makeText(this,"$unit1 $unit2",Toast.LENGTH_SHORT).show()
            textView.text = "Loading.."
            getCurrency(textView,amount)
        }
    }
    fun getCurrency(textView: TextView, userAmount: Editable?){
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://currency-converter13.p.rapidapi.com/convert?from=$unit2&to=$unit1&amount=$userAmount")
            .get()
            .addHeader("x-rapidapi-host", "currency-converter13.p.rapidapi.com")
            .addHeader("x-rapidapi-key", "38bde84021msh3f0a1ccdb2ba88bp1cfdd9jsn960570ebf832")
            .build()

        val response = client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(this@MainActivity,"Error fetching data",Toast.LENGTH_SHORT).show()
                Log.d("error fetch currency","ni chal ra re bc")
                return
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: Response) {
                val receivedBody = response.body?.string()
                if (receivedBody != null) {
                    Log.d("chal gya",receivedBody)
                    val gson = GsonBuilder().create()
                    val finalReveived = gson.fromJson(receivedBody,conversion2::class.java)

                    runOnUiThread{
                        val convertedText = finalReveived.amount.toBigDecimal().setScale(2,RoundingMode.HALF_DOWN)
                        textView.text = "$convertedText $unit1"
                    }
                }
            }
        })
    }





}