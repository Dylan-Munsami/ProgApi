package com.example.greetandeat2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MenuActivity : AppCompatActivity() {

    private lateinit var adapter: MenuAdapter
    private val cart = mutableListOf<MenuItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val restaurantName = intent.getStringExtra("restaurantName") ?: "Restaurant"
        val restaurantId = intent.getStringExtra("restaurantId") ?: ""

        val tvTitle = findViewById<TextView>(R.id.tvRestaurantTitle)
        val recyclerView = findViewById<RecyclerView>(R.id.rvMenu)
        val btnCheckout = findViewById<Button>(R.id.btnCheckout)

        tvTitle.text = restaurantName

        // Use API to fetch menu if available
        val menuList = intent.getParcelableArrayListExtra<MenuItem>("menu") ?: arrayListOf()
        adapter = MenuAdapter(menuList) { menuItem ->
            cart.add(menuItem)
            Toast.makeText(this, "${menuItem.name} added to cart", Toast.LENGTH_SHORT).show()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val backButton = findViewById<Button>(R.id.btnBack)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        btnCheckout.setOnClickListener {
            if (cart.isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, CartActivity::class.java)
                intent.putParcelableArrayListExtra("cartItems", ArrayList(cart))
                startActivity(intent)
            }
        }

    }
}
