package com.example.greetandeat2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: RestaurantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.rvRestaurants)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val restaurants = listOf(
            Restaurant("1", "Burger King", listOf(
                MenuItem("1", "Whopper", 60.0),
                MenuItem("2", "Cheese Burger", 50.0),
                MenuItem("3", "Chicken Nuggets", 40.0),
                MenuItem("4", "Fries", 25.0),
                MenuItem("5", "Coke", 15.0),
                MenuItem("6", "Ice Cream", 20.0)
            )),
            Restaurant("2", "Pizza Hut", listOf(
                MenuItem("1", "Margherita Pizza", 55.0),
                MenuItem("2", "Pepperoni Pizza", 65.0),
                MenuItem("3", "Veggie Pizza", 50.0),
                MenuItem("4", "Garlic Bread", 25.0),
                MenuItem("5", "Coke", 15.0),
                MenuItem("6", "Cheesy Sticks", 30.0)
            )),
            Restaurant("3", "Tiagos", listOf(
                MenuItem("1", "Spaghetti Bolognese", 70.0),
                MenuItem("2", "Lasagna", 75.0),
                MenuItem("3", "Carbonara", 65.0),
                MenuItem("4", "Garlic Bread", 20.0),
                MenuItem("5", "Salad", 35.0),
                MenuItem("6", "Tiramisu", 30.0)
            )),
            Restaurant("4", "KFC", listOf(
                MenuItem("1", "Original Chicken", 65.0),
                MenuItem("2", "Zinger Burger", 55.0),
                MenuItem("3", "Popcorn Chicken", 40.0),
                MenuItem("4", "Fries", 25.0),
                MenuItem("5", "Coke", 15.0),
                MenuItem("6", "Mashed Potato", 20.0)
            )),
            Restaurant("5", "Roccomamas", listOf(
                MenuItem("1", "Bacon Burger", 70.0),
                MenuItem("2", "Roccomamas Fries", 30.0),
                MenuItem("3", "Cheese Burger", 60.0),
                MenuItem("4", "Onion Rings", 25.0),
                MenuItem("5", "Milkshake", 30.0),
                MenuItem("6", "Chicken Wings", 50.0)
            )),

            Restaurant("6", "Simply Asia", listOf(
                MenuItem("1", "Pad Thai", 60.0),
                MenuItem("2", "Green Curry", 65.0),
                MenuItem("3", "Fried Rice", 50.0),
                MenuItem("4", "Spring Rolls", 25.0),
                MenuItem("5", "Coke", 15.0),
                MenuItem("6", "Mango Sticky Rice", 35.0)
            ))
        )

        adapter = RestaurantAdapter { restaurant ->
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("restaurantName", restaurant.name)
            intent.putExtra("restaurantId", restaurant.id)
            intent.putParcelableArrayListExtra("menu", ArrayList(restaurant.menu))
            startActivity(intent)
        }

        adapter.submitList(restaurants)
        recyclerView.adapter = adapter
    }
}
