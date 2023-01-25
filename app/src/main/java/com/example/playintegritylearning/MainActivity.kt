package com.example.playintegritylearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.playintegritylearning.databinding.ActivityMainBinding
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.IntegrityTokenRequest
import com.google.android.play.core.integrity.IntegrityTokenResponse
import io.reactivex.schedulers.Schedulers
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.checkBtn.setOnClickListener {
            getIntegrityToken()
        }
    }

    private fun getIntegrityToken() {
        val nonce: String = generateNonce()
        // Create an instance of a manager.
        val integrityManager = IntegrityManagerFactory.create(applicationContext)
        // Request the integrity token by providing a nonce.
        val integrityTokenResponse = integrityManager.requestIntegrityToken(
            IntegrityTokenRequest.builder()
                .setCloudProjectNumber(345641564)// CloudProjectNumber form google cloud
                .setNonce(nonce)
                .build()
        )
        integrityTokenResponse.addOnSuccessListener { integrityTokenResponse: IntegrityTokenResponse ->
            val integrityToken = integrityTokenResponse.token()
            binding.textView2.text = integrityToken
//            In here you should send integrityToken to your app's backend
//            sendIntegrityTokenToServer(integrityToken)
        }
        integrityTokenResponse.addOnFailureListener { e: Exception? ->
            Log.e("IntegrityToken error", e?.message.toString())

        }
    }

    fun generateNonce(): String {
        val length = 50
        var nonce = ""
        val allowed = "test_test_test"
//        In here you get unique value form your server
//        val allowed = getUniqueValue()
        for (i in 0 until length) {
            nonce = nonce + allowed[Math.floor(Math.random() * allowed.length).toInt()].toString()
        }
        return nonce
    }


    fun sendIntegrityTokenToServer(integrityToken: String) {

        val service = RetrofitHelper.getInstance().create(ServiceApi::class.java)
        val call = service.verifyIntegrityToken(integrityToken)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()
                    Toast.makeText(this@MainActivity, weatherResponse, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(calll: Call<String>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG)
            }
        })
    }

    fun getUniqueValue(): String {
        var uniqueValue = ""
        val service = RetrofitHelper.getInstance().create(ServiceApi::class.java)
        val call = service.getUniqueValue()
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()
                    uniqueValue = weatherResponse.toString()
                    Toast.makeText(this@MainActivity, weatherResponse, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(calll: Call<String>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: " + t.message, Toast.LENGTH_LONG)
            }
        })
        return uniqueValue
    }

}