package com.example.greetandeat2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RewardsActivity : AppCompatActivity() {

    private lateinit var rewardsRecycler: RecyclerView
    private lateinit var pointsText: TextView
    private lateinit var subscribeBtn: Button
    private var totalPoints = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_rewards)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pointsText = findViewById(R.id.points)
        subscribeBtn = findViewById(R.id.subscribeBtn)
        rewardsRecycler = findViewById(R.id.rewardsRecycler)

        // Build rewards list with unlock state from RewardsManager
        val rewardsList = listOf(
            Reward(
                "First Login",
                "Logged in for the first time",
                100,
                R.drawable.ic_login,
                if (RewardsManager.isRewardUnlocked(this, "first_login")) RewardLevel.GOLD else RewardLevel.SILVER
            ),
            Reward(
                "First Order",
                "Placed your first order",
                200,
                R.drawable.ic_order,
                if (RewardsManager.isRewardUnlocked(this, "first_order")) RewardLevel.GOLD else RewardLevel.SILVER
            ),
            Reward(
                "Stay Under Budget",
                "Spent under monthly budget",
                150,
                R.drawable.ic_budget,
                if (RewardsManager.isRewardUnlocked(this, "budget")) RewardLevel.GOLD else RewardLevel.SILVER
            ),
            Reward(
                "Loyal Customer",
                "5 orders completed",
                300,
                R.drawable.ic_loyal,
                if (RewardsManager.isRewardUnlocked(this, "loyal")) RewardLevel.GOLD else RewardLevel.SILVER
            ),
            Reward(
                "Big Spender",
                "Order over R500",
                250,
                R.drawable.ic_money,
                if (RewardsManager.isRewardUnlocked(this, "big_spender")) RewardLevel.GOLD else RewardLevel.SILVER
            ),
            Reward(
                "Daily Check-in",
                "Logged in 7 consecutive days",
                120,
                R.drawable.ic_daily,
                if (RewardsManager.isRewardUnlocked(this, "daily")) RewardLevel.GOLD else RewardLevel.SILVER
            )
        )

        // Calculate total points from unlocked rewards
        totalPoints = rewardsList.filter { it.level == RewardLevel.GOLD }.sumOf { it.points }
        pointsText.text = "Points: $totalPoints"

        rewardsRecycler.layoutManager = GridLayoutManager(this, 2)
        rewardsRecycler.adapter = RewardsAdapter(rewardsList) { reward ->
            // Click action for rewards
            when (reward.title) {
                "First Login" -> startActivity(Intent(this, Home::class.java))
                "First Order" -> startActivity(Intent(this, MenuActivity::class.java))
                "Stay Under Budget" -> Toast.makeText(this, "Budget reward page coming soon!", Toast.LENGTH_SHORT).show()
                "Loyal Customer" -> startActivity(Intent(this, TrackingActivity::class.java))
                "Big Spender" -> startActivity(Intent(this, CartActivity::class.java))
                "Daily Check-in" -> Toast.makeText(this, "Daily streak tracking!", Toast.LENGTH_SHORT).show()
            }
        }

        subscribeBtn.setOnClickListener {
            Toast.makeText(this, "Subscribed to Greet & Eat Pro!", Toast.LENGTH_SHORT).show()
        }
    }
}
