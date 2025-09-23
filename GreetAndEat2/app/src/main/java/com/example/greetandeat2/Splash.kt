package com.example.greetandeat2

import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Splash : AppCompatActivity() {

    private val SPLASH_DELAY: Long = 3000 // 3 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val splashImage = findViewById<ImageView>(R.id.splash_image)
        val welcomeText = findViewById<TextView>(R.id.welcome_text)

        // Apply gradient effect to text
        welcomeText.post {
            val textShader = LinearGradient(
                0f, 0f, welcomeText.width.toFloat(), welcomeText.textSize,
                intArrayOf(0xFFFFD200.toInt(), 0xFFDD2476.toInt()), // gold → pink
                null,
                Shader.TileMode.CLAMP
            )
            welcomeText.paint.shader = textShader
        }

        // Animate logo (scale + fade)
        splashImage.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(1500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        // Animate text (fade + pulse)
        welcomeText.animate()
            .alpha(1f)
            .setDuration(1200)
            .setStartDelay(1000)
            .withEndAction {
                // Pulse effect
                welcomeText.animate()
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .setDuration(500)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .withEndAction {
                        welcomeText.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(500)
                            .start()
                    }
                    .start()
            }
            .start()

        // Move to Login screen after delay
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, Login::class.java))
            finish()
        }, SPLASH_DELAY)
    }
}
