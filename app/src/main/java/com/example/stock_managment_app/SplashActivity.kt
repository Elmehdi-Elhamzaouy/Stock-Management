package com.example.stock_managment_app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.stock_managment_app.mainActivities.MainActivity
import com.example.stock_managment_app.mainActivities.MainActivity_MainPage

class SplashActivity : AppCompatActivity() {

    private val splashTimeOut: Long = 3000 // 3 secondes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            // Après le délai, démarrer l'activité principale
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, splashTimeOut)
    }
}
