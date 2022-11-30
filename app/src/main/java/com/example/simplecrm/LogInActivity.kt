package com.example.simplecrm

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.simplecrm.databinding.ActivityLogInBinding
import com.marsad.stylishdialogs.StylishAlertDialog

class LogInActivity : AppCompatActivity() {
    lateinit var binding: ActivityLogInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        binding.loginBtn.setOnClickListener {
            val userName = binding.loginUsernameEt.text.toString()
            val password = binding.loginPasswordEt.text.toString()
            if (userName != "admin" || password != "admin") {
                Toast.makeText(this, "Please enter valid inputs (admin,admin)", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val alertDialog = StylishAlertDialog(this, StylishAlertDialog.PROGRESS)
                alertDialog.titleText = "Signing in..."
                alertDialog.cancellable = false
                alertDialog.show()
                Handler().postDelayed({
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }, 2000)
            }
        }
        setContentView(binding.root)
    }
}