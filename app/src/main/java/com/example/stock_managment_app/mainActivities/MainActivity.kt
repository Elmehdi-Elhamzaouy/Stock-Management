package com.example.stock_managment_app.mainActivities

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.stock_managment_app.dataClass.Compte
import com.example.stock_managment_app.database.BDApp
import com.example.stock_managment_app.R

class MainActivity : AppCompatActivity() {

    private lateinit var userEditText: EditText
    private lateinit var passEditText: EditText
    private lateinit var logBtn: Button
    private lateinit var signUp: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        signUp = findViewById(R.id.signupbtn)
        userEditText = findViewById(R.id.user)
        passEditText = findViewById(R.id.pass)
        logBtn = findViewById(R.id.loginbtn)

        signUp.setOnClickListener {
            val intent = Intent(this, MainActivity_SignUp::class.java)
            startActivity(intent)
        }

        // Assuming you have a reference to your database helper
        val dbHelper = BDApp(this)

        // Create a handler for delayed execution
        val handler = Handler()

        // Perform the action when the login button is clicked
        logBtn.setOnClickListener {
            val email = userEditText.text.toString()
            val pass = passEditText.text.toString()

            // Show loading indicator on the button
            logBtn.text = "Loading..."
            logBtn.isEnabled = false // Disable the button during the delay

            // Introduce a delay using a Handler
            handler.postDelayed({
                // Check if the entered credentials exist in the database
                val isCredentialsValid = dbHelper.checkCredentials(email, pass)

                // Reset button text and enable it after the delay
                logBtn.text = "Login"
                logBtn.isEnabled = true

                if (isCredentialsValid) {
                    // Retrieve the account information
                    val account = dbHelper.getAllAccounts().find { it.email == email }
                    val company = dbHelper.getCompanyByUsernameOrEmail(email)

                    // Perform your action for regular users here
                    Toast.makeText(applicationContext, "User logged in!", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity_MainPage::class.java)
                    intent.putExtra("USER_EMAIL", email)
                    intent.putExtra("COMPANY", company)
                    startActivity(intent)


                } else {
                    Toast.makeText(applicationContext, "Invalid credentials", Toast.LENGTH_LONG).show()
                }
            }, 2000) // 2000 milliseconds (2 seconds) delay, adjust as needed
        }
    }

}