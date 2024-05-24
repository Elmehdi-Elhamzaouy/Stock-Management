package com.example.stock_managment_app.mainActivities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.stock_managment_app.R
import com.example.stock_managment_app.database.BDApp

class SuppliersActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var dbHelper: BDApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suppliers)

        listView = findViewById(R.id.supplierListView)

        dbHelper = BDApp(this)
        val companyName = intent.getStringExtra("CPName").toString()
        val suppliers = dbHelper.getAllUniqueSuppliers(companyName)

        val adapter = SupplierListAdapter(this, suppliers)
        listView.adapter = adapter
    }

    private class SupplierListAdapter(
        context: AppCompatActivity,
        private val suppliers: List<Pair<String, String>>
    ) : ArrayAdapter<Pair<String, String>>(context, R.layout.list_item_supplier, suppliers) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var itemView = convertView
            if (itemView == null) {
                itemView = LayoutInflater.from(context).inflate(R.layout.list_item_supplier, parent, false)
            }

            val supplierNameTextView = itemView?.findViewById<TextView>(R.id.supplier_name)
            val categoryNameTextView = itemView?.findViewById<TextView>(R.id.category_name)

            val supplier = getItem(position)
            supplierNameTextView?.text = supplier?.first
            categoryNameTextView?.text = supplier?.second

            return itemView!!
        }
    }
}
