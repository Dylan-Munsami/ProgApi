package com.example.greetandeat2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class Setting : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_setting)

        // Handle system bars padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // === UI Elements ===
        val editProfileTop = findViewById<TextView>(R.id.editProfile)
        val editProfileButton = findViewById<Button>(R.id.editProfileButton)
        val changePasswordButton = findViewById<Button>(R.id.changePasswordButton)
        val paymentMethodsButton = findViewById<Button>(R.id.managePaymentsButton)
        val logoutButton = findViewById<Button>(R.id.logoutButton)
        val themeSwitch = findViewById<Switch>(R.id.themeSwitch)
        val notificationSwitch = findViewById<Switch>(R.id.notificationSwitch)
        val languageSpinner = findViewById<Spinner>(R.id.languageSpinner)
        val userName = findViewById<TextView>(R.id.userName)
        val userEmail = findViewById<TextView>(R.id.userEmail)

        // === Load User Info ===
        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val name = sharedPref.getString("user_name", "Guest User")
        val email = sharedPref.getString("user_email", "guest@example.com")

        userName.text = name
        userEmail.text = email

        // === Edit Profile Button ===
        editProfileButton.setOnClickListener {
            Toast.makeText(this, "Edit Profile clicked", Toast.LENGTH_SHORT).show()
            // Example: Navigate to EditProfileActivity
            // startActivity(Intent(this, EditProfileActivity::class.java))
        }

        editProfileTop.setOnClickListener {
            Toast.makeText(this, "Edit Profile clicked", Toast.LENGTH_SHORT).show()
        }

        // === Change Password Button ===
        changePasswordButton.setOnClickListener {
            Toast.makeText(this, "Change Password clicked", Toast.LENGTH_SHORT).show()
        }

        // === Payment Methods ===
        paymentMethodsButton.setOnClickListener {
            Toast.makeText(this, "Manage Payment Methods clicked", Toast.LENGTH_SHORT).show()
        }


        logoutButton.setOnClickListener {
            val editor = sharedPref.edit()
            editor.clear()
            editor.apply()

            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()


            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // === Theme Switch ===
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Notifications Enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications Disabled", Toast.LENGTH_SHORT).show()
            }
        }


        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                val selected = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@Setting, "Language: $selected", Toast.LENGTH_SHORT).show()
                // TODO: Save language preference & apply locale change
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}
