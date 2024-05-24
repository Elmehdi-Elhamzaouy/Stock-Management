package com.example.stock_managment_app.mainActivities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.stock_managment_app.R
import com.example.stock_managment_app.database.BDApp

class SettingsActivity : AppCompatActivity() {

    private lateinit var emailTextView: TextView
    private lateinit var compTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var emailEdit: View
    private lateinit var compEdit: View
    private lateinit var phoneEdit: View
    private lateinit var userEdit: View
    private lateinit var username: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        emailTextView = findViewById(R.id.email)
        compTextView = findViewById(R.id.company)
        phoneTextView = findViewById(R.id.phone)
        emailEdit = findViewById(R.id.editEmail)
        compEdit = findViewById(R.id.editComp)
        phoneEdit = findViewById(R.id.editPhone)
        userEdit = findViewById(R.id.editUser)
        username = findViewById(R.id.username)

        // Get the username or email from the intent
        val user = intent.getStringExtra("USER")
        username.text = user

        // Assuming you have a reference to your database helper
        val dbHelper = BDApp(this)
        val userOrEmail = username.text.toString()
        val account = dbHelper.getAccountByUsernameOrEmail(userOrEmail)

        // Display the account information in the views
        emailTextView.text = account?.email
        compTextView.text = account?.company
        phoneTextView.text = account?.phone
        // Get the account information for the username or email

        userEdit.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.edit_username_dialog, null)
            val dialog = AlertDialog.Builder(this)
                .setTitle("Edit Username")
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    val newUsername = dialogView.findViewById<EditText>(R.id.editUsername).text.toString()
                    dbHelper.updateAccountByUsernameOrEmail(username.text.toString(), account?.email ?: "", account?.company ?: "", account?.phone ?: "", newUsername)
                    username.text = newUsername
                }
                .setNegativeButton("Cancel", null)
                .create()
            dialog.show()
        }



        emailEdit.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.edit_email_dialog, null)
            val dialog = AlertDialog.Builder(this)
                .setTitle("Edit Email")
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    val newEmail = dialogView.findViewById<EditText>(R.id.editEmail).text.toString()
                    dbHelper.updateAccountByUsernameOrEmail(username.text.toString(), newEmail, account?.company ?: "", account?.phone ?: "", account?.username ?: "")
                    emailTextView.text = newEmail
                }
                .setNegativeButton("Cancel", null)
                .create()
            dialog.show()
        }

        compEdit.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.edit_company_dialog, null)
            val dialog = AlertDialog.Builder(this)
                .setTitle("Edit Company")
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    val newCompany = dialogView.findViewById<EditText>(R.id.editCompany).text.toString()
                    dbHelper.updateAccountByUsernameOrEmail(username.text.toString(), account?.email ?: "", newCompany, account?.phone ?: "", account?.username ?: "")
                    compTextView.text = newCompany
                }
                .setNegativeButton("Cancel", null)
                .create()
            dialog.show()
        }

        phoneEdit.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.edit_phone_dialog, null)
            val dialog = AlertDialog.Builder(this)
                .setTitle("Edit Phone")
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    val newPhone = dialogView.findViewById<EditText>(R.id.editPhone).text.toString()
                    dbHelper.updateAccountByUsernameOrEmail(username.text.toString(), account?.email ?: "", account?.company ?: "", newPhone, account?.username ?: "")
                    phoneTextView.text = newPhone
                }
                .setNegativeButton("Cancel", null)
                .create()
            dialog.show()
        }



    }
}