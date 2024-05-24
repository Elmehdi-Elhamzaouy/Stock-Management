package com.example.stock_managment_app.mainActivities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.stock_managment_app.R
import com.example.stock_managment_app.database.BDApp

class MainActivity_MainPage : AppCompatActivity() {

    private lateinit var company: TextView
    private lateinit var product: TextView
    private lateinit var sales: TextView
    private lateinit var dbHelper: BDApp
    private var companyName: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_main_page)

        dbHelper = BDApp(this)

        company = findViewById(R.id.company)
        product = findViewById(R.id.product)
        sales = findViewById(R.id.sales)

        companyName = intent.getStringExtra("COMPANY")
        company.text = "Welcome $companyName"

        updateProductCount()

        // Find the CardViews
        val invoiceCard = findViewById<CardView>(R.id.invoiceCard)
        val inventoryCard = findViewById<CardView>(R.id.inventoryCard)
        val salesAnalyticsCard = findViewById<CardView>(R.id.salesAnalyticsCard)
        val suppliersCard = findViewById<CardView>(R.id.suppliersCard)
        val settingsCard = findViewById<CardView>(R.id.settingsCard)

        invoiceCard.setOnClickListener {
            val intent = Intent(this, InvoiceActivity::class.java)
            intent.putExtra("CPName", companyName)
            startActivity(intent)
        }

        inventoryCard.setOnClickListener {
            val intent = Intent(this, InventoryActivity::class.java)
            intent.putExtra("CPName", companyName)
            startActivity(intent)
        }

        suppliersCard.setOnClickListener {
            val intent = Intent(this, SuppliersActivity::class.java)
            intent.putExtra("CPName", companyName)
            startActivity(intent)
        }

        val username = intent.getStringExtra("USER_EMAIL")

        settingsCard.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("USER", username)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        updateProductCount()
        updateSalesCount()
    }

    private fun updateProductCount() {
        val pro = dbHelper.getAllStockItems(companyName.toString()).size
        product.text = pro.toString()
    }
    private fun updateSalesCount() {
        val pro = dbHelper.getAllSales(companyName.toString()).size
        sales.text = pro.toString()
    }
}
