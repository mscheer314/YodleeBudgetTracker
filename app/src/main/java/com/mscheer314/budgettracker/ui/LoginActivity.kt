// Copyright (c) 2019 Yodlee, Inc. All Rights Reserved.
// Licensed under the MIT License. See `LICENSE` for details.
package com.mscheer314.budgettracker.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.android.databinding.ActivityLoginBinding
import com.mscheer314.budgettracker.api.AccountRetriever
import com.mscheer314.budgettracker.data.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val accountRetriever = AccountRetriever()
    private lateinit var loginToken: Token

    private val loginCallback = object : Callback<Token> {
        override fun onFailure(call: Call<Token>, t: Throwable) {
            Log.e("MainActivity", "Problem calling Yodlee API ${t.message}")
        }

        override fun onResponse(call: Call<Token>, response: Response<Token>) {
            Log.i("MainActivity", "Token Call successful!!!!!!")
            Log.i("MainActivity", response.toString())
            response.isSuccessful.let {
                loginToken = response.body()!!
                Log.i(
                        "MainActivity",
                        "accessToken: ${loginToken.token.accessToken}\n" +
                                "expiresIn: ${loginToken.token.issuedAt}\n" +
                                "issuedAt: ${loginToken.token.expiresIn}"
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.signInBtn.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java).apply {
                putExtra("token", loginToken.token.accessToken)
            }
            startActivity(intent)
        }
        if (isNetworkConnected()) {
            accountRetriever.login(loginCallback)
        } else {
            AlertDialog.Builder(this).setTitle("No Internet Connection")
                    .setMessage("Please check your internet connection and try again")
                    .setPositiveButton(android.R.string.ok) { _, _ -> }
                    .setIcon(android.R.drawable.ic_dialog_alert).show()
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}