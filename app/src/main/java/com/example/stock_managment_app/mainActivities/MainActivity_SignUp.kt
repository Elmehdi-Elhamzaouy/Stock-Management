package com.example.stock_managment_app.mainActivities


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stock_managment_app.dataClass.Compte
import com.example.stock_managment_app.database.BDApp
import com.example.stock_managment_app.R

class MainActivity_SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_sign_up)

        val signBtn = findViewById<Button>(R.id.signbtn)

        signBtn.setOnClickListener {
            val userEditText = findViewById<EditText>(R.id.user)
            val emailEditText = findViewById<EditText>(R.id.email)
            val companyEditText = findViewById<EditText>(R.id.company)
            val phoneEditText = findViewById<EditText>(R.id.phone)
            val passEditText = findViewById<EditText>(R.id.pass)
            val passCoEditText = findViewById<EditText>(R.id.passco)

            val user = userEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val company = companyEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val pass = passEditText.text.toString().trim()
            val passCo = passCoEditText.text.toString().trim()

            if (user.isEmpty() || email.isEmpty() ||company.isEmpty() ||phone.isEmpty() || pass.isEmpty() || passCo.isEmpty()) {
                // Display a Toast indicating that all fields must be filled
                Toast.makeText(applicationContext, "All fields must be filled", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Validate username
            val usernameRegex = "^[a-zA-Z0-9_-]{3,15}\$"
            if (!user.matches(usernameRegex.toRegex())) {
                Toast.makeText(applicationContext, "Invalid username format", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Validate email
            val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
            if (!email.matches(emailRegex.toRegex())) {
                Toast.makeText(applicationContext, "Invalid email format", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Validate password
            val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).*\$"
            if (!pass.matches(passwordRegex.toRegex())) {
                Toast.makeText(applicationContext, "Invalid password format", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Confirm password
            if (pass != passCo) {
                Toast.makeText(applicationContext, "Passwords do not match", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Rest of your code...


            val db = BDApp(applicationContext)

            db.ajouterCompte(user, email, company, phone, pass)

            Toast.makeText(applicationContext, "Successfully Added", Toast.LENGTH_LONG).show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }

        val haveAcc = findViewById<TextView>(R.id.haveAc)
        haveAcc.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
