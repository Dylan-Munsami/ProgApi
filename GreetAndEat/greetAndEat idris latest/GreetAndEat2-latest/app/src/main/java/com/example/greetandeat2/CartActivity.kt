package com.example.greetandeat2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class CartActivity : AppCompatActivity() {

    private lateinit var adapter: CartAdapter
    private lateinit var auth: FirebaseAuth
    private val cartItems = mutableListOf<MenuItem>()
    private lateinit var tvTotal: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        auth = FirebaseAuth.getInstance()

        // Get UI references
        val recyclerView = findViewById<RecyclerView>(R.id.rvCart)
        tvTotal = findViewById(R.id.tvCartTotal)
        val btnPay = findViewById<Button>(R.id.btnPay)

        // Retrieve cart items from intent
        cartItems.addAll(intent.getParcelableArrayListExtra<MenuItem>("cartItems") ?: arrayListOf())

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CartAdapter(cartItems) { itemToRemove ->
            // Handle removing items
            cartItems.remove(itemToRemove)
            updateTotal()
            Toast.makeText(this, "${itemToRemove.name} removed", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        // Display initial total
        updateTotal()
        val backButton = findViewById<Button>(R.id.btnBack)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Proceed to payment
        btnPay.setOnClickListener {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentUser = auth.currentUser
            if (currentUser == null) {
                Toast.makeText(this, "Please log in to continue", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, PaymentActivity::class.java)
            intent.putParcelableArrayListExtra("cartItems", ArrayList(cartItems))
            startActivity(intent)
        }
    }

    /**
     * Recalculate and update the total amount in the cart.
     */
    private fun updateTotal() {
        val total = cartItems.sumOf { it.price * it.quantity }
        tvTotal.text = "Total: R$total"
        adapter.notifyDataSetChanged()
    }
}
