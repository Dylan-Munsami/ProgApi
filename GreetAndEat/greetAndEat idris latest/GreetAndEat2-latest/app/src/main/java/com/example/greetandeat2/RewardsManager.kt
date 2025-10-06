package com.example.greetandeat2

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

object RewardsManager {
    private const val PREFS_NAME = "rewards_prefs"

    // Unlock a reward
    fun unlockReward(context: Context, rewardId: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(rewardId, true).apply()
    }

    // Check if a reward is unlocked
    fun isRewardUnlocked(context: Context, rewardId: String): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(rewardId, false)
    }

    // --- Daily Check-in Logic ---
    fun handleDailyCheckIn(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val lastLogin = prefs.getString("last_login_date", null)
        val streak = prefs.getInt("login_streak", 0)

        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

        if (lastLogin == null || lastLogin != today) {
            val newStreak = if (lastLogin != null && isYesterday(lastLogin, today)) {
                streak + 1
            } else {
                1 // reset streak
            }

            prefs.edit()
                .putString("last_login_date", today)
                .putInt("login_streak", newStreak)
                .apply()

            if (newStreak >= 7) {
                unlockReward(context, "daily") // ✅ unlock reward after 7 days streak
            }
        }
    }

    private fun isYesterday(lastLogin: String, today: String): Boolean {
        val format = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val lastDate = format.parse(lastLogin)
        val todayDate = format.parse(today)

        val diff = (todayDate.time - lastDate.time) / (1000 * 60 * 60 * 24)
        return diff == 1L
    }
}
