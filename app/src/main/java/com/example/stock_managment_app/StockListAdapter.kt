package com.example.stock_managment_app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.stock_managment_app.R
import com.example.stock_managment_app.dataClass.Stock

class StockListAdapter(private val context: Context, private var stockList: MutableList<Stock>) : BaseAdapter() {

    override fun getCount(): Int {
        return stockList.size
    }

    override fun getItem(position: Int): Any {
        return stockList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_stock, parent, false)
        }

        val stock = getItem(position) as Stock

        val nameTextView = view?.findViewById<TextView>(R.id.nameTextView)
        val categoryTextView = view?.findViewById<TextView>(R.id.categoryTextView)
        val quantityTextView = view?.findViewById<TextView>(R.id.quantityTextView)
        val totalPriceTextView = view?.findViewById<TextView>(R.id.totalPriceTextView)

        nameTextView?.text = stock.name
        categoryTextView?.text = stock.category
        quantityTextView?.text = stock.quantity.toString()
        totalPriceTextView?.text = stock.totalPrice.toString()

        return view!!
    }

    fun updateData(newStockList: List<Stock>) {
        stockList.clear()
        stockList.addAll(newStockList)
        notifyDataSetChanged()
    }
}
