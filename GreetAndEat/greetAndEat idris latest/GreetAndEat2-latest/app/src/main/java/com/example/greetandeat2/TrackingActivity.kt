package com.example.greetandeat2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TrackingActivity : AppCompatActivity() {

    private lateinit var tvStatus: TextView
    private lateinit var tvEstimatedTime: TextView
    private lateinit var tvOrderId: TextView
    private lateinit var tvStatusHistory: TextView
    private lateinit var btnRefresh: Button
    private lateinit var btnGoHome: Button

    private var orderId: String? = null
    private var userId: String? = null
    private var cartItems: ArrayList<MenuItem> = arrayListOf()
    private var totalAmount: Double = 0.0

    private var currentStatusIndex = 0
    private val statuses = listOf("ORDER_RECEIVED", "PREPARING", "READY_FOR_PICKUP", "OUT_FOR_DELIVERY", "DELIVERED")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)

        initializeViews()
        setupClickListeners()
        loadInitialStatus()
    }

    private fun initializeViews() {
        tvStatus = findViewById(R.id.tvTrackingStatus)
        tvEstimatedTime = findViewById(R.id.tvEstimatedTime)
        tvOrderId = findViewById(R.id.tvOrderId)
        tvStatusHistory = findViewById(R.id.tvStatusHistory)
        btnRefresh = findViewById(R.id.btnRefresh)
        btnGoHome = findViewById(R.id.btnGoHome)

        // Get extras safely
        orderId = intent.getStringExtra("orderId") ?: "testOrder123"
        userId = intent.getStringExtra("userId") ?: "guest"
        cartItems = intent.getParcelableArrayListExtra<MenuItem>("cartItems") ?: arrayListOf()
        totalAmount = intent.getDoubleExtra("totalAmount", cartItems.sumOf { it.price * it.quantity })

        tvOrderId.text = "Order #$orderId"
        tvStatus.text = "🔄 Ready to track order"
        tvEstimatedTime.text = "⏰ Estimated: 30-40 min"
        btnRefresh.text = "🔄 Progress Order"

        // Show initial cart summary
        if (cartItems.isNotEmpty()) {
            val summary = cartItems.joinToString("\n") { "${it.name} x${it.quantity} - R${it.price * it.quantity}" }
            tvStatusHistory.text = "Order Summary:\n$summary\n\nStatus history will appear here..."
        } else {
            tvStatusHistory.text = "No items in cart."
        }
    }

    private fun setupClickListeners() {
        btnRefresh.setOnClickListener {
            progressToNextStatus()
        }

        btnGoHome.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }

    private fun loadInitialStatus() {
        tvStatus.text = "Status: ORDER_RECEIVED"
    }

    private fun progressToNextStatus() {
        if (currentStatusIndex >= statuses.size - 1) return

        currentStatusIndex++
        val newStatus = statuses[currentStatusIndex]
        tvStatus.text = "Status: ${formatStatus(newStatus)}"
        tvEstimatedTime.text = getEstimatedTime(newStatus)

        val message = when (newStatus) {
            "PREPARING" -> "👨‍🍳 Kitchen started cooking!"
            "READY_FOR_PICKUP" -> "✅ Order ready for delivery!"
            "OUT_FOR_DELIVERY" -> "🚗 Driver on the way!"
            "DELIVERED" -> "🎉 Order delivered!"
            else -> "Status updated"
        }

        tvStatusHistory.append("\n⏰ ${getCurrentTime()}: $message")
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        if (newStatus == "DELIVERED") {
            btnRefresh.isEnabled = false
            btnRefresh.text = "✅ Delivered"
        }
    }

    private fun formatStatus(status: String): String {
        return when (status) {
            "ORDER_RECEIVED" -> "📥 Order Received"
            "PREPARING" -> "👨‍🍳 Preparing Food"
            "READY_FOR_PICKUP" -> "✅ Ready for Delivery"
            "OUT_FOR_DELIVERY" -> "🚗 Out for Delivery"
            "DELIVERED" -> "🎉 Delivered!"
            else -> status
        }
    }

    private fun getEstimatedTime(status: String): String {
        return when (status) {
            "ORDER_RECEIVED" -> "⏰ Estimated: 35-40 minutes"
            "PREPARING" -> "⏰ Estimated: 25-30 minutes"
            "READY_FOR_PICKUP" -> "⏰ Estimated: 15-20 minutes"
            "OUT_FOR_DELIVERY" -> "⏰ Estimated: 5-10 minutes"
            "DELIVERED" -> "✅ Delivered!"
            else -> "⏰ Time pending"
        }
    }

    private fun getCurrentTime(): String {
        val sdf = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }

    override fun onBackPressed() {
        // Go back to Home activity instead of closing app
        val intent = Intent(this, Home::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}
