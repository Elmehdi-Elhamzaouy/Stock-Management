package com.example.stock_managment_app.mainActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.example.stock_managment_app.R
import com.example.stock_managment_app.StockListAdapter
import com.example.stock_managment_app.dataClass.Stock
import com.example.stock_managment_app.database.BDApp

class InventoryActivity : AppCompatActivity() {

    private lateinit var addBtn: Button
    private lateinit var listView: ListView
    private lateinit var spinner: Spinner
    private lateinit var adapter: StockListAdapter
    private lateinit var dbHelper: BDApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        addBtn = findViewById(R.id.add)
        spinner = findViewById(R.id.category)
        listView = findViewById(R.id.listView)

        val companyName = intent.getStringExtra("CPName")

        addBtn.setOnClickListener {
            val intent = Intent(this, Add_stock::class.java)
            intent.putExtra("COMPName", companyName)
            startActivity(intent)
        }

        // Initialize the adapter with an empty list
        adapter = StockListAdapter(this, mutableListOf())
        listView.adapter = adapter

        // Populate the list view with stock items
        dbHelper = BDApp(this)
        updateStockItems()

        // Set long click listener for list view items
        listView.setOnItemLongClickListener { parent, view, position, id ->
            val stockItem = adapter.getItem(position) as Stock
            showDeleteConfirmationDialog(stockItem)
            true // Consume the long click event
        }


        // Set up the spinner
        val categories = resources.getStringArray(R.array.category_array)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        // Set up the spinner item selection listener
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = parent?.getItemAtPosition(position).toString()
                if (selectedCategory == "Category") {
                    // If "Category" is selected, display all items
                    updateStockItems()
                } else {
                    // Otherwise, display items for the selected category
                    val stockItems = dbHelper.getAllStockItemsByCategory(selectedCategory, companyName.toString())
                    adapter.updateData(stockItems)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun updateStockItems() {
        val companyName = intent.getStringExtra("CPName").toString()
        val stockItems = dbHelper.getAllStockItems(companyName)
        adapter.updateData(stockItems)
    }

    private fun showDeleteConfirmationDialog(stockItem: Stock) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Are you sure you want to delete ${stockItem.name}?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                dbHelper.deleteStockItem(stockItem.id)
                updateStockItems()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.dismiss()
            }
        val alert = dialogBuilder.create()
        alert.setTitle("Delete Stock Item")
        alert.show()
    }
}

