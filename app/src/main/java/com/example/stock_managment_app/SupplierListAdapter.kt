package com.example.stock_managment_app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SupplierListAdapter(context: Context, suppliers: List<Pair<String, String>>) :
    ArrayAdapter<Pair<String, String>>(context, R.layout.list_item_supplier, suppliers) {

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
