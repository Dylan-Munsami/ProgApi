package com.example.greetandeat2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Retrieve cart items and calculate total
        val cartItems = intent.getParcelableArrayListExtra<MenuItem>("cartItems") ?: arrayListOf()
        val total = cartItems.sumOf { it.price * it.quantity }

        // Find UI components
        val tvAmount = findViewById<TextView>(R.id.tvAmount)
        val tvSummary = findViewById<TextView>(R.id.tvSummary)
        val btnPay = findViewById<Button>(R.id.btnPay)

        // Build order summary text
        val summary = if (cartItems.isNotEmpty()) {
            cartItems.joinToString("\n") { item ->
                "${item.name} x${item.quantity} - R${item.price * item.quantity}"
            }
        } else {
            "No items in cart."
        }
        val backButton = findViewById<Button>(R.id.btnBack)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        // Display summary and total
        tvSummary.text = summary
        tvAmount.text = "Total: R$total"

        // Handle payment button click
        btnPay.setOnClickListener {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"
            Toast.makeText(this, "Payment Successful! Thank you, $userId", Toast.LENGTH_SHORT).show()

            // Navigate to TrackingActivity and clear backstack
            val intent = Intent(this, TrackingActivity::class.java)
            intent.putParcelableArrayListExtra("cartItems", cartItems)
            intent.putExtra("totalAmount", total)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
