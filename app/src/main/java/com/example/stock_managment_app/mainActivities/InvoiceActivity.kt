package com.example.stock_managment_app.mainActivities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stock_managment_app.R
import com.example.stock_managment_app.SaleListAdapter
import com.example.stock_managment_app.database.BDApp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class InvoiceActivity : AppCompatActivity() {

    private lateinit var productNameEditText: EditText
    private lateinit var buyerEditText: EditText
    private lateinit var quantityEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var invoiceButton: Button
    private lateinit var calendar: View
    private lateinit var listView: ListView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice)

        productNameEditText = findViewById(R.id.productNameEditText)
        buyerEditText = findViewById(R.id.buyerEditText)
        quantityEditText = findViewById(R.id.quantityEditText)
        dateEditText = findViewById(R.id.dateEditText)
        priceEditText = findViewById(R.id.priceEditText)
        invoiceButton = findViewById(R.id.invoiceButton)
        calendar = findViewById(R.id.calendar)

        val dbHelper = BDApp(this)
        val companyName = intent.getStringExtra("CPName").toString()

        val sales = dbHelper.getAllSales(companyName)
        val adapter = SaleListAdapter(this, sales)
        listView = findViewById(R.id.listView)
        listView.adapter = adapter

        calendar.setOnClickListener {
            showDatePicker(dateEditText)
        }

        invoiceButton.setOnClickListener {
            val productName = productNameEditText.text.toString()
            val buyer = buyerEditText.text.toString()
            val quantityStr = quantityEditText.text.toString()
            val date = dateEditText.text.toString()
            val priceStr = priceEditText.text.toString()

            if (productName.isNotEmpty() && quantityStr.isNotEmpty() && priceStr.isNotEmpty()) {
                val quantity = quantityStr.toInt()
                val price = priceStr.toDouble()

                val success = dbHelper.invoiceProduct(productName, companyName, quantity, price)
                if (success) {
                    Toast.makeText(this, "Product invoiced successfully", Toast.LENGTH_SHORT).show()
                    productNameEditText.text.clear()
                    buyerEditText.text.clear()
                    quantityEditText.text.clear()
                    dateEditText.text.clear()
                    priceEditText.text.clear()
                } else {
                    Toast.makeText(this, "Failed to invoice product. Product not found or quantity exceeds stock.", Toast.LENGTH_SHORT).show()
                }

                dbHelper.addSale(productName, companyName, buyer, quantity, date, price)

            } else {
                Toast.makeText(this, "Please enter product name, quantity, and price", Toast.LENGTH_SHORT).show()
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
