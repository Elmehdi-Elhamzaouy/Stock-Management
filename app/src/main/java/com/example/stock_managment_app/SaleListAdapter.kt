package com.example.stock_managment_app

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.stock_managment_app.dataClass.Sale

class SaleListAdapter(
    context: Context,
    private val sales: List<Sale>
) : ArrayAdapter<Sale>(context, R.layout.list_item_sale, sales) {

    @SuppressLint("MissingInflatedId")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val sale = sales[position]
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.list_item_sale, parent, false)

        val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        val buyerTextView = view.findViewById<TextView>(R.id.buyerTextView)
        val quantityTextView = view.findViewById<TextView>(R.id.quantityTextView)
        val dateTextView = view.findViewById<TextView>(R.id.dateTextView)
        val totalPriceTextView = view.findViewById<TextView>(R.id.totalPriceTextView)

        nameTextView.text = sale.name
        buyerTextView.text = sale.buyer
        quantityTextView.text = sale.quantity.toString()
        dateTextView.text = sale.date
        totalPriceTextView.text = sale.totalPrice.toString()

        return view
    }
}

