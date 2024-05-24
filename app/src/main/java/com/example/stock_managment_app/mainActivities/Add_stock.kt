package com.example.stock_managment_app.mainActivities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.stock_managment_app.R
import com.example.stock_managment_app.database.BDApp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Add_stock : AppCompatActivity() {

    private lateinit var calender: View
    private lateinit var addBtn: Button
    private lateinit var companyName: TextView
    private lateinit var nameEditText: EditText
    private lateinit var categoryEditText: Spinner
    private lateinit var supplierEditText: EditText
    private lateinit var quantityEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var priceEditText: EditText


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_stock)

        companyName = findViewById(R.id.companyName)
        nameEditText = findViewById(R.id.name)
        categoryEditText = findViewById(R.id.category)
        supplierEditText = findViewById(R.id.supplier)
        quantityEditText = findViewById(R.id.quantity)
        dateEditText = findViewById(R.id.date)
        priceEditText = findViewById(R.id.price)
        calender = findViewById(R.id.calender)
        addBtn = findViewById(R.id.add_stock)

        val cpName = intent.getStringExtra("COMPName")
        companyName.setText(cpName)

        calender.setOnClickListener {
            showDatePicker(dateEditText)
        }

        addBtn.setOnClickListener {
            val name = nameEditText.text.toString()
            val category = categoryEditText.selectedItem.toString()
            val supplier = supplierEditText.text.toString()
            val quantity = quantityEditText.text.toString().toIntOrNull() ?: 0
            val date = dateEditText.text.toString()
            val price = priceEditText.text.toString().toDoubleOrNull() ?: 0.0

            val dbHelper = BDApp(this)
            val cpName = companyName.text.toString()

            val result = dbHelper.addStockItem(name, cpName, category, supplier, quantity, date, price)
            if (result == -1L) {
                Toast.makeText(this, "Failed to add stock item", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Stock item added successfully", Toast.LENGTH_SHORT).show()
                finish() // Close the activity after adding the stock item
            }

        }


    }

    private fun showDatePicker(field: EditText) {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.time)
                field.setText(formattedDate)

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }
}