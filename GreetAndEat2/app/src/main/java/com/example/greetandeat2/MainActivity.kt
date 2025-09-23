package com.example.greetandeat2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.greetandeat2.R.id.rvRestaurants
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: RestaurantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup RecyclerView
        val recyclerView = findViewById<RecyclerView>(rvRestaurants)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RestaurantAdapter()
        recyclerView.adapter = adapter

        fetchRestaurants()
    }

    private fun fetchRestaurants() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.service.getRestaurants()
                if (response.isSuccessful) {
                    val restaurants = response.body()
                    Log.d("API_TEST", "Restaurants: $restaurants")
                    restaurants?.let {
                        adapter.submitList(it) // update RecyclerView with API data
                    }
                } else {
                    Log.e("API_TEST", "Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("API_TEST", "Exception: ${e.message}")
            }
        }
    }
}
