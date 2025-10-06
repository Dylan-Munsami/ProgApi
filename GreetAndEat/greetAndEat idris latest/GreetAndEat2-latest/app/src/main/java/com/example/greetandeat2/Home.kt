package com.example.greetandeat2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.core.view.WindowInsetsCompat

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

        // Buttons
        val btnMain = findViewById<Button>(R.id.btnMain)
        val btnRewards = findViewById<Button>(R.id.btnRewards)
        val btnSettings = findViewById<Button>(R.id.btnSettings)
        val btnTracking = findViewById<Button>(R.id.btnTracking)

        // Navigation
        btnMain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java)) // When the "Main" button is clicked, navigate to the MainActivity screen
        }

        btnRewards.setOnClickListener {
            startActivity(Intent(this, RewardsActivity::class.java))
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, Setting::class.java))
        }

        btnTracking.setOnClickListener {
            startActivity(Intent(this, TrackingActivity ::class.java))
        }
    }
}
