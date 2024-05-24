package com.example.stock_managment_app.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.stock_managment_app.dataClass.Compte
import com.example.stock_managment_app.dataClass.Sale
import com.example.stock_managment_app.dataClass.Stock

class BDApp(context: Context) : SQLiteOpenHelper(context, "BDApp", null ,1  ){

    companion object {
        private const val TABLE_COMPTE = "Compte"
        private const val TABLE_STOCK = "Stock"
        private const val TABLE_SALES = "Sales"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
        "CREATE TABLE $TABLE_COMPTE (" +
                "username TEXT PRIMARY KEY, " +
                "email TEXT, " +
                "company TEXT, " +
                "phone TEXT, " +
                "password TEXT)"
        )
        db.execSQL(
        "CREATE TABLE $TABLE_STOCK(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "name TEXT, " +
                "company TEXT," +
                "category TEXT, " +
                "supplier TEXT, " +
                "quantity INTEGER, " +
                "date TEXT," +
                "totalPrice DOUBLE)"
        )
        db.execSQL(
            "CREATE TABLE $TABLE_SALES(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "name TEXT, " +
                    "company TEXT," +
                    "buyer TEXT," +
                    "quantity INTEGER, " +
                    "date TEXT," +
                    "totalPrice DOUBLE)"
        )

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COMPTE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STOCK")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SALES")
        onCreate(db)
    }

    fun ajouterCompte(username: String, email: String, company: String, phone: String, password: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("username", username)
            put("email", email)
            put("company", company)
            put("phone", phone)
            put("password", password)

        }
        db.insert(TABLE_COMPTE, null, values)
        db.close()
    }

    fun addStockItem(name: String, company: String, category: String, supplier: String, quantity: Int, date: String, price: Double): Long {
        val totalPrice = price * quantity
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("company", company)
            put("category", category)
            put("supplier", supplier)
            put("quantity", quantity)
            put("date", date)
            put("totalPrice", totalPrice)
        }
        val id = try {
            db.insertOrThrow(TABLE_STOCK, null, values)
        } catch (e: Exception) {
            // Log the error or show an error message
            Log.e("BDApp", "Error inserting stock item", e)
            -1
        } finally {
            db.close()
        }
        return id
    }


    @SuppressLint("Range")
    fun getAllStockItems(company: String): List<Stock> {
        val stockItems = mutableListOf<Stock>()
        val db = readableDatabase
        val selection = "company = ?"
        val selectionArgs = arrayOf(company)
        val cursor = db.query(
            TABLE_STOCK,
            arrayOf("id", "name", "company", "category", "supplier", "quantity", "date", "totalPrice"),
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val category = cursor.getString(cursor.getColumnIndex("category"))
            val supplier = cursor.getString(cursor.getColumnIndex("supplier"))
            val quantity = cursor.getInt(cursor.getColumnIndex("quantity"))
            val date = cursor.getString(cursor.getColumnIndex("date"))
            val totalPrice = cursor.getDouble(cursor.getColumnIndex("totalPrice"))

            val stockItem = Stock(id, name, company, category, supplier, quantity, date, totalPrice)
            stockItems.add(stockItem)
        }

        cursor.close()
        return stockItems
    }


    @SuppressLint("Range")
    fun getAllStockItemsByCategory(category: String, company: String): List<Stock> {
        val stockItems = mutableListOf<Stock>()
        val db = readableDatabase
        val selection = "category = ? AND company = ?"
        val selectionArgs = arrayOf(category, company)
        val cursor = db.query(
            TABLE_STOCK,
            arrayOf("id", "name", "company", "category", "supplier", "quantity", "date", "totalPrice"),
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val supplier = cursor.getString(cursor.getColumnIndex("supplier"))
            val quantity = cursor.getInt(cursor.getColumnIndex("quantity"))
            val date = cursor.getString(cursor.getColumnIndex("date"))
            val totalPrice = cursor.getDouble(cursor.getColumnIndex("totalPrice"))

            val stockItem = Stock(id, name, company, category, supplier, quantity, date, totalPrice)
            stockItems.add(stockItem)
        }

        cursor.close()
        return stockItems
    }


    fun deleteStockItem(id: Long): Boolean {
        val db = writableDatabase
        val whereClause = "id = ?"
        val whereArgs = arrayOf(id.toString())
        val deletedRows = db.delete(TABLE_STOCK, whereClause, whereArgs)
        db.close()
        return deletedRows > 0
    }


    @SuppressLint("Range")
    fun getAllUniqueSuppliers(company: String): List<Pair<String, String>> {
        val suppliers = mutableListOf<Pair<String, String>>()
        val db = readableDatabase
        val selection = "company = ?"
        val selectionArgs = arrayOf(company)
        val cursor = db.query(
            TABLE_STOCK,
            arrayOf("supplier", "category"),
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val uniqueSuppliers = mutableSetOf<String>()
        while (cursor.moveToNext()) {
            val supplier = cursor.getString(cursor.getColumnIndex("supplier"))
            val category = cursor.getString(cursor.getColumnIndex("category"))
            if (supplier !in uniqueSuppliers) {
                suppliers.add(Pair(supplier, category))
                uniqueSuppliers.add(supplier)
            }
        }

        cursor.close()
        return suppliers
    }


    @SuppressLint("Range")
    fun invoiceProduct(productName: String, companyName: String, quantity: Int, price: Double): Boolean {
        val db = writableDatabase
        val selection = "name = ? AND company = ?"
        val selectionArgs = arrayOf(productName, companyName)
        val cursor = db.query(
            TABLE_STOCK,
            arrayOf("quantity", "totalPrice"),
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            val currentQuantity = cursor.getInt(cursor.getColumnIndex("quantity"))
            val totalPrice = cursor.getDouble(cursor.getColumnIndex("totalPrice"))
            if (currentQuantity >= quantity) {
                val updatedQuantity = currentQuantity - quantity
                val updatedTotalPrice = totalPrice - (quantity * price)
                val values = ContentValues().apply {
                    put("quantity", updatedQuantity)
                    put("totalPrice", updatedTotalPrice)
                }
                val updateResult = db.update(TABLE_STOCK, values, selection, selectionArgs)
                cursor.close()
                return updateResult > 0
            }
        }

        cursor.close()
        return false
    }

    fun addSale(name: String, company: String, buyer: String, quantity: Int, date: String, price: Double): Long {
        val db = writableDatabase
        val totalPrice = price * quantity
        val values = ContentValues().apply {
            put("name", name)
            put("company", company)
            put("buyer", buyer)
            put("quantity", quantity)
            put("date", date)
            put("totalPrice", totalPrice)
        }
        val id = try {
            db.insertOrThrow(TABLE_SALES, null, values)
        } catch (e: Exception) {
            // Log the error or show an error message
            Log.e("BDApp", "Error inserting sale item", e)
            -1
        } finally {
            db.close()
        }
        return id
    }

    @SuppressLint("Range")
    fun getAllSales(company: String): List<Sale> {
        val salesList = mutableListOf<Sale>()
        val db = readableDatabase
        val selection = "company = ?"
        val selectionArgs = arrayOf(company)
        val cursor = db.query(
            TABLE_SALES,
            arrayOf("id", "name", "company", "buyer", "quantity", "date", "totalPrice"),
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val buyer = cursor.getString(cursor.getColumnIndex("buyer"))
            val quantity = cursor.getInt(cursor.getColumnIndex("quantity"))
            val date = cursor.getString(cursor.getColumnIndex("date"))
            val totalPrice = cursor.getDouble(cursor.getColumnIndex("totalPrice"))

            val sale = Sale(id, name, company, buyer, quantity, date, totalPrice)
            salesList.add(sale)
        }

        cursor.close()
        return salesList
    }



    @SuppressLint("Range")
    fun getCompanyByUsernameOrEmail(identifier: String): String? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_COMPTE,
            arrayOf("company"),
            "username = ? OR email = ?",
            arrayOf(identifier, identifier),
            null,
            null,
            null
        )

        var company: String? = null
        if (cursor.moveToFirst()) {
            company = cursor.getString(cursor.getColumnIndex("company"))
        }

        cursor.close()
        return company
    }



    fun checkCredentials(identifier: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            "Compte",
            null,
            "(email = ? OR username = ?) AND password = ?",
            arrayOf(identifier, identifier, password),
            null,
            null,
            null
        )

        val result = cursor.count > 0
        cursor.close()

        // Add logging for debugging
        Log.d("CheckCredentials", "Identifier: $identifier, Password: $password, Result: $result")

        return result
    }

    fun getAllAccounts(): List<Compte> {
        val accountsList = mutableListOf<Compte>()
        val db = readableDatabase
        val cursor = db.query(
            "Compte",
            null,
            null,
            null,
            null,
            null,
            null
        )

        return accountsList
    }

    @SuppressLint("Range")
    fun getAccountByUsernameOrEmail(identifier: String): Compte? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_COMPTE,
            arrayOf("username", "email", "company", "phone"),
            "username = ? OR email = ?",
            arrayOf(identifier, identifier),
            null,
            null,
            null
        )

        var compte: Compte? = null
        if (cursor.moveToFirst()) {
            compte = Compte(
                cursor.getString(cursor.getColumnIndex("username")),
                cursor.getString(cursor.getColumnIndex("email")),
                cursor.getString(cursor.getColumnIndex("company")),
                cursor.getString(cursor.getColumnIndex("phone")),
                "" // You can set the password to empty string or null, as it's not needed here
            )
        }

        cursor.close()
        return compte
    }

    fun updateAccountByUsernameOrEmail(identifier: String, newEmail: String, newCompany: String, newPhone: String, newUsername: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("email", newEmail)
            put("company", newCompany)
            put("phone", newPhone)
            put("username", newUsername)
        }

        val whereClause = "username = ? OR email = ?"
        val whereArgs = arrayOf(identifier, identifier)

        val rowsAffected = db.update(TABLE_COMPTE, values, whereClause, whereArgs)
        db.close()

        return rowsAffected > 0
    }







}
